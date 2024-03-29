package com.example.idiomaster.ui.minijuegos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.idiomaster.R;
import com.example.idiomaster.databinding.ActivityTraducePalabrasBinding;
import com.example.idiomaster.dialogo.FinalizarJuego;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Usuario;
import com.example.idiomaster.registrar.MainDrawer;
import com.example.idiomaster.repositorio.FirebasesImple;
import com.example.idiomaster.utils.InternetUtil;
import com.example.idiomaster.victoriaderrota.Derrota;
import com.example.idiomaster.victoriaderrota.Victoria;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TraducePalabras extends AppCompatActivity implements View.OnClickListener, FinalizarJuego.IDialogoLisenerOnClick{
    private ActivityTraducePalabrasBinding binding;
    private List<String> opciones;
    private GridLayout gridLayout;
    private Button btnvalidar;
    private int indiceActual = 0;
    private String respuestPulsada;
    private TextView tvCorrectoInCorrecto;
    private List<String> opcionesBarajadas;
    private String sistemaIdioma;
    private int fallos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTraducePalabrasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvCorrectoInCorrecto = binding.textviewCorrectoIncorrecto;
        gridLayout = binding.gridJugar;
        System.out.println("Niveles totales: "+ MainDrawer.getMundoActual().getNiveles().size());
        opciones = new ArrayList<>();
        binding.palabraTraducir.setText(MainDrawer.getNivelSeleccionado().getPalabras().get(indiceActual));

        int palabrasSize = MainDrawer.getNivelSeleccionado().getPalabras().size();
        for (int i = 0; i < palabrasSize; i++) {
            opciones.add(MainDrawer.getNivelSeleccionado().getPalabras().get(i));
        }
        // Obtén el idioma del sistema
        Configuration config = getResources().getConfiguration();
        Locale systemLocale = config.getLocales().get(0);
        sistemaIdioma = systemLocale.getLanguage();
        System.out.println(sistemaIdioma);

        // Traducir palabra al idioma del sistema
        //traducirTexto(binding.palabraTraducir, "es", sistemaIdioma);
        traducirTexto(binding.palabraTraducir, IniciarSesion.getInicioSesionUsuario().getIdioma(), sistemaIdioma);
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
        binding.botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalizarJuego finalizarJuego = new FinalizarJuego();
                finalizarJuego.show(getSupportFragmentManager(), "Parar");
            }
        });
    }
    private void traducirTexto(final TextView textView, String sourceLanguage, String targetLanguage) {
        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(targetLanguage)
                .build();
        Translator translator = Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        translator.translate(textView.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        textView.setText(s);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TraducePalabras.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TraducePalabras.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("ResourceAsColor")
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
            if (i == indiceRespuestaCorrecta && indiceActual < MainDrawer.getNivelSeleccionado().getPalabras().size()) {
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
            b.setTextColor(Color.WHITE);
            b.setBackgroundColor(R.color.azul);
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
        //Comprobarsi es correcto y si no
        if(respuestPulsada != null){
            // Obtener texto del TextView y limpiar cualquier espacio adicional
            String textoTraducido = MainDrawer.getNivelSeleccionado().getPalabras().get(indiceActual).trim();
            // Limpiar cualquier espacio adicional de la respuesta seleccionada
            String respuestaSeleccionada = respuestPulsada.trim();

            if (respuestaSeleccionada.equalsIgnoreCase(textoTraducido)){
                //Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show();
                tvCorrectoInCorrecto.setText(R.string.correcto);
                tvCorrectoInCorrecto.setTextColor(ContextCompat.getColor(this, R.color.azul));
                indiceActual++;
            } else {
                //Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
                tvCorrectoInCorrecto.setText(R.string.incorrecto);
                tvCorrectoInCorrecto.setTextColor(ContextCompat.getColor(this, R.color.rojo));
                fallos++;
                indiceActual++;
            }
            //Aumentar el indice para que pase a la siguiente palabra independientemente si se equivoca o acierta
            if (indiceActual <= MainDrawer.getNivelSeleccionado().getPalabras().size() && indiceActual < MainDrawer.getNivelSeleccionado().getPalabras().size()) {
                binding.palabraTraducir.setText(MainDrawer.getNivelSeleccionado().getPalabras().get(indiceActual));
                traducirTexto(binding.palabraTraducir, IniciarSesion.getInicioSesionUsuario().getIdioma(), sistemaIdioma);
            } else {
                // Toast.makeText(this, "¡Juego completado!", Toast.LENGTH_SHORT).show();
                if(fallos>=3){
                    Intent derrotaIntent = new Intent(this, Derrota.class);
                    startActivity(derrotaIntent);

                }else{
                    Intent victoriaIntent = new Intent(this, Victoria.class);
                    startActivity(victoriaIntent);
                    IniciarSesion.getInicioSesionUsuario().setProgresoNivel(IniciarSesion.getInicioSesionUsuario().getProgresoNivel()+1);
                    if(MainDrawer.getNivelSeleccionado().getId() == MainDrawer.getMundoActual().getNiveles().get(MainDrawer.getMundoActual().getNiveles().size()-1).getId()){
                        IniciarSesion.getInicioSesionUsuario().setProgresoMundo(IniciarSesion.getInicioSesionUsuario().getProgresoMundo()+1);
                    }
                    FirebasesImple firebaseData = new FirebasesImple();
                    Usuario usuario = IniciarSesion.getInicioSesionUsuario();
                    firebaseData.actualizarProgresoFirebase(usuario.getEmail(), usuario.getIdioma(), usuario.getProgresoMundo(),usuario.getProgresoNivel());
                }
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        InternetUtil.isOnline(new InternetUtil.OnOnlineCheckListener() {
            @Override
            public void onResult(boolean isOnline) {
                if (!isOnline) {
                        runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showNoInternetDialog();
                        }
                    });
                }
            }
        });
        super.onResume();
    }
    private void showNoInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sin conexión a Internet")
                .setMessage("Necesitas conectarte a Internet para usar esta aplicación.")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBotonDialogoClic() {
        finish();
    }
}