package com.example.idiomaster.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.idiomaster.R;
import com.example.idiomaster.databinding.FragmentJugarBinding;

public class AJugar extends Fragment implements View.OnClickListener{

    Button btnPera;
    Button btnManzana;
    Button btnNaranja;
    TextView textViewPalabra;
    private FragmentJugarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJugarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnPera = root.findViewById(R.id.buttonPera);
        btnManzana = root.findViewById(R.id.buttonManzana);
        btnNaranja = root.findViewById(R.id.buttonNaranja);
        btnPera.setOnClickListener(this);
        btnManzana.setOnClickListener(this);
        btnNaranja.setOnClickListener(this);
        textViewPalabra = root.findViewById(R.id.textViewPalabra);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        Button btnSeleccionado = (Button) v;
        if(btnSeleccionado.getText().toString().equalsIgnoreCase(textViewPalabra.getText().toString())){
            Toast.makeText(getContext(),"¡ES CORRECTO!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),"ES INCORRECTO",Toast.LENGTH_LONG).show();
        }
    }
}