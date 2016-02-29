package com.example.pedro.tesisalpha;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService
{
    private static final int NOTIF_ALERTA_ID = 1;
    public Context mContext=this;
    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                mostrarNotification(extras.getString("message"), extras.getString("title"), extras.getString("subtitle"), extras.getString("tickerText"), extras.getString("idusuario"), extras.getString("numero_mesa"), extras.getString("idorder"),extras.getString("idproduct"), extras.getString("costproduct"));


            }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void enviarbroadcast(String msg,String tit,String sub,String tipouser, String iduser,String nmesa,String idorder,String idproduct,String costproduct) {
        Intent intent = new Intent();
        switch(tit) {
            case "Orden Eliminada":
                intent.putExtra("accion", "eliminar");
                break;
            case "Nueva Orden":
                intent.putExtra("accion", "agregar");
                break;
            case "Nueva Devolucion":
                intent.putExtra("accion", "devolver");
                break;
            case "Orden Lista":
                intent.putExtra("accion", "remover");
                break;
            //default:
                //intent.putExtra("accion", "remover");
        }
        intent.setAction("actualizar");
        intent.putExtra("nombre", tit);
        intent.putExtra("mesa", nmesa);
        intent.putExtra("hobb", sub);
        intent.putExtra("idorder",idorder);
        intent.putExtra("iduser",iduser);
        intent.putExtra("idproduct", idproduct);

        intent.putExtra("costproduct",costproduct);
        mContext.sendBroadcast(intent);

    }

    private void mostrarNotification(String msg,String tit,String sub,String tipouser, String iduser,String nmesa,String idorder,String idproduct,String costproduct)
    {
        enviarbroadcast(msg, tit, sub, tipouser, iduser, nmesa, idorder,idproduct, costproduct);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setContentTitle(tit)
                        .setSubText(sub)
                        /*.setVibrate(new long[]{100, 250, 100, 500})
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))*/
                        .setContentText(msg);

        Log.i("marcos", tit + " " + sub + " " + tipouser + " " + iduser + " " + nmesa + " " + idorder);
        Log.i("marcos", "idpro "+idproduct+" cost"+costproduct);
        Intent notIntent;
        SharedPreferences prefs = getSharedPreferences(
                "usuario",
                Context.MODE_PRIVATE);
        boolean sesion = prefs.getBoolean("sesion", false) ;
        if (sesion){
            if (tipouser.equals("cocina")){
                notIntent =  new Intent(this, Pedido_Activity.class);
            }else {
                notIntent =  new Intent(this, MesaPedidoActivity.class);
                notIntent.putExtra("message", nmesa);

            }
        }else{
            notIntent =  new Intent(this, Sesion.class);
        }

        /*SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mesa", nmesa);
        editor.commit();*/
        notIntent.setData((Uri.parse("foobar://" + SystemClock.elapsedRealtime())));
        PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, 0);

        mBuilder.setContentIntent(contIntent);
        //if (prefs.getString("idusuario", "").equals(iduser)){
            mNotificationManager.notify(Integer.parseInt(idorder), mBuilder.build());
        //}



    }
}