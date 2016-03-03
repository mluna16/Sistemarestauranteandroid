package com.example.pedro.tesisalpha;

/**
 * Created by Pedro on 22/03/2015.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorMenu
        extends RecyclerView.Adapter<AdaptadorMenu.MenuViewHolder>
        implements View.OnClickListener {

    private View.OnClickListener listener;
    private ArrayList<Menues> datos;

    public static class MenuViewHolder
            extends RecyclerView.ViewHolder {
        public ViewGroup linearLayoutDetails;
        public ImageView imageViewExpand;
        public static final int DURATION = 250;
        public ImageView img;
        public LinearLayout linearLayoutDetalles;
        public MyCustomEditTextListener myCustomEditTextListener;
        public TextView txtnom;
        public EditText txtcant;
        public TextView txtpre;
        public TextView txtlim;
        public CheckBox chkSelected;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;
        public MenuViewHolder(View itemView,MyCustomEditTextListener m) {
            super(itemView);
/*            linearLayoutDetails = (ViewGroup) itemView.findViewById(R.id.linearLayoutDetails);
            linearLayoutDetalles = (LinearLayout) itemView.findViewById(R.id.linearLayoutDetalles);
            imageViewExpand = (ImageView) itemView.findViewById(R.id.imageViewExpand);*/

            //    txtimg = (ImageButton)itemView.findViewById(R.id.imageButton);
            txtnom = (TextView) itemView.findViewById(R.id.Lblnom);
            txtcant = (EditText) itemView.findViewById(R.id.Lblcant);
            txtpre = (TextView) itemView.findViewById(R.id.Lblpre);
            txtlim = (TextView) itemView.findViewById(R.id.Lbllimite);
            //img = (ImageView) itemView.findViewById(R.id.imagecarga);
            myCustomEditTextListener = m;
            txtcant.addTextChangedListener(m);
            //clr = (CardView) itemView.findViewById(R.id.card1);
            //findViewById(R.id.card1);

            CardView cardView = (CardView) itemView.findViewById(R.id.card1);

            chkSelected = (CheckBox) itemView
                    .findViewById(R.id.chkSelected);

        }

        public void toggleDetails(ViewGroup view,Boolean exp) {
            if (exp) {
                ExpandAndCollapseViewUtil.expand(view, DURATION);
                imageViewExpand.setImageResource(R.mipmap.more);
                rotate(-180.0f);
            } else {
                ExpandAndCollapseViewUtil.collapse(view, DURATION);
                imageViewExpand.setImageResource(R.mipmap.less);
                rotate(180.0f);
            }
        }

        private void rotate(float angle) {
            Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setFillAfter(true);
            animation.setDuration(DURATION);
            imageViewExpand.startAnimation(animation);
        }

        public void bindMenu(Menues t) {
           /* StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //imagen carga


            try {
                URL imageUrl = null;
                HttpURLConnection conn = null;
                    imageUrl = new URL("http://45.55.227.224/"+t.getImagen());

                    conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

                    Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
                    img.setImageBitmap(imagen);



                } catch (IOException e) {

                    e.printStackTrace();

                }*/
            //fin imagen carga


            txtnom.setText(t.getNombre());
//            txtcant.setText(t.getCantidad());
            txtpre.setText(t.getPrecio().toString());
            txtlim.setText(t.getLimite().toString());


        }
    }

    public AdaptadorMenu(ArrayList<Menues> datos) {
        this.datos = datos;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listitem_menu, viewGroup, false);

        itemView.setOnClickListener(this);


        //android:background="?android:attr/selectableItemBackground"

        MenuViewHolder tvh = new MenuViewHolder(itemView, new MyCustomEditTextListener());

        return tvh;
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder viewHolder, final int pos) {
        Menues item = datos.get(pos);

        viewHolder.bindMenu(item);
        viewHolder.txtnom.setText(datos.get(pos).getNombre());
//            txtcant.setText(t.getCantidad());
        viewHolder.txtpre.setText(datos.get(pos).getPrecio().toString());
        viewHolder.txtlim.setText(datos.get(pos).getLimite().toString());
        viewHolder.chkSelected.setChecked(datos.get(pos).isSelected());
        //viewHolder.txtcant.setId(pos);

        viewHolder.chkSelected.setTag(datos.get(pos));
        viewHolder.txtcant.setTag(datos.get(pos));

        viewHolder.myCustomEditTextListener.updatePosition(pos,viewHolder.txtcant);
        if (!(datos.get(pos).getCantidad() ==1)){
        viewHolder.txtcant.setText(datos.get(pos).getCantidad() + "");
        }else {
            viewHolder.txtcant.setText("");
        }

/*        final boolean exp=datos.get(pos).isSelected1();
        viewHolder.toggleDetails(viewHolder.linearLayoutDetalles, exp);
        viewHolder.linearLayoutDetalles.setTag(datos.get(pos));
        viewHolder.linearLayoutDetalles.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LinearLayout cb = (LinearLayout) v;
                Menues contact = (Menues) cb.getTag();
                viewHolder.toggleDetails(cb,!contact.isSelected1() );
                datos.get(pos).setSelected1(!contact.isSelected1());
            }
        });*/

/*        viewHolder.txtcant.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                final EditText ct = (EditText) view;
                return false;
            }
        });*/
/*        viewHolder.txtcant.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                final EditText ct = (EditText) v;
                final Menues contacto = (Menues) ct.getTag();
                TextWatcher watcher = new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        if (!ct.getText().toString().equals("")) {
                            if (Integer.parseInt(ct.getText().toString()) <= datos.get(pos).limite) {
                                contacto.setCantidad(Integer.parseInt(ct.getText().toString()));

                            } else {
                                if (Integer.parseInt(ct.getText().toString()) <= 0) {
                                    contacto.setCantidad(Integer.parseInt(ct.getText().toString()));
                                    ct.setText("1");
                                } else {
                                    ct.setText(datos.get(pos).limite.toString());
                                }
                                contacto.setCantidad(datos.get(pos).limite);

                            }
                            datos.get(pos).setCantidad(Integer.parseInt(ct.getText().toString()));

                        }

                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //Do something or nothing.
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Do something or nothing
                    }
                };

                ct.addTextChangedListener(watcher);
            }
        });*/

        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Menues contact = (Menues) cb.getTag();
                if (!(Integer.parseInt(datos.get(pos).cantidad.toString()) > 0)) {

                    datos.get(pos).setCantidad(1);


                }
                contact.setSelected(cb.isChecked());
                datos.get(pos).setSelected(cb.isChecked());

                Toast.makeText(
                        v.getContext(),
                        "Clicked on Checkbox: " + cb.getText() + " is "
                                + cb.isChecked(), Toast.LENGTH_LONG).show();
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
        if (listener != null)
            listener.onClick(view);
    }

    // method to access in activity after updating selection
    public ArrayList<Menues> getMenuestist() {
        return datos;
    }
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;
        EditText e;


        public void updatePosition(int position,EditText e) {
            this.position = position;
            this.e = (EditText) e;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            if (!charSequence.toString().equals("")) {
                if ((Integer.parseInt(charSequence.toString()) > 0)&&(Integer.parseInt(charSequence.toString()) <= datos.get(position).limite)) {
                    datos.get(position).setCantidad(Integer.parseInt(charSequence.toString()));
                } else {
                    if (Integer.parseInt(charSequence.toString()) <= 0) {
                        datos.get(position).setCantidad(1);
                        e.setText("");
                    } else {
                        datos.get(position).setCantidad(datos.get(position).limite);
                        e.setText(datos.get(position).limite.toString());
                    }

                }
                //datos.get(pos).setCantidad(Integer.parseInt(ct.getText().toString()));

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
