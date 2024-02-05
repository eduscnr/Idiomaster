package com.example.idiomaster.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.idiomaster.R;
import com.example.idiomaster.databinding.FragmentHomeBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idiomaster.adaptadores.AdaptadorNivel;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Mundo;
import com.example.idiomaster.modelo.Nivel;
import com.example.idiomaster.MainActivity;
import com.example.idiomaster.ui.minijuegos.TraducePalabras;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AdaptadorNivel.listener{
    private List<Nivel> niveles;
    private AdaptadorNivel.listener listener;
    private FragmentHomeBinding binding;
    private AdaptadorNivel adaptadorNivel;
    private RecyclerView r;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);*/
        niveles = obtenerNiveles();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listener = this;
        r = root.findViewById(R.id.recyclerView);
        AdaptadorNivel adaptadorNivel = new AdaptadorNivel(niveles, this);
        r.setHasFixedSize(true);
        r.setLayoutManager(new LinearLayoutManager(requireContext()));
        System.out.println(IniciarSesion.getInicioSesionUsuario());
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
        DatabaseReference databaseReference = firebaseDatabase.getReference("minijuegos/"+IniciarSesion.getInicioSesionUsuario().getIdioma()+"/mundos");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Mundo mundo = d.getValue(Mundo.class);
                    System.out.println(mundo);
                    for (Nivel n : mundo.getNiveles()){
                        System.out.println(n);
                        niveles.add(n);
                    }
                }
                adaptadorNivel = new AdaptadorNivel(niveles, listener);
                System.out.println(niveles);
                r.setAdapter(adaptadorNivel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return niveles;
    }

    @Override
    public void onClickCardView(int posicion) {
        System.out.println(niveles.get(posicion));
        MainActivity.setNivelSeleccionado(niveles.get(posicion));
        Intent traducePalabras = new Intent(requireContext(), TraducePalabras.class);
        requireContext().startActivity(traducePalabras);
    }
}