package com.example.idiomaster.adaptadores;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.idiomaster.R;
import com.example.idiomaster.modelo.Cuento;

import java.util.List;

public class AdaptadorCuentos extends RecyclerView.Adapter<AdaptadorCuentos.CuentoViewHolder> {

    private List<Cuento> listaCuentos;
    private Context context; // Necesario para iniciar la nueva actividad
    private AdaptadorCuentos.listener listener;

    public interface listener{
        void onClickCardView(int posicion);
    }

    public AdaptadorCuentos(List<Cuento> listaCuentos, AdaptadorCuentos.listener listener) {
        this.listaCuentos = listaCuentos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CuentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_storie, parent, false);
        return new AdaptadorCuentos.CuentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentoViewHolder holder, int position) {
        Cuento cuento = listaCuentos.get(position);
        holder.tituloTextView.setText(cuento.getTitulo());
        holder.autorTextView.setText(cuento.getAutor());

        // Agrega el clic del CardView para mostrar la historia completa
        holder.cardView.setOnClickListener(e->{
            int posicion = holder.getAdapterPosition();
            listener.onClickCardView(posicion);
        });
    }

    @Override
    public int getItemCount() {
        return listaCuentos.size();
    }

    public static class CuentoViewHolder extends RecyclerView.ViewHolder {

        private TextView tituloTextView;
        private TextView autorTextView;
        private CardView cardView;
        private ConstraintLayout constraintLayout;

        public CuentoViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.txt_tituloCuento);
            autorTextView = itemView.findViewById(R.id.txtAutorCuento);
            cardView = itemView.findViewById(R.id.cardViewCuento);
            constraintLayout = itemView.findViewById(R.id.constrainCuento);
        }
    }
}
