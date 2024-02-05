package com.example.idiomaster.iniciar;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.idiomaster.MainActivity;
import com.example.idiomaster.R;
import com.example.idiomaster.database.DatabaseSqlite;
import com.example.idiomaster.databinding.ActivityIniciarSesionBinding;
import com.example.idiomaster.modelo.Usuario;
import com.example.idiomaster.registrar.Registro;
import com.example.idiomaster.repositorio.DaoImplement;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class IniciarSesion extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0001;
    private FirebaseAuth firebaseAuth;
    private ActivityIniciarSesionBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private static Usuario inicioSesionUsuario;
    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("232536227001-8vodv266emdtju434ksdpqhs2gj8eldu.apps.googleusercontent.com")
            .requestEmail()
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIniciarSesionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseSqlite databaseSqlite = new DatabaseSqlite(this);
        databaseSqlite.getReadableDatabase();
        firebaseAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
                                DaoImplement daoImplement = new DaoImplement(IniciarSesion.this);
                                inicioSesionUsuario = daoImplement.iniciarSesionUsuario(email);
                                Intent intent = new Intent(IniciarSesion.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Esto evita que el usuario regrese a la pantalla de inicio de sesión presionando el botón "Atrás"
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
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                //Para lanzar otra actividad
                Intent i = new Intent(IniciarSesion.this, MainActivity.class);
                startActivity(i);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
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

    public static Usuario getInicioSesionUsuario() {
        return inicioSesionUsuario;
    }

    public static void setInicioSesionUsuario(Usuario inicioSesionUsuario) {
        IniciarSesion.inicioSesionUsuario = inicioSesionUsuario;
    }
}