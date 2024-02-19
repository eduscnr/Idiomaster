package com.example.idiomaster.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.idiomaster.R;

public class AdaptadorIdiomas extends ArrayAdapter<String> {
    private int[] banderas = {R.drawable.icons8_espa_a_48, R.drawable.icons8_bandera_italiana_48,
            R.drawable.icons8_gran_breta_a_48};
    private String[] idiomas= {"Español", "Italiano", "Ingles"};
    private Context mContext;
    public AdaptadorIdiomas(@NonNull Context context, int resource, String[] object) {
        super(context, resource, object);
        mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearFilaPersonalizada(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearFilaPersonalizada(position, convertView, parent);
    }

    public View crearFilaPersonalizada(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View filas = inflater.inflate(R.layout.item_idioma, parent, false);
        ImageView imageView = filas.findViewById(R.id.bandera);
        imageView.setImageResource(banderas[position]);
        TextView idioma = filas.findViewById(R.id.idioma);
        idioma.setText(idiomas[position]);
        return filas;
    }
}
