package com.example.pedro.tesisalpha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Pedido_Activity extends AppCompatActivity {
    public final static String EXTRA_PLATILLO="platillo";
    public final static String EXTRA_CANTIDAD="cantidad";
    String[] separated;
    Cookie sessionInfo;
    boolean reload=false, busy=true, sw=true;
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    String SENDER_ID = "373000797222";
    static final String TAG = "GCMDemo";
    String idusuario="";
    private String regid;
    private GoogleCloudMessaging gcm;
    private ImageView btnok;
    private Context context=this;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    JSONArray productos;
    ArrayList<pedidos> datos;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private RecyclerView recView;
    String message,plato,cantidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarcocina);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        SharedPreferences prefs =
                getSharedPreferences("usuario", Context.MODE_PRIVATE);
        //List<Cookie> cookies = null;
        String cookienombre = prefs.getString("cookie_nombre", "") ;
        String cookievalor = prefs.getString("cookie_valor", "") ;
        String cookiedominio = prefs.getString("cookie_dominio", "") ;
        BasicClientCookie newCookie = new BasicClientCookie(cookienombre,cookievalor);
        newCookie.setDomain(cookiedominio);
        //cookies.
        //cookies= prefs.getString("cookie", "");

        //httpcookies objcookie = (httpcookies)i.getSerializableExtra("cookie");

        httpclient.getCookieStore().addCookie(newCookie);
        productos p = new productos();
        p.execute();
        LinearLayout l = (LinearLayout) findViewById(R.id.ocultococina);
        l.setVisibility(View.VISIBLE);

    }
    public void openact() {
        Toast toast = Toast.makeText(getApplicationContext(),"recargando" , Toast.LENGTH_SHORT);
        toast.show();
/*        httpcookies h = new httpcookies(sessionInfo);
        Intent intent = new Intent(this, Pedido_Activity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("cookie",h);
        startActivity(intent);*/
        productos p = new productos();
        p.execute();
    }
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mesas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class productos extends AsyncTask<String,Integer,Boolean> {
        String txt="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        public void onPreExecute(){
            busy=true;
        }
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.get("http://45.55.227.224/api/v1/order/espera",httpclient);
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
            busy=false;
            LinearLayout l = (LinearLayout) findViewById(R.id.ocultococina);
            l.setVisibility(View.INVISIBLE);
        }


    }

    void cargado() {
        //TextView txttitulo= (TextView) findViewById(R.id.txt1);

        datos = new ArrayList<pedidos>();
        for(int i=0; i<productos.length(); i++){
            try {
                JSONObject jsonObject = productos.getJSONObject(i);
                datos.add(new pedidos(jsonObject.getString("idOrder"),jsonObject.getString("nombrePlato"),jsonObject.getInt("mesa"),jsonObject.getString("mesonero"),"Pendiente",false));
// "Descripcion " + jsonObject.getString("description")
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recView = (RecyclerView) findViewById(R.id.RecView);
        //recView.setHasFixedSize(true);

        final AdaptadorPedido adaptador = new AdaptadorPedido(datos);




/*                adaptador.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        r.cancel(true);
                        *//*
                        i.refreshDrawableState();*//*

                            Log.i("DemoRecView", "Pulsado el elemento " + (recView.getChildPosition(v) + 1));
                            int posi = recView.getChildAdapterPosition(v);
                            final String n;

                            n = datos.get(posi).getId();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Pedido_Activity.this);
                        TextView t;
                        t = (TextView) v.findViewById(R.id.Lblnombre);
                        final ImageView image = (ImageView) v.findViewById(Integer.parseInt(n));
                        image.setImageResource(R.drawable.ic_social_notifications_on);


                            builder1.setMessage("Marcar como listo pedido: "+t.getText());
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("Si",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Log.i("DemoRecView", "IdOrder " + n);


                                            image.refreshDrawableState();
                                            Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.rotar2);
                                            image.startAnimation(logoMoveAnimation);
                                            Agregar a = new Agregar();
                                            a.execute(message, n);

                                        }
                                    });
                            builder1.setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            image.setImageResource(R.drawable.ic_social_notifications_on_gray);
                                            busy=false;
                                            r = new Recargar();
                                            r.execute();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        *//*Log.i("DemoRecView", "Pulsado el elemento " + (recView.getChildPosition(v) + 1));
                        //openact(recView.getChildPosition(v));
                        *//*
                    }
                });*/

        recView.setAdapter(adaptador);

        recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recView.setLayoutManager(new GridLayoutManager(this,2));

        /* recView.addItemDecoration(
                 new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
*/
        recView.setItemAnimator(new DefaultItemAnimator());
        btnok = (ImageView)findViewById(R.id.button_listo);
        btnok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String CurrentString = "Fruit: they taste good";

   /*             btnok.refreshDrawableState();
                Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.rotar2);
                btnok.startAnimation(logoMoveAnimation);*/
                String data = "";
                ArrayList<pedidos> stlist = ((AdaptadorPedido) adaptador)
                        .getPedidostist();
                for (int i = 0; i < stlist.size(); i++) {
                    pedidos seleccion = stlist.get(i);
                    if (seleccion.isSelected() == true) {
                        String a =seleccion.getNombre();
                        String b =seleccion.getId();
                        if ((stlist.size()-1)==i){
                            data=data+b;
                        }else {
                            data=data+b+":";
                        }


                        //data = data + "\n" + seleccion.getNombre().toString() + " " + seleccion.getCantidad();
                    }
                }
                //Toast.makeText(Pedido_Activity.this, data, Toast.LENGTH_LONG).show();

                separated = data.split(":");
/*                for (int i=0;i<separated.length;i++){
                    Toast.makeText(Pedido_Activity.this, separated.length+"", Toast.LENGTH_LONG).show();
                }*/
                Agregar a = new Agregar();
                a.execute(message, "");
            }
        });
    }
    private void tareaLarga()
    {
        try {
            Thread.sleep(5000);
            reload=true;
        } catch(InterruptedException e) {}
    }
    public class Agregar extends AsyncTask<String,Integer,Boolean> {
        String txt2="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        public void onPreExecute(){
            busy=true;
        }
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();

            for (int i = 0; i < separated.length; i++) {
                params[1]=separated[i];
                txt2 = handler.poststatus("http://45.55.227.224/api/v1/order/changeReady", httpclient, params);
            }
            sessionInfo=handler.sessionInfo;
            //tipousuario= params[1];
            try {
                respJSON = new JSONObject(txt2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //tipousuario=txt2;
            return handler.sw;

        }
        public void onPostExecute(Boolean resul) {
            /*busy=false;
            Toast toast = Toast.makeText(getApplicationContext(),"busy="+busy+ " reeload = "+reload , Toast.LENGTH_SHORT);
            toast.show();*/
            openact();
        }


    }

}
