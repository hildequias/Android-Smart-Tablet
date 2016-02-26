package com.pixonsoft.demobroadcastreceiver.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by mobile6 on 12/10/15.
 */
public class MeuBroadcast extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "TESTE de BROADCAST_RECEIVE", Toast.LENGTH_SHORT).show();
    }
}
