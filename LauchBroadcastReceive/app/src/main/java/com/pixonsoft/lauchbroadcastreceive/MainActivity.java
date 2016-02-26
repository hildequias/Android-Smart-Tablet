package com.pixonsoft.lauchbroadcastreceive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void executarApp(View view){

        Intent i = new Intent("com.example.pf0877.demobroadcastreceiver.intent.testebroadcast");
        i.putExtra("TITULO", "");
        i.putExtra("DESCRICAO", "Oi esse é um exemplo de notificação junto");
        i.putExtra("TICKER", "Você tem uma nova mensagem");
        sendBroadcast(i);
    }
}
