package com.pixonsoft.demolivraria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private final String PREF_NAME = "LIVRARIA";
    private final String MANTER_CONECTADO = "MANTERCONECTADO";

    private TextInputLayout tlNome;
    private TextInputLayout tlSenha;
    private CheckBox cbManterConectado;
    private Button btEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isLogado()){
            iniciarApp();
        }

        tlNome = (TextInputLayout) findViewById(R.id.tlNomeID);
        tlSenha = (TextInputLayout) findViewById(R.id.tlPassword);
        cbManterConectado = (CheckBox) findViewById(R.id.ckManterConectado);
        btEntrar = (Button) findViewById(R.id.btEntrar);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(doLogin()){
                    iniciarApp();
                }else{
                    tlSenha.setError("Usuário ou senha inválido!");
                }
            }
        });
    }

    private void iniciarApp(){
        Intent i = new Intent(LoginActivity.this, ListaActivity.class);
        startActivity(i);
        finish();
    }

    private boolean isLogado(){

        SharedPreferences settings = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return settings.getBoolean(MANTER_CONECTADO, false);
    }

    private void manterConectado(){

        SharedPreferences settings = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(MANTER_CONECTADO, cbManterConectado.isChecked());
        editor.commit();
    }

    private boolean doLogin(){

        boolean valido = false;
        String usuario = tlNome.getEditText().getText().toString();
        String senha = tlSenha.getEditText().getText().toString();

        if(usuario.equals("admin") && senha.equals("123")){
            valido = true;
        }
        return valido;
    }
}
