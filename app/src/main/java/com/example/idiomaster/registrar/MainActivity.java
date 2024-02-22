package com.example.idiomaster.registrar;

import android.os.Bundle;

import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Mundo;
import com.example.idiomaster.modelo.Nivel;
import com.example.idiomaster.repositorio.DaoImplement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.idiomaster.R;
import com.example.idiomaster.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static Nivel nivelSeleccionado;
    private static String idiomaSeleccionado = IniciarSesion.getInicioSesionUsuario().getIdioma();
    private static Mundo mundoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public static Nivel getNivelSeleccionado() {
        return nivelSeleccionado;
    }

    public static void setNivelSeleccionado(Nivel nivelSeleccionado) {
        MainActivity.nivelSeleccionado = nivelSeleccionado;
    }

    public static String getIdiomaSeleccionado() {
        return idiomaSeleccionado;
    }

    public static void setIdiomaSeleccionado(String idiomaSeleccionado) {
        MainActivity.idiomaSeleccionado = idiomaSeleccionado;
    }

    public static Mundo getMundoActual() {
        return mundoActual;
    }

    public static void setMundoActual(Mundo mundoActual) {
        MainActivity.mundoActual = mundoActual;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DaoImplement dao = new DaoImplement();
        dao.actualizarProgresoFirebase(IniciarSesion.getInicioSesionUsuario().getEmail(),IniciarSesion.getInicioSesionUsuario().getIdioma(),
                IniciarSesion.getInicioSesionUsuario().getProgresoMundo(), IniciarSesion.getInicioSesionUsuario().getProgresoNivel());
    }
}