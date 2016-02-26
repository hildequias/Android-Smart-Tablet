package com.pixonsoft.democomponenteandroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pixonsoft.democomponenteandroid.adapter.ContatosAdapter;
import com.pixonsoft.democomponenteandroid.domain.Contato;

import java.util.ArrayList;
import java.util.List;

public class ListaContatosActivity extends AppCompatActivity {

    private ListView lvContatos;
    private List<Contato> contatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);

        lvContatos = (ListView) findViewById(R.id.lvContatos);
        Cursor phones = getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, null);

        if(phones.getCount() > 0){

            contatos = new ArrayList<>();
            String nome     = "";
            String telefone = "";

            phones.moveToFirst();

            do{

                nome = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ));

                telefone = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ));

                Contato contato = new Contato();
                contato.setNome(nome);
                contato.setTelefone(telefone);
                contatos.add(contato);

            }while (phones.moveToNext());

            lvContatos.setAdapter(new ContatosAdapter(this, contatos));
            lvContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent();
                    i.putExtra("telefone", contatos.get(position).getTelefone());
                    setResult(RESULT_OK, i);
                    finish();
                }
            });
        }
    }

}
