package com.example.pedro.tesisalpha;

/**
 * Created by Pedro on 22/03/2015.
 */
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorPedido
        extends RecyclerView.Adapter<AdaptadorPedido.pedidoViewHolder>
        implements View.OnClickListener {
    public boolean status=true;
    private View.OnClickListener listener;
    private ArrayList<pedidos> datos;


    public static class pedidoViewHolder
            extends RecyclerView.ViewHolder {


        private TextView txtnom;
        private TextView txtid;
        private TextView txtmeso;
        private TextView txtnum;
        private CheckBox stat;
        ImageView image;
        public pedidoViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.button_marcar_listo);

            txtnom = (TextView)itemView.findViewById(R.id.Lblnombre);
            //txtid = (TextView)itemView.findViewById(R.id.Lblid);
            txtnum = (TextView)itemView.findViewById(R.id.Lblmesa);
            //txtmeso = (TextView)itemView.findViewById(R.id.Lblmesonero);
            //txtlim = (TextView)itemView.findViewById(R.id.Lbllimite);
            //clr = (CardView) itemView.findViewById(R.id.card1);
                    //findViewById(R.id.card1);

        }
        CardView cardView= (CardView) itemView.findViewById(R.id.card1);

        public void bindpedido(pedidos t) {

            image.setImageResource(R.drawable.ic_social_notifications_on_gray);
            txtnom.setText(t.getNombre());
//            txtid.setText(t.getId());
            txtnum.setText(t.getNumeromesa().toString());
  //          txtmeso.setText(t.getMesonero());
image.setId(Integer.parseInt(t.getId()));
/*            image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    image.setImageResource(R.drawable.ic_social_notifications_on);
                }
            });*/
        }
    }

    public AdaptadorPedido(ArrayList<pedidos> datos) {
        this.datos = datos;
    }
    ImageView image;
    @Override
    public pedidoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listitem_pedido, viewGroup, false);

         itemView.setOnClickListener(this);

        //android:background="?android:attr/selectableItemBackground"

        pedidoViewHolder tvh = new pedidoViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(pedidoViewHolder viewHolder, int pos) {
        pedidos item = datos.get(pos);

        viewHolder.bindpedido(item);
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