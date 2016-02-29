package com.example.pedro.tesisalpha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
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
public class Mesas_Activity extends ActionBarActivity {
    httphandler handler;
    int nmesas = 1;
    Cookie sessionInfo;
    JSONArray mesas;
    private RecyclerView recView;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    JSONObject respJSON;
    String txt = "Iniciando Sesion", txt1 = "cargando mesas", tipousuario = "";
    public final static String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    String SENDER_ID = "373000797222";
    static final String TAG = "GCMDemo";
    private Context context;
    private String regid;
    private GoogleCloudMessaging gcm;
    private ImageView imagen;
    private Animation rotacion;
    boolean reload = false, busy = false, sw = true;
    private ArrayList<Titular> datos;
    String idusuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarmain);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        idusuario = i.getStringExtra("idusuario");
        httpcookies objcookie = (httpcookies) i.getSerializableExtra("cookie");
        BasicClientCookie newCookie = new BasicClientCookie(objcookie.getName(), objcookie.getValue());
        newCookie.setDomain(objcookie.getDomain());
        httpclient.getCookieStore().addCookie(newCookie);
        LinearLayout l = (LinearLayout) findViewById(R.id.ocultomain);
        l.setVisibility(View.VISIBLE);
        Mesas tarea = new Mesas();
        tarea.execute();
        IntentFilter filter = new IntentFilter();
        filter.addAction("actualizar");
        registerReceiver(mBroadcast, filter);
    }

    public void cargado(int nmes) {

        datos = new ArrayList<Titular>();
        Boolean sw = false;


        for (int i = 0; i < nmesas; i++) {
            try {
                JSONObject jsonObject = mesas.getJSONObject(i);
                String var3 = jsonObject.getString("State");

                if (var3.equals("ocupado")) {

                    sw = true;
                } else {
                    sw = false;
                }

                datos.add(new Titular(jsonObject.getString("NumberTable"), "", sw));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        final AdaptadorTitulares adaptador = new AdaptadorTitulares(datos);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + (recView.getChildPosition(v) + 1));
                //TextView nomb = (TextView) v.findViewById(R.id.LblTitulo1);
                int posi = recView.getChildAdapterPosition(v);
                final String n;
                n = datos.get(posi).getTitulo();
                Log.i(TAG, "n = " + n);
                SharedPreferences prefs =
                        getSharedPreferences("usuario", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("mesa", n);
                editor.commit();
                openact(n);

            }
        });

        recView.setAdapter(adaptador);

        //recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        // buscando los pixeles a partir de dips con la densidad
/*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
*/

/*        switch(metrics.densityDpi)
        {
            case DisplayMetrics.DENSITY_HIGH: //HDPI
                recView.setLayoutManager(new GridLayoutManager(this,4));
                break;
            case DisplayMetrics.DENSITY_MEDIUM: //MDPI*/
        recView.setLayoutManager(new GridLayoutManager(this, 3));
/*                break;

            case DisplayMetrics.DENSITY_LOW:  //LDPI
                recView.setLayoutManager(new GridLayoutManager(this,2));
                break;
        }*/


        // recView.addItemDecoration(
        //         new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        recView.setItemAnimator(new DefaultItemAnimator());
        //

    }

    public void openact(String message) {
        httpcookies h = new httpcookies(sessionInfo);
        Intent i = getIntent();
        Intent intent = new Intent(this, MesaPedidoActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("gcm", false);
        intent.putExtra("cookie", h);
        startActivity(intent);

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

            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomain);
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

            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomain);
            l.setVisibility(View.INVISIBLE);

        }


    }

    public class Mesas extends AsyncTask<String, Integer, Boolean> {
        String txt = "Cargando Mesas", tipousuario = "";
        JSONObject respJSON;

        protected void onPreExecute() {

        }

        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.get("http://45.55.227.224/api/v1/table", httpclient);
            sessionInfo = handler.sessionInfo;
            try {

                respJSON = new JSONObject(txt);
                JSONObject data = respJSON.getJSONObject("data");
                nmesas = data.getInt("TotalMesas");
                mesas = data.getJSONArray("Mesas");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return handler.sw;

        }

        public void onPostExecute(Boolean resul) {
            cargado(nmesas);
            if (resul) {

            }
            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomain);
            l.setVisibility(View.INVISIBLE);
            recView = (RecyclerView) findViewById(R.id.RecView);
            recView.setVisibility(View.VISIBLE);
        }


    }
    BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            if(intent.getAction().equals("actualizar")){
                String x=intent.getExtras().getString("mesa");
                Toast.makeText(getBaseContext(), x, Toast.LENGTH_LONG).show();

            }
        }
    };

}
