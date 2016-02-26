package com.pixonsoft.demoeventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pixonsoft.demoeventbus.eventos.EventoCarregar;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tvStatus);

        EventBus.getDefault().register(this);
    }

    public void onEvent(EventoCarregar event){
        tvStatus.setText(tvStatus.getText() + "\n" + event.getData());
    }

    protected void onDestroy(){
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
