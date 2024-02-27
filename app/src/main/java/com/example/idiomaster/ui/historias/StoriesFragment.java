package com.example.idiomaster.ui.historias;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idiomaster.R;
import com.example.idiomaster.adaptadores.AdaptadorCuentos;
import com.example.idiomaster.adaptadores.AdaptadorMundo;
import com.example.idiomaster.databinding.FragmentStoriesBinding;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Cuento;
import com.example.idiomaster.modelo.Mundo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoriesFragment extends Fragment {

    private FragmentStoriesBinding binding;
    private RecyclerView rvCuentos;
    private RadioButton facil;
    private RadioButton intermedio;
    private RadioButton dificil;
    private RadioGroup rdgDificultad;
    private List<Cuento>cuentosSeleccionados = new ArrayList<>();
    private AdaptadorCuentos adaptadorCuentos;
    private AdaptadorCuentos.listener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories, container, false);
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentStoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rvCuentos = binding.rvCuentos;
        facil =binding.easyButton;
        intermedio =binding.intermediateButton;
        dificil =binding.advancedButton;

        rdgDificultad =binding.difficultyRadioGroup;

        rdgDificultad.setOnCheckedChangeListener((group, checkedId) -> {
            facil.setPaintFlags(0);
            intermedio.setPaintFlags(0);
            dificil.setPaintFlags(0);

            RadioButton selectedButton =  view.findViewById((checkedId));
            String dificultadSeleccionada = (String) selectedButton.getText();
            Log.d("Boton seleccionado:", (String) selectedButton.getText());
            if (selectedButton != null) {
                Log.d("", "se va a pintar el boton " + selectedButton.getText());
                selectedButton.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            }
            intermedio.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            //TODO OBTENER CUENTOS SELECCIONADOS
            cuentosSeleccionados = obtenerCuentos(dificultadSeleccionada);
            adaptadorCuentos = new AdaptadorCuentos(cuentosSeleccionados,listener);
            rvCuentos.setAdapter(adaptadorCuentos);

        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public List<Cuento> obtenerCuentos(String dificultad){
        List<Cuento>cuentos = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("historias/"+ IniciarSesion.getInicioSesionUsuario().getIdioma()+"/" + dificultad);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Cuento cuento = d.getValue(Cuento.class);
                    cuentos.add(cuento);
                }
                /*
                adaptadorMundo = new AdaptadorMundo(mundos, listenerMundo);
                recyclerViewMundos.setAdapter(adaptadorMundo);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return cuentos;
    }
}