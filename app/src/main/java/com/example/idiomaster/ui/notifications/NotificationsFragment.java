package com.example.idiomaster.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.idiomaster.MainActivity;
import com.example.idiomaster.R;
import com.example.idiomaster.adaptadores.AdaptadorIdiomas;
import com.example.idiomaster.databinding.FragmentNotificationsBinding;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Usuario;
import com.example.idiomaster.repositorio.DaoImplement;
import com.example.idiomaster.repositorio.IDao;

public class NotificationsFragment extends Fragment implements Spinner.OnItemSelectedListener{

    private FragmentNotificationsBinding binding;
    private String[] idiomas= {"Español", "Italiano", "Ingles"};
    private String idioma;
    private DaoImplement doa;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);*/

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Spinner spinner = root.findViewById(R.id.spinner);
        AdaptadorIdiomas adaptadorIdiomas = new AdaptadorIdiomas(requireContext(), R.layout.item_idioma, idiomas);
        spinner.setAdapter(adaptadorIdiomas);
        spinner.setOnItemSelectedListener(this);
        doa = new DaoImplement(requireContext());
        binding.correoElectronico.setText(IniciarSesion.getInicioSesionUsuario().getEmail());
        binding.guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = IniciarSesion.getInicioSesionUsuario();
                doa.actualizarProgresoFirebase(usuario.getEmail(), usuario.getIdioma(), usuario.getProgresoMundo(), usuario.getProgresoNivel());
                doa.cambiarIdiomaFirebase(usuario.getEmail(), idioma);
                /*doa.actualizarProgreso(usuario.getEmail(), usuario.getIdioma(), usuario.getProgresoMundo(), usuario.getProgresoNivel());
                IniciarSesion.setInicioSesionUsuario(doa.cambiarIdioma(IniciarSesion.getInicioSesionUsuario().getEmail(), idioma));*/
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(binding.spinner.getId() == adapterView.getId()){
            switch (i) {
                case 0:
                    idioma = "es";
                    break;
                case 1:
                    idioma = "it";
                    break;
                case 2:
                    idioma = "en";
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}