package com.example.idiomaster.ui.cuentos;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.idiomaster.databinding.ActivityLeerCuentoBinding;
import com.example.idiomaster.modelo.Cuento;
import com.example.idiomaster.registrar.MainDrawer;

public class LeerCuento extends AppCompatActivity implements View.OnClickListener{
    private ActivityLeerCuentoBinding binding;
    private TextView txtCuento;
    private TextView txtAutor;
    private TextView txtTitulo;


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeerCuentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txtCuento = binding.txtContenidoLeerCuento;
        txtAutor = binding.txtAutorLeerCuento;
        txtTitulo = binding.txtTituloLeerCuento;
        Cuento cuento= MainDrawer.getCuentoSeleccionado();
        txtTitulo.setText(cuento.getTitulo());
        txtAutor.setText(cuento.getAutor());
        txtCuento.setText(cuento.getCuento());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }
}
