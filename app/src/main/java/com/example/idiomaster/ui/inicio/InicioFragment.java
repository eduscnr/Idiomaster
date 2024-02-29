package com.example.idiomaster.ui.inicio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.idiomaster.R;
import com.example.idiomaster.adaptadores.AdaptadorMundo;
import com.example.idiomaster.databinding.FragmentHomeBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idiomaster.adaptadores.AdaptadorNivel;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Mundo;
import com.example.idiomaster.modelo.Nivel;
import com.example.idiomaster.registrar.MainDrawer;
import com.example.idiomaster.ui.minijuegos.TraducePalabras;
import com.example.idiomaster.utils.InternetUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment implements AdaptadorNivel.listener, AdaptadorMundo.listener{
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
        System.out.println("Vuelvo a cargar");
        obtenerMundos();
        obtenerNiveles();
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
        binding.salirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerMundos();
                obtenerNiveles();
                recyclerViewMundos.setVisibility(View.VISIBLE);
                recyclerViewNiveles.setVisibility(View.INVISIBLE);
                binding.salirButton.setVisibility(View.INVISIBLE);
            }
        });
        binding.salirButton.setVisibility(View.INVISIBLE);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void obtenerNiveles(){
        List<Nivel> nivelesFirebase = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("minijuegos/"+ IniciarSesion.getInicioSesionUsuario().getIdioma()+"/mundos");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Mundo mundo = d.getValue(Mundo.class);
                    for (Nivel n : mundo.getNiveles()){
                        nivelesFirebase.add(n);
                    }
                }
                adaptadorNivel = new AdaptadorNivel(nivelesFirebase, listener);
                recyclerViewNiveles.setAdapter(adaptadorNivel);
                niveles = nivelesFirebase;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void obtenerMundos(){
        List<Mundo>mundosFirebases = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("minijuegos/"+IniciarSesion.getInicioSesionUsuario().getIdioma()+"/mundos");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Mundo mundo = d.getValue(Mundo.class);
                    mundosFirebases.add(mundo);
                }
                adaptadorMundo = new AdaptadorMundo(mundosFirebases, listenerMundo);
                recyclerViewMundos.setAdapter(adaptadorMundo);
                mundos = mundosFirebases;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClickCardView(int posicion) {
        //Indica el nivel seleccionado en el MainActivity
        MainDrawer.setNivelSeleccionado(niveles.get(posicion));
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
        MainDrawer.setMundoActual(mundoSeleccionado);
        if(mundoSeleccionado.getId()<= IniciarSesion.getInicioSesionUsuario().getProgresoMundo()){
            List<Nivel> nivelesDeUnMundo = mundoSeleccionado.getNiveles();
            niveles = nivelesDeUnMundo;
            AdaptadorNivel adaptadorNivel = new AdaptadorNivel(nivelesDeUnMundo, this::onClickCardView);
            recyclerViewMundos.setVisibility(View.INVISIBLE);
            recyclerViewNiveles.setVisibility(View.VISIBLE);
            binding.salirButton.setVisibility(View.VISIBLE);
            recyclerViewNiveles.setAdapter(adaptadorNivel);
        }else{
            Toast.makeText(requireContext(), "No tienes el mundo desbloqueado", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        InternetUtil.isOnline(new InternetUtil.OnOnlineCheckListener() {
            @Override
            public void onResult(boolean isOnline) {
                if (!isOnline) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showNoInternetDialog();
                        }
                    });
                }
            }
        });
        if(adaptadorNivel != null){
            adaptadorNivel = new AdaptadorNivel(niveles, listener);
            recyclerViewNiveles.setAdapter(adaptadorNivel);
        }
        super.onResume();
    }

    private void showNoInternetDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Sin conexión a Internet")
                .setMessage("Necesitas conectarte a Internet para usar esta aplicación.")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requireActivity().finishAffinity();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}