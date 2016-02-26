package com.pixonsoft.crud_financeiro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pixonsoft.crud_financeiro.adapter.FinanceAdapter;
import com.pixonsoft.crud_financeiro.dao.FinancesDao;
import com.pixonsoft.crud_financeiro.model.Finance;

import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView;
    FinancesDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textView     = (TextView) findViewById(R.id.textView);

        dao = new FinancesDao(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), EditActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        try {

            List<Finance> finances = dao.getFinances();

            if (finances.size() > 0)
                textView.setVisibility(View.GONE);

            recyclerView.setAdapter(new FinanceAdapter(this, finances));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
