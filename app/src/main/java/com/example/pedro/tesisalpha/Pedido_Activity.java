package com.example.pedro.tesisalpha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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


public class Pedido_Activity extends AppCompatActivity {
    public final static String EXTRA_PLATILLO="platillo";
    public final static String EXTRA_CANTIDAD="cantidad";
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
    private Context context=this;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    JSONArray productos;
    ArrayList<pedidos> datos;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private RecyclerView recView;
    Recargar r = new Recargar();
    String message,plato,cantidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarcocina);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        httpcookies objcookie = (httpcookies)i.getSerializableExtra("cookie");
        BasicClientCookie newCookie = new BasicClientCookie(objcookie.getName(),objcookie.getValue());
        newCookie.setDomain(objcookie.getDomain());
        httpclient.getCookieStore().addCookie(newCookie);
        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(Pedido_Activity.this);
        idusuario=i.getStringExtra("idusuario");
        //Obtenemos el Registration ID guardado
        regid = getRegistrationId(context);
        //Si no disponemos de Registration ID comenzamos el registro
        if (regid.equals("")) {
            TareaRegistroGCM tareagcm = new TareaRegistroGCM();
            tareagcm.execute();
        }
        productos p = new productos();
        p.execute();
        Recargar r = new Recargar();
        r.execute();
        LinearLayout l = (LinearLayout) findViewById(R.id.ocultococina);
        l.setVisibility(View.VISIBLE);
    }
    private String getRegistrationId(Context context)
    {
        SharedPreferences prefs = getSharedPreferences(
                Pedido_Activity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.length() == 0)
        {
            Log.d(TAG, "Registro GCM no encontrado.");
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
        reload=false;
        r = new Recargar();
        r.execute();
    }
    public void onResume(){
        super.onResume();

        r.execute();
    }
    @Override
    public void onPause(){
        super.onPause();

        r.cancel(true);
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
                datos.add(new pedidos(jsonObject.getString("idOrder"),jsonObject.getString("nombrePlato"),jsonObject.getInt("mesa"),jsonObject.getString("mesonero"),"Pendiente"));
// "Descripcion " + jsonObject.getString("description")
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recView = (RecyclerView) findViewById(R.id.RecView);
        //recView.setHasFixedSize(true);

        final AdaptadorPedido adaptador = new AdaptadorPedido(datos);




                adaptador.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        r.cancel(true);
                        /*
                        i.refreshDrawableState();*/

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

                        /*Log.i("DemoRecView", "Pulsado el elemento " + (recView.getChildPosition(v) + 1));
                        //openact(recView.getChildPosition(v));
                        */
                    }
                });

        recView.setAdapter(adaptador);

        recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recView.setLayoutManager(new GridLayoutManager(this,2));

        /* recView.addItemDecoration(
                 new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
*/
        recView.setItemAnimator(new DefaultItemAnimator());
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
            txt2 = handler.poststatus("http://45.55.227.224/api/v1/order/changeReady", httpclient, params);
            sessionInfo=handler.sessionInfo;
            tipousuario= params[1];
            try {
                respJSON = new JSONObject(txt2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //tipousuario=txt2;
            return handler.sw;

        }
        public void onPostExecute(Boolean resul) {
            busy=false;
            Toast toast = Toast.makeText(getApplicationContext(),"busy="+busy+ " reeload = "+reload , Toast.LENGTH_SHORT);
            toast.show();
            r = new Recargar();
            r.execute();
        }


    }
    public class Recargar extends AsyncTask<String,Integer,Boolean> {
        public Boolean doInBackground(String... params) {

            do {
                tareaLarga();

            }while (!reload&&busy);
        return reload;
        }
        public void onPostExecute(Boolean resul) {
            if (reload){
                openact();
            }

        }


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
        /*public void onPostExecute(String resul ){
            txtdebug.setText(resul);
        }*/
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
    }
}
