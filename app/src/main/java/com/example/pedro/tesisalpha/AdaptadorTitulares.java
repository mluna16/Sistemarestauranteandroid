package com.example.pedro.tesisalpha;

/**
 * Created by Pedro on 22/03/2015.
 */
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorTitulares
        extends RecyclerView.Adapter<AdaptadorTitulares.TitularesViewHolder>
        implements View.OnClickListener {

    private View.OnClickListener listener;
    private ArrayList<Titular> datos;

    public static class TitularesViewHolder
            extends RecyclerView.ViewHolder {

        private TextView txtTitulo;
        private TextView txtSubtitulo;
        private RecyclerView clr;
        public TitularesViewHolder(View itemView) {
            super(itemView);

            txtTitulo = (TextView)itemView.findViewById(R.id.LblTitulo1);
            txtSubtitulo = (TextView)itemView.findViewById(R.id.LblSubTitulo);
            //clr = (CardView) itemView.findViewById(R.id.card1);
                    //findViewById(R.id.card1);

        }
        CardView cardView= (CardView) itemView.findViewById(R.id.card1);

        public void bindTitular(Titular t) {
            txtTitulo.setText(t.getTitulo());
            txtTitulo.setTextColor(Color.parseColor("#ffffffff"));
            txtSubtitulo.setTextColor(Color.parseColor("#ffffffff"));

            if (t.ocupado) {
                txtSubtitulo.setText("ocupado");
                cardView.setCardBackgroundColor(Color.parseColor("#f44336"));

            } else {
                txtSubtitulo.setText("desocupado");
                cardView.setCardBackgroundColor(Color.parseColor("#4caf50"));
            }
            if (t.getSubtitulo().equals("1")){
                txtSubtitulo.setText("Facturando");
            }else {

            }
        }
    }

    public AdaptadorTitulares(ArrayList<Titular> datos) {
        this.datos = datos;
    }

    @Override
    public TitularesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listitem_titular, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        TitularesViewHolder tvh = new TitularesViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(TitularesViewHolder viewHolder, int pos) {
        Titular item = datos.get(pos);

        viewHolder.bindTitular(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }
}