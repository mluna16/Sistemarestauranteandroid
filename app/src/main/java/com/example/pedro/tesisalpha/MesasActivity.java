package com.example.pedro.tesisalpha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MesasActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    int nmesas=1;
    Cookie sessionInfo;
    JSONArray mesas;
    private RecyclerView recView;
    DefaultHttpClient httpclient = new DefaultHttpClient();
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
    private Context context;
    private ImageView imagen;
    private Animation rotacion;
    boolean reload=false,busy=false,sw=true;
    private ArrayList<Titular> datos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarmain);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        httpcookies objcookie = (httpcookies)i.getSerializableExtra("cookie");
                BasicClientCookie newCookie = new BasicClientCookie(objcookie.getName(),objcookie.getValue());
                newCookie.setDomain(objcookie.getDomain());
                httpclient.getCookieStore().addCookie(newCookie);
        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(MesasActivity.this);
      //  Log.d(TAG, "Registro GCM"+gcm);
        idusuario=i.getStringExtra("idusuario");
        //Obtenemos el Registration ID guardado
        regid = getRegistrationId(context);
        //Si no disponemos de Registration ID comenzamos el registro
        if (regid.equals("")) {
            TareaRegistroGCM tareagcm = new TareaRegistroGCM();
            tareagcm.execute();
        }
        LinearLayout l = (LinearLayout) findViewById(R.id.ocultomain);
        l.setVisibility(View.VISIBLE);
        Mesas tarea= new Mesas();
        tarea.execute();
    }
    private String getRegistrationId(Context context)
    {
        SharedPreferences prefs = getSharedPreferences(
                MesasActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        Log.d(TAG, "R"+prefs.getString(PROPERTY_REG_ID, ""));
        if (registrationId.length() == 0)
        {
            Log.d(TAG,registrationId+ "Registro GCM no encontrado.");
            return "";
        }

        String registeredUser =
                prefs.getString(PROPERTY_USER, "user");

        int registeredVersion =
                prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

        long expirationTime =
                prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));

        Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
                ", version=" + registeredVersion +
                ", expira=" + expirationDate + ")");

        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion)
        {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return "";
        }
        else if (System.currentTimeMillis() > expirationTime)
        {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        }
        else if (!idusuario.equals(registeredUser))
        {
            Log.d(TAG, "Nuevo nombre de usuario.");
            return "";
        }

        return registrationId;
    }

    private static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }
    public void cargado(int nmes){

        datos = new ArrayList<Titular>();
        Boolean sw=false;




        for(int i=0; i<nmesas; i++){
            try {
            JSONObject jsonObject = mesas.getJSONObject(i);
            String var3 = jsonObject.getString("State");

            if (var3.equals("ocupado")){

                sw=true;}
            else {
                sw=false;}
                
            datos.add(new Titular(jsonObject.getString("NumberTable"), "",sw));
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
                TextView nomb=(TextView) v.findViewById(R.id.LblTitulo1);

                openact(nomb.getText().toString());

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
        intent.putExtra("cookie",h);
        startActivity(intent);

    }
    public void openact2(String message) {
        httpcookies h = new httpcookies(sessionInfo);
        Intent i = getIntent();
        Intent intent = new Intent(this, Sesion.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("cookie",h);
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
            sesion tarea= new sesion();
            tarea.execute();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public class sesion extends AsyncTask<String,Integer,Boolean> {
        String txt="Cerrando Sesión";
        JSONObject respJSON;
        protected void onPreExecute() {

            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomain);
            l.setVisibility(View.VISIBLE);
        }

        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.get("http://45.55.227.224/api/v1/logout", httpclient);
            sessionInfo=handler.sessionInfo;
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
    public class Mesas extends AsyncTask<String,Integer,Boolean> {
        String txt="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        protected void onPreExecute() {
        }
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.get("http://45.55.227.224/api/v1/table",httpclient);
            sessionInfo=handler.sessionInfo;
            try {

                respJSON = new JSONObject(txt);
                JSONObject data = respJSON.getJSONObject("data");
                nmesas=data.getInt("TotalMesas");
                mesas = data.getJSONArray("Mesas");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return handler.sw;

        }
        public void onPostExecute(Boolean resul) {
            cargado(nmesas);
            if (resul){
                Recargar r = new Recargar();
                r.execute();
            }
            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomain);
            l.setVisibility(View.INVISIBLE);
            recView = (RecyclerView) findViewById(R.id.RecView);
            recView.setVisibility(View.VISIBLE);
        }


    }
    public class Recargar extends AsyncTask<String,Integer,Boolean> {
        public Boolean doInBackground(String... params) {
            if(sw){
                do {
                    tareaLarga();

                }while (!reload);}
            return reload;
        }
        public void onPostExecute(Boolean resul) {
            if (reload){
                recarga();
            }

        }
    }
    @Override
    public void onResume(){
        super.onResume();
        sw=true;
        Recargar r = new Recargar();
        r.execute();
    }
    @Override
    public void onPause(){
        super.onPause();
        sw=false;
    }
    private void recarga() {

        Toast toast = Toast.makeText(getApplicationContext(),"recargando" , Toast.LENGTH_SHORT);
        toast.show();
        Mesas p = new Mesas();
        p.execute();
        reload=false;
        Recargar r = new Recargar();
        r.execute();
    }

    private void tareaLarga() {
        try {
            Thread.sleep(5000);
            reload=true;
        } catch(InterruptedException e) {}
    }
    private class TareaRegistroGCM extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";

            try
            {
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);

                Log.d(TAG, "Registrado en GCM: registration_id=" + regid);

                //Nos registramos en nuestro servidor
                boolean registrado = registroServidor(idusuario, regid);
                Log.d(TAG,"idusuario"+ idusuario+"\n"+"regid"+regid);
                //Guardamos los datos del registro
                if(registrado)
                {
                    Log.d(TAG, "Guardamos los datos del registro");
                    setRegistrationId(context, idusuario, regid);

                    msg="done"+"\n";
                }
            }
            catch (IOException ex)
            {
                msg="error"+"\n";
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
            //return regid;
        }
        public void onPostExecute(String resul ){
            Log.d(TAG, resul);
            //txtdebug.setText(resul);
        }
    }

    private void setRegistrationId(Context context, String user, String regId)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER, user);
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putLong(PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + EXPIRATION_TIME_MS);
        editor.commit();
    }

    private boolean registroServidor(String usuario, String regId)
    {
        String txt ="";
        boolean reg = false;
/*
        handler = new httphandler();
        //httphandler handler1 = new httphandler();
        txt = handler.post("http://45.55.227.224/api/v1/login",httpclient,usuario,regId);
        sessionInfo=handler.sessionInfo;*/
/*        try {
            respJSON = new JSONObject(txt);

            JSONObject data = respJSON.getJSONObject("data");
            JSONObject userdata = data.getJSONObject("userData");
            tipousuario=userdata.getString("id");*/


         //   if(tipousuario!=null){
                //handler = new httphandler();
                httphandler handler1 = new httphandler();
                txt = handler1.postgcm("http://45.55.227.224/api/v1/user/code", httpclient, idusuario, regId);
                sessionInfo=handler1.sessionInfo;
                //respJSON = new JSONObject(txt);

                //JSONObject data1 = respJSON.getJSONObject(txt);
                if (txt.equals("1")){
                    reg = true;
                    Log.d(TAG, "Registrado en mi servidor.");
                }else {
                    reg = false;
                    Log.d(TAG, "Error registro en mi servidor: ");
                }


            //}

/*
        }
        catch (Exception e)
        {
            //reg = false;
            Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
        }*/



        return reg;
    }
/*    private class TareaRegistroGCM extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";

            try
            {
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);

                Log.d(TAG, "Registrado en GCM: registration_id=" + regid);

                //Nos registramos en nuestro servidor
                boolean registrado = registroServidor(idusuario, regid);

                //Guardamos los datos del registro
                if(registrado)
                {
                    Log.d(TAG, "Guardamos los datos del registro");
                    setRegistrationId(context, idusuario, regid);
                    msg="done"+"\n";
                }
            }
            catch (IOException ex)
            {
                msg="error"+"\n";
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
            //return regid;
        }
        *//*public void onPostExecute(String resul ){
            txtdebug.setText(resul);
        }*//*
    }

    private void setRegistrationId(Context context, String user, String regId)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_USER, user);
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.putLong(PROPERTY_EXPIRATION_TIME,
                System.currentTimeMillis() + EXPIRATION_TIME_MS);

        editor.commit();
    }

    private boolean registroServidor(String usuario, String regId)
    {
        boolean reg = false;
        String txt;
        httphandler handler = new httphandler();
        txt = handler.postgcm("http://45.55.227.224/api/v1/user/code", httpclient, idusuario, regId);

                if (txt.equals("1")){
                    Log.d(TAG, "Registrado en mi servidor.");
                    reg = true;
                }else {
                    reg = false;
                    Log.d(TAG, "Error registro en mi servidor: ");
                }
        return reg;
    }*/
}
