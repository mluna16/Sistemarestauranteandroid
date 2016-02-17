package com.example.pedro.tesisalpha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pedro on 15/02/2016.
 */
public class Utilidades {
    public static AlertDialog MostrarAlertDialog(Context activity, String mensaje, String titulo, int icono)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(mensaje);
        builder1.setIcon(icono);
        builder1.setTitle(titulo);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder1.create();
        return alertDialog;
    }

    public static boolean CheckPlayServices(Activity context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context, 9000).show();
            }
            else
            {
                Toast.makeText(context, "Dispositivo no soportado", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    public static String ObtenerRegistrationTokenEnGcm(Context context) throws  Exception
    {
        InstanceID instanceID = InstanceID.getInstance(context);
        String token = instanceID.getToken(context.getString(R.string.senderid),
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        return token;
    }

    public static String RegistrarseEnAplicacionServidor(Context context,String registrationToken) throws  Exception
    {
        String imei = DameIMEI(context);
        String stringUrl = "http://45.55.227.224/api/v1/user/code" + imei + "&registrationId=" + registrationToken;

        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000 /* milliseconds */);
        connection.setConnectTimeout(15000 /* milliseconds */);
        connection.setRequestMethod("GET");

        int codigoEstado = connection.getResponseCode();
        if(codigoEstado != 200)
            throw new Exception("Error al procesar registro. Estado Http: " + codigoEstado);

        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

        String respuesta = "",linea;
        while ((linea = bufferedReader.readLine()) != null) {
            respuesta = respuesta + linea;
        }

        bufferedReader.close();
        inputStream.close();

        respuesta = new JSONObject(respuesta).getString("RegistroGcmResult");

        if(!respuesta.equals("OK"))
            throw new Exception("Error al registrarse en aplicacion servidor: " + respuesta);

        return respuesta;

    }
    public static String DameIMEI(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

}
