package com.example.pedro.tesisalpha;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Sesion extends ActionBarActivity {
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    private static final String PROPERTY_USER = "user";
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    String SENDER_ID = "373000797222";
    static final String TAG = "GCMDemo";
    private String regid;
    private GoogleCloudMessaging gcm;

    TextView txtEmail,txtPassword,lblResultado;
    Button btnBotonSimple;
    ProgressDialog mProgressDialog;
    private Context context=this;
    private ImageView imagen;
    private Animation rotacion;
    String idusuario="";
    httphandler handler;
    Cookie sessionInfo;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        int estado = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        btnBotonSimple = (Button)findViewById(R.id.sign_in_button);
        txtEmail = (TextView) findViewById(R.id.lblemail);
        txtPassword = (TextView) findViewById(R.id.lblpassword);
        //mProgressDialog = (ProgressDialog) findViewById(R.id.mProgressDialog);
 /*       final int millisToWait=1000;
        int millisToVibrate=2000;
        final long[] vibratePattern = new long[] {
                millisToWait, millisToVibrate,
                millisToWait, millisToVibrate
        };*/
        btnBotonSimple.setOnClickListener(new View.OnClickListener() {

@Override
            public void onClick(View v) {
 /*   NotificationCompat.Builder builder =
            new NotificationCompat.Builder(context);
    Notification notification = builder
            .setContentTitle("Title")
            .setContentText("sesion iniciada")
            .setSmallIcon(R.drawable.notification_template_icon_bg)
            .setVibrate(vibratePattern)
            .setLights(Color.MAGENTA, millisToWait, millisToWait)
            .build();
    NotificationManagerCompat notificationManager =
            NotificationManagerCompat.from(context);

    notificationManager.notify(0x1234, notification);*/
    IniciarSesion tarea = new IniciarSesion();
                tarea.execute(
                        txtEmail.getText().toString(),
                        txtPassword.getText().toString());
    LinearLayout l = (LinearLayout) findViewById(R.id.oculto);
    l.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sesion, menu);
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

    //Tarea Asíncrona para llamar al WS de inicio de sesion en segundo plano
    public class IniciarSesion extends AsyncTask<String,Integer,Boolean> {

        String txt="Iniciando Sesion",txt1="cargando mesas",tipousuario="";
        int nmesas=1;
        JSONObject respJSON;
        @Override
        protected void onPreExecute() {

        }
        public Boolean doInBackground(String... params) {


            handler = new httphandler();
            //httphandler handler1 = new httphandler();
            txt = handler.post("http://45.55.227.224/api/v1/login",httpclient,params);
            sessionInfo=handler.sessionInfo;
            try {
                respJSON = new JSONObject(txt);

                JSONObject data = respJSON.getJSONObject("data");
                JSONObject userdata = data.getJSONObject("userData");
                tipousuario=userdata.getString("type");
                idusuario=userdata.getString("id");
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
                return false;

            }


        }

        public void onPostExecute(Boolean resul ){

                if (!resul){
                    //Toast toast = Toast.makeText(getApplicationContext(),"verifique su conexion a internet" , Toast.LENGTH_SHORT);
                    Toast toast = Toast.makeText(getApplicationContext(),txt , Toast.LENGTH_SHORT);
                    toast.show();
                    LinearLayout l = (LinearLayout) findViewById(R.id.oculto);
                    l.setVisibility(View.INVISIBLE);
                }

            Intent intent = null;
            httpcookies h = new httpcookies(sessionInfo);
            if (tipousuario.equals("mesonero")){
                intent = new Intent(Sesion.this, Mesas_Activity.class);
            }else{
                if (tipousuario.equals("cocina")){
                    intent = new Intent(Sesion.this, Pedido_Activity.class);
                }
            }
            context = getApplicationContext();
            //Chequemos si está instalado Google Play Services
            //if(checkPlayServices())
            //{
            gcm = GoogleCloudMessaging.getInstance(Sesion.this);
            Log.d(TAG, "Registro GCM" + gcm);
            //Obtenemos el Registration ID guardado
            regid = getRegistrationId(context);
            //txtdebug.setText(regid);
            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                TareaRegistroGCM tarea = new TareaRegistroGCM();
                tarea.execute(idusuario);
            }
            intent.putExtra("json", txt);
            intent.putExtra("cookie",h);
            intent.putExtra("idusuario",idusuario);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        }
    }

    private String getRegistrationId(Context context) {
        SharedPreferences prefs = getSharedPreferences(
                Mesas_Activity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        Log.d(TAG, "R" + context);
        if (registrationId.length() == 0) {
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
        Log.d(TAG, "id usuario"+idusuario);
        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion) {
            Log.d(TAG, "Nueva versión de la aplicación.");
            return "";
        } else if (System.currentTimeMillis() > expirationTime) {
            Log.d(TAG, "Registro GCM expirado.");
            return "";
        } else if (!idusuario.equals(registeredUser)) {
            Log.d(TAG, "Nuevo nombre de usuario.");
            return "";
        }

        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    private class TareaRegistroGCM extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String msg = "";

            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }

                //Nos registramos en los servidores de GCM
                regid = gcm.register(SENDER_ID);

                Log.d(TAG, "Registrado en GCM: registration_id=" + regid);

                //Nos registramos en nuestro servidor
                boolean registrado = registroServidor(params[0], regid);

                //Guardamos los datos del registro
                if (registrado) {
                    Log.d(TAG, "Guardamos los datos del registro");
                    setRegistrationId(context, idusuario, regid);
                    msg = "done" + "\n";
                }
            } catch (IOException ex) {
                msg = "error" + "\n";
                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
            }

            return msg;
            //return regid;
        }

        public void onPostExecute(String resul) {
            //txtdebug.setText(resul);
        }
    }

    private void setRegistrationId(Context context, String user, String regId) {
        SharedPreferences prefs = getSharedPreferences(
                Mesas_Activity.class.getSimpleName(),
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

    private boolean registroServidor(String usuario, String regId) {
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
