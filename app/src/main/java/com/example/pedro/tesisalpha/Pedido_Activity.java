package com.example.pedro.tesisalpha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private Parcelable recyclerViewState;
    AdaptadorPedido adaptador;
    boolean reload=false, busy=true, sw=true;
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    String SENDER_ID = "373000797222";
    static final String TAG = "GCMDemo";
    String data="";
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
        IntentFilter filter = new IntentFilter();
        filter.addAction("actualizar");
        registerReceiver(mBroadcast, filter);

    }
    public void openact() {
        /*Toast toast = Toast.makeText(getApplicationContext(),"recargando" , Toast.LENGTH_SHORT);
        toast.show();*/
/*        httpcookies h = new httpcookies(sessionInfo);
        Intent intent = new Intent(this, Pedido_Activity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("cookie",h);
        startActivity(intent);*/
        productos p = new productos();
        p.execute();
    }
    public void openact2(String message) {
        httpcookies h = new httpcookies(sessionInfo);
        Intent i = getIntent();
        Intent intent = new Intent(this, Sesion.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("cookie", h);
        SharedPreferences prefs =
                getSharedPreferences("usuario",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("sesion", false);
        editor.commit();
        startActivity(intent);

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
        if (id == R.id.action_cerrar) {
            sesion tarea = new sesion();
            tarea.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class sesion extends AsyncTask<String, Integer, Boolean> {
        String txt = "Cerrando Sesión";
        JSONObject respJSON;

        protected void onPreExecute() {

            LinearLayout l = (LinearLayout) findViewById(R.id.ocultococina);
            l.setVisibility(View.VISIBLE);
        }

        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.get("http://45.55.227.224/api/v1/logout", httpclient);
            sessionInfo = handler.sessionInfo;
            try {
                respJSON = new JSONObject(txt);
                JSONObject data = respJSON.getJSONObject("data");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return handler.sw;

        }

        public void onPostExecute(Boolean resul) {
            openact2("");

            LinearLayout l = (LinearLayout) findViewById(R.id.ocultococina);
            l.setVisibility(View.INVISIBLE);

        }


    }
    public class productos extends AsyncTask<String,Integer,Boolean> {
        String txt="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        public void onPreExecute(){
            //busy=true;
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
        llenar();
            recView = (RecyclerView) findViewById(R.id.RecView);
            //recView.setHasFixedSize(true);

            adaptador = new AdaptadorPedido(datos);

            recView.setAdapter(adaptador);

            recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            //recView.setLayoutManager(new GridLayoutManager(this,2));

/*         recView.addItemDecoration(
                 new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));*/

            recView.setItemAnimator(new DefaultItemAnimator());
            btnok = (ImageView)findViewById(R.id.button_listo);
            btnok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

   /*             btnok.refreshDrawableState();
                Animation logoMoveAnimation = AnimationUtils.loadAnimation(context, R.anim.rotar2);
                btnok.startAnimation(logoMoveAnimation);*/
                    seleccionados();
/*                    for (int i = 0; i < separated.length; i++) {
                        Log.i(TAG, "onClick "+separated[i]);
                        Toast.makeText(Pedido_Activity.this, separated.length + "", Toast.LENGTH_LONG).show();
                    }*/
                    Agregar a = new Agregar();
                    a.execute(message, "");
                }
            });

            //Toast.makeText(getBaseContext(),"recargar",Toast.LENGTH_LONG).show();
            //adaptador.notifyDataSetChanged();
        /*if(!busy) {
            recView.getLayoutManager().onRestoreInstanceState(recyclerViewState);//restore
        }*/
    }

    private void seleccionados() {
        data = "";
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
    }

    private void llenar() {
        int j=0;

        datos = new ArrayList<pedidos>();
        for(int i=0; i<productos.length(); i++){
            try {
                JSONObject jsonObject = productos.getJSONObject(i);

                datos.add(new pedidos(jsonObject.getString("idOrder"),
                        jsonObject.getString("nombrePlato"),
                        jsonObject.getInt("mesa"),
                        jsonObject.getString("mesonero"),
                        "Pendiente",
                        false));

// "Descripcion " + jsonObject.getString("description")
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
                //Log.i(TAG, separated[i]);
                params[1]=separated[i];
                Log.i("idorder", separated[i]);
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
    void remover(String nOrder){
        ArrayList<pedidos> stlist = ((AdaptadorPedido) adaptador)
                .getPedidostist();
        for (int i = 0; i < stlist.size(); i++) {
            pedidos seleccion = stlist.get(i);
            if (seleccion.getId().equals(nOrder)) {
                datos.remove(i);
                adaptador.notifyItemRemoved(i);
            }
        }
    }
    void agregar(String nOrder, String nMesa, String parc){
        boolean found=false;
        ArrayList<pedidos> stlist = ((AdaptadorPedido) adaptador)
                .getPedidostist();
        for (int i = 0; i < stlist.size(); i++) {
            pedidos seleccion = stlist.get(i);
            if (seleccion.getId().equals(nOrder)) {
                found=true;

            }
        }
        if (!found){

            Log.i(TAG, "prueba "+ parc);
            datos.add(new pedidos(nOrder,
                    parc,
                    Integer.parseInt(nMesa),
                    "",
                    "Pendiente",
                    false));
            //datos.add(1, new Titular("Nuevo titular", "Subtitulo nuevo titular"));
            adaptador.notifyItemInserted(datos.size());
        }

    }
    BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            if(intent.getAction().equals("actualizar")){
                String x=intent.getStringExtra("accion");
                if (x.equals("eliminar")||x.equals("devolver")||x.equals("remover")){
                    remover(intent.getStringExtra("idorder"));
                }else {
                    if(x.equals("agregar")){

                        String par[]=intent.getStringExtra("hobb").split(": ");
                        String parc=par[1];
                        agregar(intent.getStringExtra("idorder"),intent.getStringExtra("mesa"),parc);
                        //datos.add(1, new Titular("Nuevo titular", "Subtitulo nuevo titular"));
                        //adaptador.notifyItemInserted(1);
                    }else{
                        if(x.equals("editar")){

                        }
                    }
                }

            }
            /*seleccionados();
                recyclerViewState = recView.getLayoutManager().onSaveInstanceState();//save
                productos p = new productos();
                p.execute();
                Toast.makeText(getBaseContext(),intent.getExtras().getString("nombre"),Toast.LENGTH_LONG).show();*/
        }
    };
}
