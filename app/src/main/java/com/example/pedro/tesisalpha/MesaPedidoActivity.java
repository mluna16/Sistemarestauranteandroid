package com.example.pedro.tesisalpha;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBarActivity;
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

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MesaPedidoActivity extends ActionBarActivity {
    public final static String EXTRA_PLATILLO="platillo";
    public final static String EXTRA_CANTIDAD="cantidad";
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private ImageView btnBotonSimple;
    Cookie sessionInfo;
    boolean reload=false,busy=false,sw=true;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    String message;
    String total,numm;
    int fact=0;
    TextView ped;
    TextView cant;
    JSONArray pedidos;
    ArrayList<Mesas> datos;
    private RecyclerView recView;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mesa_pedido);

        /*LinearLayout l = (LinearLayout) findViewById(R.id.ocultodis);
            l.setVisibility(View.VISIBLE);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbardis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        httpcookies objcookie = (httpcookies)i.getSerializableExtra("cookie");
        BasicClientCookie newCookie = new BasicClientCookie(objcookie.getName(),objcookie.getValue());
        newCookie.setDomain(objcookie.getDomain());

        httpclient.getCookieStore().addCookie(newCookie);
        // Get the message from the intent

        message = i.getStringExtra(MesasActivity.EXTRA_MESSAGE);
        /*plato = i.getStringExtra(Menu_Activity.EXTRA_PLATILLO);
        cantidad = i.getStringExtra(Menu_Activity.EXTRA_CANTIDAD);
*/

        // Create the text view
        //TextView textView = new TextView(this);
        //textView.setTextSize(40);
        //textView.setText(message);

        // Set the text view as the activity layout
        //setContentView(textView);

        //TextView txttitulo= (TextView) findViewById(R.id.txt1);
        //txttitulo.setText("Pedido Mesa Número:");


        pedido tarea= new pedido();
        tarea.execute();
        LinearLayout l = (LinearLayout) findViewById(R.id.ocultodis);
        l.setVisibility(View.VISIBLE);
       /* Recargar r = new Recargar();
        r.execute();*/
        IntentFilter filter = new IntentFilter();
        filter.addAction("eliminar");
        filter.addAction("editar");
        filter.addAction("devolver");

            registerReceiver(mBroadcast, filter);




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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_display_message, menu);
        return true;
    }
    public void clickFunc(){
        httpcookies h = new httpcookies(sessionInfo);
        Intent intent = new Intent(this, Menu_Activity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("cookie",h);
        startActivity(intent);
        //Toast.makeText(MesaPedidoActivity.this, "Button Clicked +", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            httpcookies h = new httpcookies(sessionInfo);
            Intent intent = new Intent(this, MesasActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            intent.putExtra("cookie",h);
            startActivity(intent);
            MesaPedidoActivity.this.finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MesaPedidoActivity.this);
            builder1.setMessage("Desea cerrar la cuenta?");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Si",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            modificar a = new modificar();
                            a.execute(
                                    "facturar",numm
                            );


                        }
                    });
            builder1.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class pedido extends AsyncTask<String,Integer,Boolean> {
        String txt="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        public void onPreExecute(){
            busy=true;


        }
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            txt = handler.get("http://45.55.227.224/api/v1/table/show/"+message,httpclient);
            sessionInfo=handler.sessionInfo;
            //numm=txt;
            try {
                respJSON = new JSONObject(txt);
                JSONObject data = respJSON.getJSONObject("data");
                numm=data.getString("NumberTable");
                pedidos = data.getJSONArray("Pedidos");
                //fact= data.getInt("Facturar");
                total=data.getString("CostTable");
            if (!total.equals("0")){
                JSONObject jsonObject1 = pedidos.getJSONObject(0);
                fact=jsonObject1.getInt("Facturar");

            }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return handler.sw;

        }
        public void onPostExecute(Boolean resul) {
            if (fact==1){
                setTitle("Mesa por facturar");
                btnBotonSimple = (ImageView)findViewById(R.id.button_add_item);


                btnBotonSimple.setVisibility(View.INVISIBLE);
                btnBotonSimple.setBackgroundColor(Color.GRAY);
                btnBotonSimple.setEnabled(false);
                //btnBotonSimple.setBackgroundColor(Color.GRAY);
                btnBotonSimple.setActivated(false);
            }else {
                cargado();


            }
            LinearLayout l = (LinearLayout) findViewById(R.id.ocultodis);
            l.setVisibility(View.INVISIBLE);
        }


    }

    void cargado() {
        /*TextView txttitulo= (TextView) findViewById(R.id.txt1);
        txttitulo.setText("Pedido Mesa Número: "+numm);*/
       /* getActionBar().setTitle("Pedido Mesa Número: "+numm);
        getSupportActionBar().setTitle("Pedido Mesa Número: "+numm);*/
        setTitle("Pedido Mesa Número: "+numm);

/*        for(int i=0; i<pedidos.length(); i++){
            try {
                JSONObject jsonObject = pedidos.getJSONObject(i);
                ped = (TextView)findViewById(R.id.Lblpedido);
                ped.setText(ped.getText()+jsonObject.getString("ProductName")+"\n");
                cant = (TextView)findViewById(R.id.Lblcantidad);
                cant.setText(cant.getText()+"1"+"\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        datos = new ArrayList<Mesas>();
        for(int i=0; i<pedidos.length(); i++){
            try {
                JSONObject jsonObject = pedidos.getJSONObject(i);
                datos.add(new Mesas(jsonObject.getString("OrderId"),jsonObject.getString("ProductId"),jsonObject.getString("ProductName"),jsonObject.getDouble("ProductCost"),jsonObject.getString("OrderState")));
// "Descripcion " + jsonObject.getString("description")
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recView = (RecyclerView) findViewById(R.id.RecViewmesa);
  //      recView.setHasFixedSize(true);
//
        final AdaptadorMesa adaptador = new AdaptadorMesa(datos,getApplicationContext());

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int posi = recView.getChildAdapterPosition(v);
                final String n;

                n = datos.get(posi).getId();

/*

                Toolbar t;

                t = (Toolbar) v.findViewById(R.id.toolbarCard);
                t.inflateMenu(R.menu.menu_card);

                t.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_option1:
//editar
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MesaPedidoActivity.this);
                                builder1.setMessage("Desea editar el pedido: ");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton("Si",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {




                                            }
                                        });
                                builder1.setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                                break;
                            case R.id.action_option2:
//eliminar

                                break;
                            case R.id.action_option3:
//devolver

                                break;
                        }
                        return true;
                    }

                });*/


            }
        });

        recView.setAdapter(adaptador);

        recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //recView.setLayoutManager(new GridLayoutManager(this,2));
/*
         recView.addItemDecoration(
                 new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
*/

        recView.setItemAnimator(new DefaultItemAnimator());

        btnBotonSimple = (ImageView)findViewById(R.id.button_add_item);

        btnBotonSimple.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                clickFunc();
            }
        });
        busy=false;
        /*LinearLayout l = (LinearLayout) findViewById(R.id.oculto);
        l.setVisibility(View.INVISIBLE);*/
    }
    public class modificar extends AsyncTask<String,Integer,Boolean> {
        String txt2="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
        protected void onPreExecute() {

        }
        public Boolean doInBackground(String... params) {
            httphandler handler = new httphandler();
            if (params[0].equals("facturar")){
                txt2 = handler.get("http://45.55.227.224/api/v1/table/getInvoice/" + params[1], httpclient);
            }
            if (params[0].equals("editar")){
           //     txt2 = handler.put("http://45.55.227.224/api/v1/order/edit/"+"idproducto a editar", httpclient);
            }
            if (params[0].equals("eliminar")){
                txt2 = handler.delete("http://45.55.227.224/api/v1/order/delete/" + params[1], httpclient);
            }
            if (params[0].equals("devolver")){

                //id_order id_product type
                //txt2 = handler.postdevolver("http://45.55.227.224/api/v1/order/returned", httpclient,params);

            }
            sessionInfo=handler.sessionInfo;


            try {
                respJSON = new JSONObject(txt2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return handler.sw;

        }
        public void onPostExecute(Boolean resul) {

            Toast toast = Toast.makeText(getApplicationContext(), txt2, Toast.LENGTH_SHORT);
            toast.show();
            //openact();
        }


    }
    public class Recargar extends AsyncTask<String,Integer,Boolean> {
        public Boolean doInBackground(String... params) {
        if(sw){
            do {
                tareaLarga();

            }while (!reload&&busy);}
            return reload;

        }
        public void onPostExecute(Boolean resul) {
            if (reload){
                recarga();
            }

        }


    }

    private void recarga() {

        Toast toast = Toast.makeText(getApplicationContext(),"recargando" , Toast.LENGTH_SHORT);
        toast.show();
        pedido p = new pedido();
        p.execute();
        reload=false;
        Recargar r = new Recargar();
        r.execute();
    }
    public void openact2(String menssage,String message1,String message2) {
        httpcookies h = new httpcookies(sessionInfo);
        Intent i = getIntent();
        Intent intent = new Intent(this, devolveractivity.class);
        intent.putExtra("nombre", menssage);
        intent.putExtra("idproduct", message2);
        intent.putExtra("idorder", message1);
        intent.putExtra("nmesa",message);
        intent.putExtra("cookie",h);
        startActivity(intent);

    }
    private void tareaLarga() {
        try {
            Thread.sleep(5000);
            reload=true;
        } catch(InterruptedException e) {}
    }
    BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {



            switch (intent.getAction()){
                case ("editar"):{
                    Toast.makeText(getBaseContext(),"editar" + intent.getExtras().getString("nombre"),Toast.LENGTH_LONG).show();
                    break;
                }
                case("devolver"):{

                     openact2(intent.getExtras().getString("nombre"),intent.getExtras().getString("idorder"),intent.getExtras().getString("idproduct") );

                    break;
                }
                case("eliminar"):{
                    Toast.makeText(getBaseContext(),"eliminar" + intent.getExtras().getString("idorder"),Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MesaPedidoActivity.this);
                    builder1.setMessage("Desea eliminar esta orden?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    modificar a = new modificar();
                                    a.execute(
                                            "eliminar", intent.getExtras().getString("idorder")
                                    );

                                }
                            });
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                    break;
                }
            }


            /*if(intent.getAction().equals("editar")){
                Toast.makeText(getBaseContext(),"CONTEXT" + intent.getExtras().getString("nombre"),Toast.LENGTH_LONG).show();
            }*/


        }
    };
}
