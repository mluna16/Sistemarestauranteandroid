package com.example.pedro.tesisalpha;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService
{
    private static final int NOTIF_ALERTA_ID = 1;

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
                mostrarNotification(extras.getString("message"),extras.getString("title"),extras.getString("subtitle"),extras.getString("tickerText"),extras.getString("idusuario"),extras.getString("numero_mesa"),extras.getString("idorder"));
            }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void mostrarNotification(String msg,String tit,String sub,String tipouser, String iduser,String nmesa,String idorder)
    {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setContentTitle(tit)
                        .setSubText(sub)
                        .setContentText(msg);
        Log.i("gcm", tit+" "+sub+" "+tipouser+" "+iduser+" "+nmesa+" "+idorder);
        Intent notIntent;
        if (tipouser.equals("cocina")){
            notIntent =  new Intent(this, Pedido_Activity.class);
        }else {
            notIntent =  new Intent(this, MesaPedidoActivity.class);
            SharedPreferences prefs =
                    getSharedPreferences("usuario",Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("mesa", nmesa);
            //editor.putString("nombre", "Prueba");
            editor.commit();
        }

        PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, 0);

        mBuilder.setContentIntent(contIntent);

        mNotificationManager.notify(Integer.parseInt(idorder), mBuilder.build());

    }
}