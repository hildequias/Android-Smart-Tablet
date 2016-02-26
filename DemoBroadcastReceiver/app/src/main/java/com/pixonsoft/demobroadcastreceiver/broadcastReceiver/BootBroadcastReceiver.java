package com.pixonsoft.demobroadcastreceiver.broadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.pixonsoft.demobroadcastreceiver.R;

/**
 * Created by mobile6 on 12/10/15.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle(intent.getStringExtra("TITULO"));
        builder.setContentText(intent.getStringExtra("DESCRICAO"));
        builder.setTicker(intent.getStringExtra("TICKER"));
        builder.setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(100, builder.build());




        Log.i("TESTE:", "BroadcartReceive worked");
        Toast.makeText(context, "BOOT Finalizado", Toast.LENGTH_LONG).show();
    }
}
