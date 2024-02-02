package com.example.idiomaster.ui.minijuegos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.idiomaster.MainActivity;
import com.example.idiomaster.R;
import com.example.idiomaster.databinding.ActivityTraducePalabrasBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TraducePalabras extends AppCompatActivity implements View.OnClickListener {
    private ActivityTraducePalabrasBinding binding;
    private List<String> opciones;
    private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTraducePalabrasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        opciones = new ArrayList<>();
        Random random = new Random();
        binding.palabraTraducir.setText(MainActivity.getNivelSeleccionado().getPalabras().get(0));
        gridLayout = binding.gridJugar;
        for (int i = 0; i <= 10; i++) {
            opciones.add(MainActivity.getNivelSeleccionado().getPalabras().get(i));
        }
        aniadeHijos(10);
        recorrer();
    }

    public void aniadeHijos(int k) {
        gridLayout.setColumnCount(1);
        gridLayout.setRowCount(3);
        Button b;
        for (int i = 0; i <= k; i++) {
            b = new Button(binding.getRoot().getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8); // Margen izquierdo, superior, derecho e inferior
            b.setLayoutParams(params);
            b.setId(View.generateViewId());
            b.setText(opciones.get(i));
            b.setTextColor(Color.BLACK);
            b.setOnClickListener(this);
            gridLayout.addView(b, i);
        }
    }

    private void recorrer() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            }
        }
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;

        if (view.getClass().getSimpleName().equals("Button")){
            Button b = (Button) view;
            recorrer();
            b.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }

        if(button.getText().toString().equalsIgnoreCase(binding.palabraTraducir.getText().toString())){
            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
        }
    }
}