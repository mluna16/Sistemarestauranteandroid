package com.example.pedro.tesisalpha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Menu_Activity extends ActionBarActivity {
    public final static String EXTRA_PLATILLO="platillo";
    public final static String EXTRA_CANTIDAD="cantidad";
    private ViewGroup linearLayoutDetails;
    private ImageView imageViewExpand;
    private static final int DURATION = 250;
    AdaptadorMenu adaptador;
    int max=0;
    String data;
    Cookie sessionInfo;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    JSONArray productos;
    ArrayList<Menues> datos;
    public final static String EXTRA_MESSAGE = "message";
    private RecyclerView recView;
    String message,plato,cantidad;
    private ImageView btnok;
    Boolean sw=false;
    String[] ncantidad;
    String[] nproducto;
    private Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarmenu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        message=i.getStringExtra(MesaPedidoActivity.EXTRA_MESSAGE);
        /*httpcookies objcookie = (httpcookies)i.getSerializableExtra("cookie");
        BasicClientCookie newCookie = new BasicClientCookie(objcookie.getName(),objcookie.getValue());
        newCookie.setDomain(objcookie.getDomain());*/
        SharedPreferences prefs = getSharedPreferences(
                "usuario",
                Context.MODE_PRIVATE);
        String cookienombre = prefs.getString("cookie_nombre", "") ;
        String cookievalor = prefs.getString("cookie_valor", "") ;
        String cookiedominio = prefs.getString("cookie_dominio", "") ;
        BasicClientCookie newCookie = new BasicClientCookie(cookienombre,cookievalor);
        newCookie.setDomain(cookiedominio);
        //message  = prefs.getString("mesa", i.getStringExtra(EXTRA_MESSAGE));
        message  = i.getStringExtra(EXTRA_MESSAGE);
        httpclient.getCookieStore().addCookie(newCookie);
        productos p = new productos();
        p.execute();
        LinearLayout l = (LinearLayout) findViewById(R.id.ocultomenu);
        l.setVisibility(View.VISIBLE);
        IntentFilter filter = new IntentFilter();
        filter.addAction("actualizar");
        registerReceiver(mBroadcast, filter);
    }

    public void openact() {
        //Toast toast = Toast.makeText(getApplicationContext(),"" , Toast.LENGTH_SHORT);

        httpcookies h = new httpcookies(sessionInfo);
        Intent intent = new Intent(this, MesaPedidoActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("gcm", false);
        intent.putExtra("cookie",h);
        startActivity(intent);
    }
    public void toggleDetails(View view) {
        if (linearLayoutDetails.getVisibility() == View.GONE) {
            ExpandAndCollapseViewUtil.expand(linearLayoutDetails, DURATION);
            imageViewExpand.setImageResource(R.mipmap.more);
            rotate(-180.0f);
        } else {
            ExpandAndCollapseViewUtil.collapse(linearLayoutDetails, DURATION);
            imageViewExpand.setImageResource(R.mipmap.less);
            rotate(180.0f);
        }
    }

    private void rotate(float angle) {
        Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(DURATION);
        imageViewExpand.startAnimation(animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public class productos extends AsyncTask<String,Integer,Boolean> {
        String txt="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.get("http://45.55.227.224/api/v1/producto",httpclient);
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
            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomenu);
            l.setVisibility(View.INVISIBLE);
        }


    }

    void cargado() {
        //TextView txttitulo= (TextView) findViewById(R.id.txt1);
        datos = new ArrayList<Menues>();
        for(int i=0; i<productos.length(); i++){
            try {
                JSONObject jsonObject = productos.getJSONObject(i);
                datos.add(new Menues(jsonObject.getString("id_product"),jsonObject.getString("image"),jsonObject.getString("name"),jsonObject.getString("description"),jsonObject.getDouble("cost"),jsonObject.getInt("inventory"), false,1,false));
// "Descripcion " + jsonObject.getString("description")
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recView = (RecyclerView) findViewById(R.id.RecView);
       // recView.setHasFixedSize(true);

        adaptador = new AdaptadorMenu(datos);


        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* linearLayoutDetails = (ViewGroup) v.findViewById(R.id.linearLayoutDetails);
                imageViewExpand = (ImageView) v.findViewById(R.id.imageViewExpand);*/
                Log.i("DemoRecView", "Pulsado el elemento " + (recView.getChildPosition(v) + 1));
                //openact(recView.getChildPosition(v));

          /*      String n;
                n=datos.get(posi).getId();*/
               /* Agregar a = new Agregar();
                a.execute(
                        message,
                        n
                );*/

            }
        });

        recView.setAdapter(adaptador);

        recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recView.setLayoutManager(new GridLayoutManager(this,2));

         recView.addItemDecoration(
                 new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        recView.setItemAnimator(new DefaultItemAnimator());
        btnok = (ImageView)findViewById(R.id.button_add_pedido);


        btnok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                seleccionados();
                for (int i=0;i<ncantidad.length;i++){
                    //for (int j=0;j<genera[i].length;j++){
                        Log.i("genera", "cant= "+ncantidad[i]+" id= "+nproducto[i]);
                    //}
                }
                Agregar a = new Agregar();
                a.execute(
                        message,
                        "",
                        ""
                );

            }
        });
    }
    private void seleccionados(){
        data = "";
        String alfa="",beta="";
        ArrayList<Menues> stlist = ((AdaptadorMenu) adaptador)
                .getMenuestist();
        int j=0;

        for (int i = 0; i < stlist.size(); i++) {
            Menues seleccion = stlist.get(i);
            if (seleccion.isSelected() == true) {
                String a= seleccion.getCantidad().toString();//cantidad
                String b= seleccion.getId();//idproducto
                if ((stlist.size()-1)==i){
                    alfa=alfa+a;
                    beta=beta+b;
                }else {
                    alfa=alfa+a+":";
                    beta=beta+b+":";
                }

                //Toast.makeText(Menu_Activity.this, "\n" + message+ " cant " + genera[j][0] + " id " + genera[j][1], Toast.LENGTH_LONG).show();
                j++;
                //data = data + "\n" + seleccion.getNombre().toString() + " " + seleccion.getCantidad();
						/*
						 * Toast.makeText( CardViewActivity.this, " " +
						 * singleStudent.getName() + " " +
						 * singleStudent.getEmailId() + " " +
						 * singleStudent.isSelected(),
						 * Toast.LENGTH_SHORT).show();
						 * 0,0 0,1
						 * 1,0 1,1
						 * 2,0 2,1
						 *
						 */
            }

        }
        Log.i("selecc", "seleccionados ");
        ncantidad = alfa.split(":");
        nproducto = beta.split(":");

    }
    public class Agregar extends AsyncTask<String,Integer,Boolean> {
        String trx="",txt2="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        protected void onPreExecute() {
            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomenu);
            l.setVisibility(View.VISIBLE);
        }
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            httphandler handler1 = new httphandler();
            for (int i = 0; i < ncantidad.length; i++) {
              /*  */
                    params[1]=nproducto[i];
                    params[2]=ncantidad[i];
                    txt2 = handler.postpedido("http://45.55.227.224/api/v1/order/store", httpclient, params);
                    //trx = handler1.get("http://45.55.227.224/api/v1/user/send", httpclient);
                    sessionInfo = handler.sessionInfo;
            }
            return handler.sw;

        }
        public void onPostExecute(Boolean resul) {
            LinearLayout l = (LinearLayout) findViewById(R.id.ocultomenu);
            l.setVisibility(View.INVISIBLE);
            openact();

        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
        /*    httpcookies h = new httpcookies(sessionInfo);
            Intent intent = new Intent(this, MesasActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            intent.putExtra("cookie",h);
            startActivity(intent);*/
            Menu_Activity.this.finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void agregar(String idproduct, String nMesa, String parc){
        //String[] res= parc.split(": ");
        ArrayList<Menues> stlist = ((AdaptadorMenu) adaptador)
                .getMenuestist();
        for (int i = 0; i < stlist.size(); i++) {
            Menues seleccion = stlist.get(i);
            if (seleccion.getId().equals(idproduct)) {
                seleccion.setLimite(seleccion.getLimite() - 1);
                Log.i("agregar", seleccion.getNombre()+"new lim "+seleccion.getLimite()+" "+parc);
                if ((seleccion.getLimite()-1)>0){
                    datos.set(i,seleccion);
                    adaptador.notifyItemChanged(i);
                }else {
                    datos.remove(i);
                    adaptador.notifyItemRemoved(i);
                }

            }
        }
    }
    BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            if(intent.getAction().equals("actualizar")){
                String x=intent.getStringExtra("accion");
                if (x.equals("eliminar")||x.equals("devolver")||x.equals("remover")){
                    //remover(intent.getStringExtra("idorder"));
                }else {
                    if(x.equals("agregar")){
                        SharedPreferences prefs = getSharedPreferences(
                                "usuario",
                                Context.MODE_PRIVATE);
                        String nombre = prefs.getString("idusuario", "") ;
                        //if (!intent.getStringExtra("iduser").equals(nombre)){
                            agregar(intent.getStringExtra("idproduct"),intent.getStringExtra("mesa"),intent.getStringExtra("idorder"));
                        //}
                    }else{
                        if(x.equals("editar")){

                        }
                    }
                }
/*                seleccionados();
                recyclerViewState = recView.getLayoutManager().onSaveInstanceState();//save
                productos p = new productos();
                p.execute();
                Toast.makeText(getBaseContext(),intent.getExtras().getString("nombre"),Toast.LENGTH_LONG).show();*/
            }
        }
    };
}
