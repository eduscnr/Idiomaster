package com.example.idiomaster.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.idiomaster.MainActivity;
import com.example.idiomaster.R;
import com.example.idiomaster.adaptadores.AdaptadorMundo;
import com.example.idiomaster.databinding.FragmentHomeBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idiomaster.adaptadores.AdaptadorNivel;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Mundo;
import com.example.idiomaster.modelo.Nivel;
import com.example.idiomaster.ui.minijuegos.TraducePalabras;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AdaptadorNivel.listener, AdaptadorMundo.listener{
    private List<Nivel> niveles;
    private List<Mundo> mundos;
    private AdaptadorNivel.listener listener;
    private AdaptadorMundo.listener listenerMundo;
    private FragmentHomeBinding binding;
    private AdaptadorNivel adaptadorNivel;
    private AdaptadorMundo adaptadorMundo;
    private RecyclerView recyclerViewNiveles;
    private RecyclerView recyclerViewMundos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/
        niveles = obtenerNiveles();
        mundos = obtenerMundos();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listener = this::onClickCardView;
        listenerMundo = this::onClickCardViewMundo;
        recyclerViewNiveles = root.findViewById(R.id.recyclerViewNiveles);
        recyclerViewNiveles.setHasFixedSize(true);
        recyclerViewNiveles.setLayoutManager(new LinearLayoutManager(requireContext()));



        recyclerViewMundos = root.findViewById(R.id.recyclerViewMundos);
        recyclerViewMundos.setHasFixedSize(true);
        recyclerViewMundos.setLayoutManager(new LinearLayoutManager(requireContext()));
        AdaptadorMundo adaptadorMundo = new AdaptadorMundo(mundos, this::onClickCardViewMundo);
        recyclerViewMundos.setAdapter(adaptadorMundo);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public List<Nivel> obtenerNiveles(){
        List<Nivel> niveles = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("minijuegos/"+ IniciarSesion.getInicioSesionUsuario().getIdioma()+"/mundos");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Mundo mundo = d.getValue(Mundo.class);
                    for (Nivel n : mundo.getNiveles()){
                        niveles.add(n);
                    }
                }
                adaptadorNivel = new AdaptadorNivel(niveles, listener);
                recyclerViewNiveles.setAdapter(adaptadorNivel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return niveles;
    }
    public List<Mundo> obtenerMundos(){
        List<Mundo>mundos = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("minijuegos/"+IniciarSesion.getInicioSesionUsuario().getIdioma()+"/mundos");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Mundo mundo = d.getValue(Mundo.class);
                    mundos.add(mundo);
                }
                adaptadorMundo = new AdaptadorMundo(mundos, listenerMundo);
                recyclerViewMundos.setAdapter(adaptadorMundo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return mundos;
    }

    @Override
    public void onClickCardView(int posicion) {
        MainActivity.setNivelSeleccionado(niveles.get(posicion));
        System.out.println(IniciarSesion.getInicioSesionUsuario().getProgresoNivel());
        if(niveles.get(posicion).getId()==IniciarSesion.getInicioSesionUsuario().getProgresoNivel()){
            Intent traducePalabras = new Intent(requireContext(), TraducePalabras.class);
            requireContext().startActivity(traducePalabras);
        } else if (niveles.get(posicion).getId() < IniciarSesion.getInicioSesionUsuario().getProgresoNivel()) {
            Toast.makeText(requireContext(), "Nivel completado pasa al sigueinte", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(requireContext(), "No tienes el nivel desbloqueado", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickCardViewMundo(int posicion){
        Mundo mundoSeleccionado = mundos.get(posicion);
        System.out.println(mundoSeleccionado.getId());
        System.out.println(IniciarSesion.getInicioSesionUsuario().getProgresoMundo());
        if(mundoSeleccionado.getId()<= IniciarSesion.getInicioSesionUsuario().getProgresoMundo()){
            List<Nivel> nivelesDeUnMundo = mundoSeleccionado.getNiveles();
            AdaptadorNivel adaptadorNivel = new AdaptadorNivel(nivelesDeUnMundo, this::onClickCardView);
            recyclerViewMundos.setVisibility(View.INVISIBLE);
            recyclerViewNiveles.setVisibility(View.VISIBLE);
            recyclerViewNiveles.setAdapter(adaptadorNivel);
        }else{
            Toast.makeText(requireContext(), "No tienes el mundo desbloqueado", Toast.LENGTH_SHORT).show();
        }

    }
}