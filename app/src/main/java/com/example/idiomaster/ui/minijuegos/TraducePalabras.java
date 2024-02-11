package com.example.idiomaster.ui.minijuegos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idiomaster.MainActivity;
import com.example.idiomaster.R;
import com.example.idiomaster.databinding.ActivityTraducePalabrasBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TraducePalabras extends AppCompatActivity implements View.OnClickListener {
    private ActivityTraducePalabrasBinding binding;
    private List<String> opciones;
    private GridLayout gridLayout;
    private Button btnvalidar;
    private int indiceActual = 0;
    private String respuestPulsada;
    private TextView tvCorrectoInCorrecto;
    private List<String> opcionesBarajadas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTraducePalabrasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvCorrectoInCorrecto = binding.textviewCorrectoIncorrecto;
        gridLayout = binding.gridJugar;

        opciones = new ArrayList<>();
        binding.palabraTraducir.setText(MainActivity.getNivelSeleccionado().getPalabras().get(indiceActual));

        int palabrasSize = MainActivity.getNivelSeleccionado().getPalabras().size();
        for (int i = 0; i < palabrasSize; i++) {
            opciones.add(MainActivity.getNivelSeleccionado().getPalabras().get(i));
        }

        aniadeHijos(2);
        recorrer();

        btnvalidar = binding.validar;
        btnvalidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnvalidar.isEnabled()) {
                    validarRespuesta();;
                    aniadeHijos(2);
                }
            }
        });

        // Obtén el idioma del sistema
        Configuration config = getResources().getConfiguration();
        Locale systemLocale = config.getLocales().get(0);

        // Obtén el código de idioma del sistema
        String languageCode = systemLocale.getLanguage();
        TextView textView = findViewById(R.id.palabraTraducir);

        if (languageCode.equals("es")) {
            textView.setText("manzana");
        } else if (languageCode.equals("en")) {
            textView.setText("apple");
        } else if (languageCode.equals("it")) {
            textView.setText("mela");
        }
    }

    public void aniadeHijos(int k) {
        opcionesBarajadas = new ArrayList<>(opciones);
        Collections.shuffle(opcionesBarajadas);
        Button b;
        int indiceRespuestaCorrecta = new Random().nextInt(3);
        for (int i = 0; i <= k; i++) {
            b = new Button(binding.getRoot().getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(8, 8, 8, 8); // Margen izquierdo, superior, derecho e inferior
            b.setLayoutParams(params);
            b.setId(View.generateViewId());
            if (i == indiceRespuestaCorrecta && indiceActual < MainActivity.getNivelSeleccionado().getPalabras().size()) {
                // Este es el botón que contendrá la respuesta correcta
                b.setText(opciones.get(indiceActual));
            } else {
                // Estos son los otros botones con respuestas incorrectas aleatorias
                int indiceOpcionIncorrecta = new Random().nextInt(opcionesBarajadas.size());
                while (indiceOpcionIncorrecta == indiceActual) {
                    // Asegura que la opción incorrecta no sea la respuesta correcta
                    indiceOpcionIncorrecta = new Random().nextInt(opcionesBarajadas.size());
                }
                b.setText(opcionesBarajadas.get(indiceOpcionIncorrecta));
            }
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
        respuestPulsada = button.getText().toString();
        if (view.getClass().getSimpleName().equals("Button")){
            Button b = (Button) view;
            recorrer();
            b.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200));
        }
    }

    public void validarRespuesta(){
        //Comprobar si es correcto y si no
        if(respuestPulsada != null){
            if (respuestPulsada.equalsIgnoreCase(binding.palabraTraducir.getText().toString())) {
                //Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                tvCorrectoInCorrecto.setText("CORRECTO");
                tvCorrectoInCorrecto.setTextColor(ContextCompat.getColor(this, R.color.teal_700));
                indiceActual++;
            } else {
                //Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                tvCorrectoInCorrecto.setText("INCORRECTO");
                tvCorrectoInCorrecto.setTextColor(ContextCompat.getColor(this, R.color.purple_200));
                indiceActual++;
            }
            //Aumentar el indice para que pase a la siguiente palabra independientemente si se equivoca o acierta
            if (indiceActual <= MainActivity.getNivelSeleccionado().getPalabras().size() && indiceActual < MainActivity.getNivelSeleccionado().getPalabras().size()) {
                binding.palabraTraducir.setText(MainActivity.getNivelSeleccionado().getPalabras().get(indiceActual));
            } else {
                // Toast.makeText(this, "¡Juego completado!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}