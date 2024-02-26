package com.example.idiomaster.adaptadores;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idiomaster.R;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.Mundo;

import java.util.List;

public class AdaptadorMundo extends RecyclerView.Adapter<AdaptadorMundo.ViewHolderMundo>{
    private List<Mundo> mundos;
    private Context context;
    private listener listener;
    public interface listener{
        void onClickCardView(int posicion);
    }
    public AdaptadorMundo(List<Mundo> mundos, AdaptadorMundo.listener listener) {
        this.mundos = mundos;
        this.listener = listener;
    }
    @NonNull
    @Override
    public AdaptadorMundo.ViewHolderMundo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_mundo, parent, false);
        return new AdaptadorMundo.ViewHolderMundo(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdaptadorMundo.ViewHolderMundo holder, int position) {
        Mundo mundo = mundos.get(position);
        boolean mundoDesbloqueado = IniciarSesion.getInicioSesionUsuario().getProgresoMundo() >= mundo.getId();
        float brillo = mundoDesbloqueado ? 1.0f : 0.5f;
        System.out.println("Me vuelvo a ejecutar :) " + mundoDesbloqueado);
        switch (mundo.getNombre()){
            case "Madrid":
                holder.constraintLayout.setBackgroundResource(R.drawable.madridanime);
                applyBrightness(holder.constraintLayout, brillo);
                break;
            case "Barcelona":
                holder.constraintLayout.setBackgroundResource(R.drawable.barcelonaanime);
                applyBrightness(holder.constraintLayout, brillo);
                break;
            case "London":
                holder.constraintLayout.setBackgroundResource(R.drawable.londresanime);
                applyBrightness(holder.constraintLayout,brillo);
                break;
            case "Dublin":
                holder.constraintLayout.setBackgroundResource(R.drawable.dublinanime);
                applyBrightness(holder.constraintLayout,brillo);
                break;
            case "Roma":
                holder.constraintLayout.setBackgroundResource(R.drawable.romaanime);
                applyBrightness(holder.constraintLayout,brillo);
                break;
            case "Napoli":
                holder.constraintLayout.setBackgroundResource(R.drawable.napolianime);
                applyBrightness(holder.constraintLayout,brillo);
                break;
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion = holder.getAdapterPosition();
                listener.onClickCardView(posicion);
            }
        });
    }
    private void applyBrightness(View view, float brillo) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                brillo,   0,   0,   0,   0,
                0, brillo,   0,   0,   0,
                0,   0, brillo,   0,   0,
                0,   0,   0,   1,   0
        });

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        view.getBackground().setColorFilter(filter);
    }

    @Override
    public int getItemCount() {
        return mundos.size();
    }

    public class ViewHolderMundo extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ConstraintLayout constraintLayout;
        public ViewHolderMundo(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewMundo);
            constraintLayout = itemView.findViewById(R.id.constrainMundo);
        }
    }
}