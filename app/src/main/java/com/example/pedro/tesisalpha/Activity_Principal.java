package com.example.pedro.tesisalpha;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Activity_Principal extends FragmentActivity implements Fragment_Lista.Callbacks {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    int nmesas=10;
    Cookie sessionInfo;
    JSONArray mesas;
    //private RecyclerView recView;
    DefaultHttpClient httpclient = new DefaultHttpClient();

   // private ArrayList<Titular> datos;
	//Guardar� TRUE si es una pantalla grande y caben dos fragmentos; o FALSE si es una pantalla peque�a y cabe solo un fragmento
	private boolean dosFragmentos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_principal_listado);
        //parametros de datos
        Intent i = getIntent();
        httpcookies objcookie = (httpcookies)i.getSerializableExtra("cookie");
        BasicClientCookie newCookie = new BasicClientCookie(objcookie.getName(),objcookie.getValue());
        newCookie.setDomain(objcookie.getDomain());
        httpclient.getCookieStore().addCookie(newCookie);
        Mesas tarea= new Mesas();
        tarea.execute();
		//Se comprueba que exista el framelayout framelayout_contenedor_entrada. Si existe estaremos usando activity_dospaneles.xml, sino estaremos usando activity_listado.xml 
		if (findViewById(R.id.framelayout_contenedor_detalle) != null) {
			// Entra aqu� solo en dise�os para pantallas grandes (es decir, si usamos res/values-large o res/values-sw600dp). Estaremos usando activity_dospaneles.xml
			dosFragmentos = true;
		}
        //onEntradaSelecionada("");
    }

	
	/**
	 * M�todo Callback que escucha cuando un elemento de la lista ha sido pulsado, entonces entra aqu�. Devuelve el ID del elemento de la lista que fue seleccionado
	 */
	@Override
	public void onEntradaSelecionada(String id) {
		if (dosFragmentos) {
			// Si estamos en pantallas grandes, se mostrar� el detalle seleccionado en esta misma actividad remplazando el fragmento del detalle por el nuevo
			Bundle arguments = new Bundle();
			arguments.putString(Fragment_Detalle.ARG_ID_ENTRADA_SELECIONADA, id);
			Fragment_Detalle fragment = new Fragment_Detalle();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_contenedor_detalle, fragment).commit();

		} else {
			// En pantallas peque�as, iniciaremos una nueva actividad con el contenido del elemento seleccionado de la lista
			Intent detailIntent = new Intent(this, Activity_Detalle.class);
			detailIntent.putExtra(Fragment_Detalle.ARG_ID_ENTRADA_SELECIONADA, id);
			startActivity(detailIntent);
		}
	}

    public class Mesas extends AsyncTask<String,Integer,Boolean> {
        String txt="Cargando Mesas",tipousuario="";
        JSONObject respJSON;
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


        }


    }
}
