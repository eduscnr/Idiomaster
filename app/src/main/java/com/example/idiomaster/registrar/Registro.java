package com.example.idiomaster.registrar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.idiomaster.databinding.ActivityRegistroBinding;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.repositorio.FirebasesImple;
import com.example.idiomaster.utils.InternetUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

public class Registro extends AppCompatActivity {
    private ActivityRegistroBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebasesImple firebasesImple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebasesImple = new FirebasesImple();
        binding.registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.email.getText().toString().isEmpty() && binding.password.getText().toString().equalsIgnoreCase(binding.passwordRepet.getText().toString())) {
                    String pass = binding.password.getText().toString().trim();
                    String ema = binding.email.getText().toString().trim();
                    System.out.println(pass);
                    System.out.println(ema);
                    firebaseAuth.createUserWithEmailAndPassword(ema, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // El usuario se ha creado exitosamente en Firebase Authentication
                                try {
                                    firebasesImple.registrarNuevoUsuario(ema);
                                } catch (JSONException e) {
                                    System.out.println(e.getMessage());
                                }
                                Toast.makeText(Registro.this, "Registro realizado", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Registro.this, IniciarSesion.class);
                                startActivity(i);
                                finish();
                            } else {
                                // Error al crear el usuario
                                Toast.makeText(Registro.this, "Usuario ya registrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registro.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
}