package com.example.pedro.tesisalpha;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pedro on 18/06/2015.
 */



public class httphandler {
Boolean sw=true;
    String text="",text1="";
    List<Cookie> cookies;
    Cookie sessionInfo;
    public String post(String posturl,DefaultHttpClient httpclient,String... param){

        try {
/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
/*            params.add(new BasicNameValuePair("password",param[1]));
            params.add(new BasicNameValuePair("email",param[0]));*/
            if (param[0].equals("1")){
                params.add(new BasicNameValuePair("email","cocina@luna.com"));
                params.add(new BasicNameValuePair("password","12345"));
            }else {
                params.add(new BasicNameValuePair("email","mesonero@luna.com"));
                params.add(new BasicNameValuePair("password","12345"));
            }



		/*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

                  /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
//cookies
            obtenercookie(httpclient);

            //httpclient.getConnectionManager().shutdown();
            //fin de cookies
            text = EntityUtils.toString(ent);
            if(!resp.equals("true"))
                sw = false;
        }
        catch(Exception e) { text="error= "+e;
            sw = false;
        }
        return text;
    }
    public String postgcm(String posturl,DefaultHttpClient httpclient,String usuario, String regId){

        try {
/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
/*            params.add(new BasicNameValuePair("password",param[1]));
            params.add(new BasicNameValuePair("email",param[0]));*/

                params.add(new BasicNameValuePair("iduser",usuario));
                params.add(new BasicNameValuePair("codigo",regId));




		/*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

                  /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
//cookies
            obtenercookie(httpclient);

            //httpclient.getConnectionManager().shutdown();
            //fin de cookies
            text = EntityUtils.toString(ent);
            if(!resp.equals("true"))
                sw = false;
        }
        catch(Exception e) { text="error= "+e;
            sw = false;
        }
        return text;
    }
    public String poststatus(String posturl,DefaultHttpClient httpclient,String... param){

        try {
/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idOrder",param[1]));


		/*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

                  /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
//cookies
            obtenercookie(httpclient);

            //httpclient.getConnectionManager().shutdown();
            //fin de cookies
            text = EntityUtils.toString(ent);
            if(!resp.equals("true"))
                sw = false;
        }
        catch(Exception e) { text="error= "+e;
            sw = false;
        }
        return text;
    }
    public String postpedido(String posturl,DefaultHttpClient httpclient,String... param){

        try {
/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("idProduct",param[1]));
            params.add(new BasicNameValuePair("idTable",param[0]));
            params.add(new BasicNameValuePair("cantidad",param[2]));

		/*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

                  /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
//cookies
            obtenercookie(httpclient);

            //httpclient.getConnectionManager().shutdown();
            //fin de cookies
            text = EntityUtils.toString(ent);
            if(!resp.equals("true"))
                sw = false;
        }
        catch(Exception e) { text="error= "+e;
            sw = false;
        }
        return text;
    }
    public void obtenercookie(DefaultHttpClient httpclient){
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                text=text+("- " + cookies.get(i).toString());
                sessionInfo = cookies.get(0);
            }
        }
    }
    public String get(String geturl,DefaultHttpClient httpClient){
        HttpGet del = new HttpGet(geturl);
        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());
            text1=respStr;
            //cookies
            obtenercookie(httpClient);

        }

        catch(Exception e) { text1="error= "+e;
            sw = false;
        }
        return text1;
    }

    public String delete(String s, DefaultHttpClient httpClient) {
        HttpDelete del = new HttpDelete(s);
        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());
            text1=respStr;
            //cookies
            obtenercookie(httpClient);

        }

        catch(Exception e) { text1="error= "+e;
            sw = false;
        }
        return text1;
    }

    public String postdevolver(String s, DefaultHttpClient httpclient, String[] param) {
        // id_product type
        try {
/*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(s);
/*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_order",param[0]));
            params.add(new BasicNameValuePair("id_product",param[1]));
            params.add(new BasicNameValuePair("type",param[2]));

		/*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

                  /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
//cookies
            obtenercookie(httpclient);

            //httpclient.getConnectionManager().shutdown();
            //fin de cookies
            text = EntityUtils.toString(ent);
            if(!resp.equals("true"))
                sw = false;
        }
        catch(Exception e) { text="error= "+e;
            sw = false;
        }
        return text;
    }
}
//        email=mesonero%40luna.com&password=12345