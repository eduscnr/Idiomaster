package com.example.idiomaster.dialogo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.idiomaster.R;
import com.example.idiomaster.ui.minijuegos.TraducePalabras;

public class FinalizarJuego extends DialogFragment {
    private Button salir;
    private Button continuar;
    IDialogoLisenerOnClick combiarAccion;
    public interface IDialogoLisenerOnClick{
        void onBotonDialogoClic();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_personalizado, null);
        builder.setView(view);
        AlertDialog dialogo = builder.create();
        dialogo.setCanceledOnTouchOutside(false);
        salir = view.findViewById(R.id.btnSalir);
        continuar = view.findViewById(R.id.btnContinuar);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                combiarAccion.onBotonDialogoClic();
            }
        });
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo.dismiss();
            }

        });
        return dialogo;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            combiarAccion = (IDialogoLisenerOnClick) context;
        }catch (ClassCastException ex){
            System.out.println(ex.toString());
        }
    }
}
