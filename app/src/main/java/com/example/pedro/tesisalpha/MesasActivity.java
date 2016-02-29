package com.example.pedro.tesisalpha;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.util.ArrayList;

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
        setContentView(R.layout.toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarmarcos);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

}
