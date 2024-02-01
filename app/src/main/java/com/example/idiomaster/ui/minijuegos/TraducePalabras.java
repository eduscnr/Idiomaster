package com.example.idiomaster.ui.minijuegos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.idiomaster.databinding.ActivityTraducePalabrasBinding;
import com.example.idiomaster.registrar.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class TraducePalabras extends AppCompatActivity implements View.OnClickListener {
    private ActivityTraducePalabrasBinding binding;
    private List<String> opciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTraducePalabrasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        opciones = new ArrayList<>();
        binding.palabraTraducir.setText(MainActivity.getNivelSeleccionado().getPalabras().get(0));
        opciones.add(MainActivity.getNivelSeleccionado().getPalabras().get(0));
        opciones.add(MainActivity.getNivelSeleccionado().getPalabras().get(6));
        opciones.add(MainActivity.getNivelSeleccionado().getPalabras().get(5));
        binding.palabraUno.setOnClickListener(this);
        binding.palabraUno.setText(opciones.get(2));
        binding.palabraDos.setOnClickListener(this);
        binding.palabraDos.setText(opciones.get(1));
        binding.palabraTres.setOnClickListener(this);
        binding.palabraTres.setText(opciones.get(0));
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        if(button.getText().toString().equalsIgnoreCase(binding.palabraTraducir.getText().toString())){
            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
        }
    }
}