package com.pixonsoft.demolivraria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.pixonsoft.demolivraria.adapter.LivroAdapter;
import com.pixonsoft.demolivraria.dao.LivroDao;

public class ListaActivity extends AppCompatActivity {

    private ListView lvLivros;
    private LivroDao dao;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dao = new LivroDao(this);

    }

    @Override
    public void onResume(){
        super.onResume();

        lvLivros = (ListView) findViewById(R.id.lvLivros);
        lvLivros.setAdapter(new LivroAdapter(this, dao.getLivros()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        int id = menuItem.getItemId();

        switch (id){

            case R.id.mn_cadastrar:
                Intent i = new Intent(this, EditLivroActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
