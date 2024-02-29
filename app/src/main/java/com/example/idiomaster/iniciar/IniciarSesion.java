package com.example.idiomaster.iniciar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.idiomaster.databinding.ActivityIniciarSesionBinding;
import com.example.idiomaster.modelo.Usuario;
import com.example.idiomaster.registrar.MainDrawer;
import com.example.idiomaster.registrar.Registro;
import com.example.idiomaster.repositorio.FirebasesImple;
import com.example.idiomaster.utils.InternetUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;


public class IniciarSesion extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0001;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ActivityIniciarSesionBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private static Usuario inicioSesionUsuario;
    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("232536227001-8vodv266emdtju434ksdpqhs2gj8eldu.apps.googleusercontent.com")
            .requestEmail()
            .build();
    private FirebasesImple firebasesImple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        firebasesImple = new FirebasesImple();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Inicio de sesión exitoso
                                    Toast.makeText(IniciarSesion.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                    Thread hilo = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                               firebasesImple.recuperarUsuario(email);
                                                // Espera 2 segundos
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            System.out.println("Usuario que se quiere logear: " + inicioSesionUsuario);
                                            // Luego de esperar 2 segundos, ejecuta el código
                                            Intent intent = new Intent(IniciarSesion.this, MainDrawer.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                    hilo.start();
                                } else {
                                    // Error al iniciar sesión
                                    Toast.makeText(IniciarSesion.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        binding.crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IniciarSesion.this, Registro.class);
                startActivity(i);
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Thread hilo = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            firebasesImple.recuperarUsuario(account.getEmail());
                            // Espera 2 segundos
                            Thread.sleep(2000);
                            if(inicioSesionUsuario != null){
                                Intent intent = new Intent(IniciarSesion.this, MainDrawer.class);
                                startActivity(intent);
                                finish();
                            }else{
                                firebasesImple.registrarNuevoUsuario(account.getEmail());
                                firebasesImple.recuperarUsuario(account.getEmail());
                                Thread.sleep(2000);
                                System.out.println(inicioSesionUsuario);
                                Intent intent = new Intent(IniciarSesion.this, MainDrawer.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        firebaseAuthWithGoogle(account.getIdToken());
                    }
                });

                hilo.start();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                Toast.makeText(IniciarSesion.this, "Error", Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    private void showNoInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sin conexión a Internet")
                .setMessage("Necesitas conectarte a Internet para usar esta aplicación.")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    }

    public static Usuario getInicioSesionUsuario() {
        return inicioSesionUsuario;
    }

    public static void setInicioSesionUsuario(Usuario inicioSesionUsuario) {
        IniciarSesion.inicioSesionUsuario = inicioSesionUsuario;
    }
}