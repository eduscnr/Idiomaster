package com.example.idiomaster.ui.cuentos;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idiomaster.R;
import com.example.idiomaster.adaptadores.AdaptadorCuentos;
import com.example.idiomaster.databinding.FragmentStoriesBinding;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Cuento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CuentosFragment extends Fragment {

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
        rvCuentos.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCuentos.setHasFixedSize(true);
        facil =binding.easyButton;
        facil.setTag("facil");
        intermedio =binding.intermediateButton;
        intermedio.setTag("intermedio");
        dificil =binding.advancedButton;
        dificil.setTag("dificil");

        rdgDificultad =binding.difficultyRadioGroup;

        rdgDificultad.setOnCheckedChangeListener((group, checkedId) -> {
            facil.setPaintFlags(0);
            intermedio.setPaintFlags(0);
            dificil.setPaintFlags(0);

            RadioButton selectedButton =  root.findViewById((checkedId));
            String dificultadSeleccionada = (String) selectedButton.getTag();
            Log.d("Boton seleccionado:", (String) selectedButton.getText());
            if (selectedButton != null) {
                Log.d("", "se va a pintar el boton " + selectedButton.getText());
                selectedButton.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            }
            ponerCuentos(dificultadSeleccionada);


        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void ponerCuentos(String dificultad){
        List<Cuento>cuentos = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("historias/"+ IniciarSesion.getInicioSesionUsuario().getIdioma()+"/" + dificultad);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {
                    Cuento c = d.getValue(Cuento.class);
                    cuentos.add(c);
                }
                adaptadorCuentos = new AdaptadorCuentos(cuentos,listener);
                rvCuentos.setAdapter(adaptadorCuentos);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}