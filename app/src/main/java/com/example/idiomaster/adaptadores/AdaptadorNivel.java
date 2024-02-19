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
import com.example.idiomaster.modelo.Nivel;

import java.util.List;

public class AdaptadorNivel extends RecyclerView.Adapter<AdaptadorNivel.ViewHolderNivel> {
    private List<Nivel> nivels;
    private Context context;
    private listener listener;
    public interface listener{
        void onClickCardView(int posicion);
    }

    public AdaptadorNivel(List<Nivel> nivels, AdaptadorNivel.listener listener) {
        this.nivels = nivels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderNivel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_nivel, parent, false);
        return new ViewHolderNivel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNivel holder, int position) {
        Nivel nivel = nivels.get(position);
        holder.nombreNivel.setText(nivel.getNombre());
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
        return nivels.size();
    }

    public class ViewHolderNivel extends RecyclerView.ViewHolder {
        private TextView nombreNivel;
        private CardView cardView;
        public ViewHolderNivel(@NonNull View itemView) {
            super(itemView);
            nombreNivel = itemView.findViewById(R.id.cv_txt_NombreNivel);
            cardView = itemView.findViewById(R.id.cardViewNivel);
        }
    }
}