package com.example.pedro.tesisalpha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Menu_Activity extends ActionBarActivity {
    public final static String EXTRA_PLATILLO="platillo";
    public final static String EXTRA_CANTIDAD="cantidad";
    private ViewGroup linearLayoutDetails;
    private ImageView imageViewExpand;
    private static final int DURATION = 250;
    int max=0;
    Cookie sessionInfo;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    JSONArray productos;
    ArrayList<Menues> datos;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private RecyclerView recView;
    String message,plato,cantidad;
    private ImageView btnok;
    Boolean sw=false;
    String[][] genera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarmenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        message=i.getStringExtra(MesaPedidoActivity.EXTRA_MESSAGE);
        httpcookies objcookie = (httpcookies)i.getSerializableExtra("cookie");
        BasicClientCookie newCookie = new BasicClientCookie(objcookie.getName(),objcookie.getValue());
        newCookie.setDomain(objcookie.getDomain());
        httpclient.getCookieStore().addCookie(newCookie);
        productos p = new productos();
        p.execute();
        LinearLayout l = (LinearLayout) findViewById(R.id.ocultomenu);
        l.setVisibility(View.VISIBLE);

    }

    public void openact() {
        //Toast toast = Toast.makeText(getApplicationContext(),"" , Toast.LENGTH_SHORT);

        httpcookies h = new httpcookies(sessionInfo);
        Intent intent = new Intent(this, MesaPedidoActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("cookie",h);
        startActivity(intent);
    }
    public void toggleDetails(View view) {
        if (linearLayoutDetails.getVisibility() == View.GONE) {
            ExpandAndCollapseViewUtil.expand(linearLayoutDetails, DURATION);
            imageViewExpand.setImageResource(R.mipmap.more);
            rotate(-180.0f);
        } else {
            ExpandAndCollapseViewUtil.collapse(linearLayoutDetails, DURATION);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public class productos extends AsyncTask<String,Integer,Boolean> {
        String txt="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.get("http://45.55.227.224/api/v1/producto",httpclient);
            sessionInfo=handler.sessionInfo;
            try {
                //respJSON = new JSONObject(txt);
                productos= new JSONArray(txt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return handler.sw;

        }
        public void onPostExecute(Boolean resul) {
            cargado();
            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomenu);
            l.setVisibility(View.INVISIBLE);
        }


    }

    void cargado() {
        //TextView txttitulo= (TextView) findViewById(R.id.txt1);
        datos = new ArrayList<Menues>();
        for(int i=0; i<productos.length(); i++){
            try {
                JSONObject jsonObject = productos.getJSONObject(i);
                datos.add(new Menues(jsonObject.getString("id_product"),jsonObject.getString("image"),jsonObject.getString("name"),jsonObject.getString("description"),jsonObject.getDouble("cost"),jsonObject.getInt("inventory"), false,1));
// "Descripcion " + jsonObject.getString("description")
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recView = (RecyclerView) findViewById(R.id.RecView);
       // recView.setHasFixedSize(true);

        final AdaptadorMenu adaptador = new AdaptadorMenu(datos);


        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* linearLayoutDetails = (ViewGroup) v.findViewById(R.id.linearLayoutDetails);
                imageViewExpand = (ImageView) v.findViewById(R.id.imageViewExpand);*/
                Log.i("DemoRecView", "Pulsado el elemento " + (recView.getChildPosition(v) + 1));
                //openact(recView.getChildPosition(v));

          /*      String n;
                n=datos.get(posi).getId();*/
               /* Agregar a = new Agregar();
                a.execute(
                        message,
                        n
                );*/

            }
        });

        recView.setAdapter(adaptador);

        recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recView.setLayoutManager(new GridLayoutManager(this,2));

/*         recView.addItemDecoration(
                 new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));*/

        recView.setItemAnimator(new DefaultItemAnimator());
        btnok = (ImageView)findViewById(R.id.button_add_pedido);

        genera = new String[adaptador.getItemCount()][2];
        btnok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = "";
                ArrayList<Menues> stlist = ((AdaptadorMenu) adaptador)
                        .getMenuestist();
int j=0;
                for (int i = 0; i < stlist.size(); i++) {
                    Menues seleccion = stlist.get(i);
                    if (seleccion.isSelected() == true) {
                        genera[j][0]= seleccion.getCantidad().toString();
                        genera[j][1]= seleccion.getId();
                        Toast.makeText(Menu_Activity.this, "\n" + message+ " cant " + genera[j][0] + " id " + genera[j][1], Toast.LENGTH_LONG).show();
                        j++;
                        //data = data + "\n" + seleccion.getNombre().toString() + " " + seleccion.getCantidad();
						/*
						 * Toast.makeText( CardViewActivity.this, " " +
						 * singleStudent.getName() + " " +
						 * singleStudent.getEmailId() + " " +
						 * singleStudent.isSelected(),
						 * Toast.LENGTH_SHORT).show();
						 * 0,0 0,1
						 * 1,0 1,1
						 * 2,0 2,1
						 *
						 */

                    }

                }

                Agregar a = new Agregar();
                a.execute(
                        message,
                        "",
                        ""
                );

            }
        });
    }
    public class Agregar extends AsyncTask<String,Integer,Boolean> {
        String txt2="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            for (int i = 0; i < genera.length; i++) {
                try {
                    params[1]=genera[i][1];
                    params[2]=genera[i][0];
                    txt2 = handler.postpedido("http://45.55.227.224/api/v1/order/store", httpclient, params);
                    sessionInfo = handler.sessionInfo;
                    tipousuario = params[0];
                    respJSON = new JSONObject(txt2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return handler.sw;

        }
        public void onPostExecute(Boolean resul) {

            Toast toast = Toast.makeText(getApplicationContext(), txt2, Toast.LENGTH_SHORT);
            toast.show();
            openact();

        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
        /*    httpcookies h = new httpcookies(sessionInfo);
            Intent intent = new Intent(this, MesasActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            intent.putExtra("cookie",h);
            startActivity(intent);*/
            Menu_Activity.this.finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
