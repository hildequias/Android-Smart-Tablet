package com.pixonsoft.carroapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pixonsoft.carroapp.adapter.TabsAdapter;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(R.string.tab_classicos));
        tabs.addTab(tabs.newTab().setText(R.string.tab_esportivos));
        tabs.addTab(tabs.newTab().setText(R.string.tab_luxuosos));

        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        pager = (ViewPager) findViewById(R.id.pager);

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        pager.setAdapter(tabsAdapter);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
                Toast.makeText(this, "Cadastrar", Toast.LENGTH_LONG).show();
                break;

            case R.id.mn_configuracao:
                Toast.makeText(this, "Configurar", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
