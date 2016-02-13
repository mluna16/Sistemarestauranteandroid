package com.example.pedro.tesisalpha;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


public class Sesion extends ActionBarActivity {
    TextView txtEmail,txtPassword,lblResultado;
    Button btnBotonSimple;
    private Context context=this;
    private ImageView imagen;
    private Animation rotacion;
    httphandler handler;
    Cookie sessionInfo;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        btnBotonSimple = (Button)findViewById(R.id.sign_in_button);
        txtEmail = (TextView) findViewById(R.id.lblemail);
        txtPassword = (TextView) findViewById(R.id.lblpassword);
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


            if (tipousuario.equals("mesonero")){
                httpcookies h = new httpcookies(sessionInfo);
                Intent intent;
                intent = new Intent(Sesion.this, MesasActivity.class);
                intent.putExtra("json", txt);
                intent.putExtra("cookie",h);

                startActivity(intent);
            }else{
                if (tipousuario.equals("cocina")){
                    httpcookies h = new httpcookies(sessionInfo);
                    Intent intent;
                    intent = new Intent(Sesion.this, Pedido_Activity.class);
                    //intent.putExtra("json", txt);
                    intent.putExtra("cookie",h);

                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                }
            }

        }
    }

}
