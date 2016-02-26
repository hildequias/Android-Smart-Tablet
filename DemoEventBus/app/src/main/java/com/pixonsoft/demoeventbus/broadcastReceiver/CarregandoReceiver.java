package com.pixonsoft.demoeventbus.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pixonsoft.demoeventbus.eventos.EventoCarregar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.greenrobot.event.EventBus;

/**
 * Created by mobile6 on 12/10/15.
 */
public class CarregandoReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        EventoCarregar eventoCarregar = null;

        DateFormat df = new SimpleDateFormat("%H:%M:%S");
        String date = df.format(Calendar.getInstance().getTime());

        if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
            eventoCarregar = new EventoCarregar("Descarregando desde ás "+ date);
        }

        EventBus.getDefault().post(eventoCarregar);
    }
}
