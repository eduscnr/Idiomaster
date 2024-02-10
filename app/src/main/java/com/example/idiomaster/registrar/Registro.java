package com.example.idiomaster.registrar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.idiomaster.MainActivity;
import com.example.idiomaster.R;
import com.example.idiomaster.databinding.ActivityRegistroBinding;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.repositorio.DaoImplement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends AppCompatActivity {
    private ActivityRegistroBinding binding;
    private FirebaseAuth firebaseAuth;
    private DaoImplement daoImplement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        daoImplement = new DaoImplement(Registro.this);
        binding.registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.email.getText().toString().isEmpty()
                        && binding.password.getText().toString().equalsIgnoreCase(binding.passwordRepet.getText().toString())){
                    String pass = binding.password.getText().toString().trim();
                    String ema = binding.email.getText().toString().trim();
                    System.out.println(pass);
                    System.out.println(ema);
                    firebaseAuth.createUserWithEmailAndPassword(ema,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // El usuario se ha creado exitosamente en Firebase Authentication
                                        if(daoImplement.buscarUsuario(ema)){
                                            System.out.println("existe");
                                            Toast.makeText(Registro.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(Registro.this, "Registro realizado", Toast.LENGTH_SHORT).show();
                                            daoImplement.registrarUsuario(ema);
                                            Intent i = new Intent(Registro.this, IniciarSesion.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    } else {
                                        // Error al crear el usuario
                                        Toast.makeText(Registro.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
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
}