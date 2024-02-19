package com.example.idiomaster.victoriaderrota;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.idiomaster.R;
import com.example.idiomaster.databinding.ActivityDerrotaBinding;
import com.example.idiomaster.databinding.ActivityVictoriaBinding;

public class Derrota extends AppCompatActivity {
    private ActivityDerrotaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDerrotaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}