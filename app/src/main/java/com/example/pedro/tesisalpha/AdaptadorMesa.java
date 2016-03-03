package com.example.pedro.tesisalpha;

/**
 * Created by Pedro on 22/03/2015.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorMesa
        extends RecyclerView.Adapter<AdaptadorMesa.MesaViewHolder>
        implements View.OnClickListener {

    private View.OnClickListener listener;
    private ArrayList<Mesas> datos;
    public Context mContext;
    public class MesaViewHolder
            extends RecyclerView.ViewHolder {
        Toolbar toolbarCard;
        private ImageButton txtimg;
        private TextView txtnommesa;
    //    private TextView txtdes;
        private TextView txtpremesa;
        private TextView txtsta;
        private RecyclerView clr;

        public MesaViewHolder(View itemView) {
            super(itemView);


        //    txtimg = (ImageButton)itemView.findViewById(R.id.imageButton);
            txtnommesa = (TextView)itemView.findViewById(R.id.Lblnommesa);
          //  txtdes = (TextView)itemView.findViewById(R.id.Lbldes);
            txtpremesa = (TextView)itemView.findViewById(R.id.Lblpremesa);
            txtsta = (TextView)itemView.findViewById(R.id.Lblstatus);
            toolbarCard = (Toolbar) itemView.findViewById(R.id.toolbarCard);
            //clr = (CardView) itemView.findViewById(R.id.card1);
                    //findViewById(R.id.card1);
            /*            toolbarCard.setTitle("Nombre");
            toolbarCard.setSubtitle(t.getNombre());*/

            toolbarCard.inflateMenu(R.menu.menu_card);


        }
        CardView cardView= (CardView) itemView.findViewById(R.id.card1);

        public void bindMesa(final Mesas t) {
           /* String imageUrl = null;
            HttpURLConnection conn = null;

            try {
                imageUrl = "http://www.matrallune.com/images/imagen_corporativa.jpg";
                InputStream in = new java.net.URL(imageUrl).openStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2
                Bitmap imagen = BitmapFactory.decodeStream(in, new Rect(0, 0, 0, 0), options);
                txtimg.setImageBitmap(imagen);
            } catch (IOException e) {

                e.printStackTrace();

            }*/


            txtnommesa.setText(t.getNombre());
           // txtdes.setText(t.getDescripcion());
            txtpremesa.setText(t.getPrecio().toString());
            String color;
            if (t.getStatus().toString().equals("listo")){
                color="#4caf50";
            }else {
                color="#f44336";
            }
            txtsta.setTextColor(Color.parseColor(color));
            txtsta.setText(t.getStatus().toString());
        }
    }

    public AdaptadorMesa(ArrayList<Mesas> datos,Context ctx) {

        this.datos = datos;
        mContext = ctx;
    }

    @Override
    public MesaViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listitem_mesa, viewGroup, false);

        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        MesaViewHolder tvh = new MesaViewHolder(itemView);

        return tvh;
    }
    public ArrayList<Mesas> getMesastist() {
        return datos;
    }

    @Override
    public void onBindViewHolder(MesaViewHolder viewHolder, int pos) {
        final Mesas item = datos.get(pos);

        viewHolder.bindMesa(item);
        viewHolder.toolbarCard.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem iten) {


                switch (iten.getItemId()) {

                    case R.id.action_option1:
//editar
                        Intent intent = new Intent();
                        intent.setAction("editar");
                        intent.putExtra("nombre", item.getNombre());
                        AdaptadorMesa.this.mContext.sendBroadcast(intent);
                        break;
                    case R.id.action_option2:
//eliminar
                        Intent intent1 = new Intent();
                        intent1.setAction("eliminar");
                        intent1.putExtra("idorder",item.getIdorder());
                        AdaptadorMesa.this.mContext.sendBroadcast(intent1);
                        break;
                    case R.id.action_option3:
//devolver
                        Intent intent2 = new Intent();
                        intent2.setAction("devolver");
                        intent2.putExtra("nombre", item.getNombre());
                        intent2.putExtra("idorder", item.getIdorder());
                        intent2.putExtra("idproduct", item.getId());
                        AdaptadorMesa.this.mContext.sendBroadcast(intent2);
                        break;
                }
                return true;
            }
        });
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