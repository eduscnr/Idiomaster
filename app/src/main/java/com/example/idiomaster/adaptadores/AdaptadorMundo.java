package com.example.idiomaster.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.idiomaster.R;
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
        holder.nombreMundo.setText(mundo.getNombre());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicion = holder.getAdapterPosition();
                listener.onClickCardView(posicion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mundos.size();
    }

    public class ViewHolderMundo extends RecyclerView.ViewHolder {
        private TextView nombreMundo;
        private CardView cardView;
        public ViewHolderMundo(@NonNull View itemView) {
            super(itemView);
            nombreMundo = itemView.findViewById(R.id.cv_txt_NombreMundo);
            cardView = itemView.findViewById(R.id.cardViewMundo);
        }
    }
}