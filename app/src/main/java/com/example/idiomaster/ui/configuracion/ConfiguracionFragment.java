package com.example.idiomaster.ui.configuracion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.idiomaster.R;
import com.example.idiomaster.adaptadores.AdaptadorIdiomas;
import com.example.idiomaster.databinding.FragmentNotificationsBinding;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Usuario;
import com.example.idiomaster.repositorio.FirebasesImple;
import com.example.idiomaster.utils.InternetUtil;


public class ConfiguracionFragment extends Fragment implements Spinner.OnItemSelectedListener{

    private FragmentNotificationsBinding binding;
    private String[] idiomas= {"Español", "Italiano", "Ingles"};
    private String idioma;
    private FirebasesImple doa;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Spinner spinner = root.findViewById(R.id.spinner);
        AdaptadorIdiomas adaptadorIdiomas = new AdaptadorIdiomas(requireContext(), R.layout.item_idioma, idiomas);
        spinner.setAdapter(adaptadorIdiomas);
        spinner.setOnItemSelectedListener(this);
        doa = new FirebasesImple();
        binding.correoElectronico.setText(IniciarSesion.getInicioSesionUsuario().getEmail());
        binding.guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = IniciarSesion.getInicioSesionUsuario();
                doa.actualizarProgresoFirebase(usuario.getEmail(), usuario.getIdioma(), usuario.getProgresoMundo(), usuario.getProgresoNivel());
                doa.cambiarIdiomaFirebase(usuario.getEmail(), idioma);
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
    public void onResume() {
        InternetUtil.isOnline(new InternetUtil.OnOnlineCheckListener() {
            @Override
            public void onResult(boolean isOnline) {
                if (!isOnline) {
                    requireActivity().runOnUiThread(new Runnable() {
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
        new AlertDialog.Builder(requireContext())
                .setTitle("Sin conexión a Internet")
                .setMessage("Necesitas conectarte a Internet para usar esta aplicación.")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requireActivity().finishAffinity();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}