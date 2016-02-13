package com.example.pedro.tesisalpha;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class devolveractivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    int nmesas=1;
    Cookie sessionInfo;
    String mesas;
    Button buttonsimple;
    private RecyclerView recView;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    private Context context=this;
    private ImageView imagen;
    private Animation rotacion;
    boolean reload=false,busy=false,sw=true;
    private ArrayList<Titular> datos;
    String idorder,idproduct;
    public class ObjetosClase{
        int id;
        String nombre;
        //Constructor
        public ObjetosClase(int id, String nombre) {
            super();
            this.id = id;
            this.nombre = nombre;
        }
        @Override
        public String toString() {
            return nombre;
        }
        public int getId() {
            return id;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.devolver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbardevolver);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        idproduct = i.getStringExtra("idproduct");
        idorder = i.getStringExtra("idorder");
        mesas = i.getStringExtra("nmesa");
        httpcookies objcookie = (httpcookies)i.getSerializableExtra("cookie");
        BasicClientCookie newCookie = new BasicClientCookie(objcookie.getName(),objcookie.getValue());
        newCookie.setDomain(objcookie.getDomain());
        httpclient.getCookieStore().addCookie(newCookie);
//Creamos la lista
        LinkedList comidas = new LinkedList();
//La poblamos con los ejemplos
        comidas.add(new ObjetosClase(1, "Mala Presentacion"));
        comidas.add(new ObjetosClase(2,"Comida Fria" ));
        comidas.add(new ObjetosClase(3,"No Era Lo Solicitado" ));
        comidas.add(new ObjetosClase(4, "Tiene insectos"));
//Creamos el adaptador
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, comidas);
//Añadimos el layout para el menú y se lo damos al spinner


        TextView nom = (TextView) findViewById(R.id.txtnombprod);
        nom.setText(i.getStringExtra("nombre"));
        final Spinner s= (Spinner) findViewById(R.id.spinner);
        s.setAdapter(spinner_adapter);

        buttonsimple = (Button)findViewById(R.id.buttondevolver);
        buttonsimple.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Toast toast = Toast.makeText(getApplicationContext(),"idorder="+idorder+"isproduct"+idproduct+" id= "+(s.getSelectedItemPosition()+1), Toast.LENGTH_SHORT);
                toast.show();*/
                Mesas tarea= new Mesas();
                tarea.execute(idorder,
                        idproduct,
                        Integer.toString(s.getSelectedItemPosition() + 1)
                );

            }
        });
    }

    public void openact(String message) {
        httpcookies h = new httpcookies(sessionInfo);
        Intent i = getIntent();
        Intent intent = new Intent(this, MesaPedidoActivity.class);
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



        return super.onOptionsItemSelected(item);
    }

    public class Mesas extends AsyncTask<String,Integer,Boolean> {
        String txt="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        protected void onPreExecute() {


        }

        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.postdevolver("http://45.55.227.224/api/v1/order/returned", httpclient,params);


        sessionInfo=handler.sessionInfo;


        try {
            respJSON = new JSONObject(txt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return handler.sw;


        }
        public void onPostExecute(Boolean resul) {

            if (!resul){
                Toast toast = Toast.makeText(getApplicationContext(),txt, Toast.LENGTH_SHORT);
                toast.show();
                openact(mesas);
            }

        }


    }


}
