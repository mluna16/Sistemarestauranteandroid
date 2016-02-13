package com.example.pedro.tesisalpha;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    httphandler handler;
    Cookie sessionInfo;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    JSONObject respJSON;
    String txt="Iniciando Sesion",txt1="cargando mesas",tipousuario="";
    public static final String EXTRA_MESSAGE = "message";
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
    private EditText txtUsuario;
    private TextView txtdebug;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtdebug = (TextView)findViewById(R.id.textDebug);
        btnRegistrar = (Button)findViewById(R.id.btnGuadar);

        //btnRegistrar.setOnClickListener(new OnClickListener() {
/*
            @Override
            public void onClick(View v)
            {*/
                context = getApplicationContext();

                //Chequemos si está instalado Google Play Services
                //if(checkPlayServices())
                //{
                gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                Log.d(TAG, "Registro GCM"+gcm);
                //Obtenemos el Registration ID guardado
                regid = getRegistrationId(context);
                txtdebug.setText(regid);
                //Si no disponemos de Registration ID comenzamos el registro
                if (regid.equals("")) {
                    TareaRegistroGCM tarea = new TareaRegistroGCM();
                    tarea.execute(txtUsuario.getText().toString());
                }
                //}
                //else
                //{
                //    Log.i(TAG, "No se ha encontrado Google Play Services.");
                //}
   /*         }
        });*/
    }

//	@Override
//	protected void onResume()
//	{
//	    super.onResume();
//
//	    checkPlayServices();
//	}

//	private boolean checkPlayServices() {
//	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//	    if (resultCode != ConnectionResult.SUCCESS)
//	    {
//	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
//	        {
//	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
//	        }
//	        else
//	        {
//	            Log.i(TAG, "Dispositivo no soportado.");
//	            finish();
//	        }
//	        return false;
//	    }
//	    return true;
//	}

    private String getRegistrationId(Context context)
    {
        SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        Log.d(TAG, "R"+context);
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
        else if (!txtUsuario.getText().toString().equals(registeredUser))
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
        catch (NameNotFoundException e)
        {
            throw new RuntimeException("Error al obtener versión: " + e);
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
                boolean registrado = registroServidor(params[0], regid);

                //Guardamos los datos del registro
                if(registrado)
                {
                    Log.d(TAG, "Guardamos los datos del registro");
                    setRegistrationId(context, tipousuario, regid);
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
            txtdebug.setText(resul);
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
        boolean reg = false;
        handler = new httphandler();
        //httphandler handler1 = new httphandler();
        txt = handler.post("http://45.55.227.224/api/v1/login",httpclient,usuario,regId);
        sessionInfo=handler.sessionInfo;
        try {
            respJSON = new JSONObject(txt);

            JSONObject data = respJSON.getJSONObject("data");
            JSONObject userdata = data.getJSONObject("userData");
            tipousuario=userdata.getString("id");

                Log.d(TAG, "Registrado en mi servidor.");
            if(tipousuario!=null){
                handler = new httphandler();
                //httphandler handler1 = new httphandler();
                txt = handler.postgcm("http://45.55.227.224/api/v1/user/code", httpclient, tipousuario, regId);
                sessionInfo=handler.sessionInfo;
                //respJSON = new JSONObject(txt);

                    //JSONObject data1 = respJSON.getJSONObject(txt);
                    if (txt.equals("1")){
                        reg = true;
                    }else {
                        reg = false;
                        Log.d(TAG, "Error registro en mi servidor: ");
                    }


            }


        }
        catch (Exception e)
        {
            //reg = false;
            Log.d(TAG, "Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
        }



        return reg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}