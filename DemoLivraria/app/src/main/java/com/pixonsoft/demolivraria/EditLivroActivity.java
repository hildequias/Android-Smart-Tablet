package com.pixonsoft.demolivraria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pixonsoft.demolivraria.dao.LivroDao;
import com.pixonsoft.demolivraria.model.Livro;

public class EditLivroActivity extends AppCompatActivity {

    private EditText titulo;
    private EditText autor;
    private EditText isbn;
    private EditText editora;
    private EditText descricao;
    private Button btSave;
    private LivroDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_livro);

        titulo = (EditText) findViewById(R.id.etTitulo);
        autor = (EditText) findViewById(R.id.etAutor);
        isbn = (EditText) findViewById(R.id.etIsbn);
        editora = (EditText) findViewById(R.id.etEditora);
        descricao = (EditText) findViewById(R.id.etDescricao);

        dao = new LivroDao(this);

        btSave = (Button) findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();
            }
        });
    }

    private void saveData() {

        Livro livro = new Livro();
        livro.setTitulo(titulo.getText().toString());
        livro.setAutor(autor.getText().toString());
        livro.setIsbn(isbn.getText().toString());
        livro.setEditora(editora.getText().toString());
        livro.setDescricao(descricao.getText().toString());

        dao.inserir(livro);
        finish();
    }
}
