package com.pixonsoft.crud_financeiro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.pixonsoft.crud_financeiro.dao.FinancesDao;
import com.pixonsoft.crud_financeiro.model.Finance;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mobile6 on 1/26/16.
 */
public class EditActivity extends AppCompatActivity {

    Button btnSave;
    EditText etDescription;
    EditText etValue;
    CalendarView calendarView;
    Spinner spinner;

    FinancesDao dao;
    Finance finance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dao = new FinancesDao(this);

        etDescription   = (EditText) findViewById(R.id.description);
        etValue         = (EditText) findViewById(R.id.value);
        calendarView    = (CalendarView) findViewById(R.id.calendarView);
        spinner         = (Spinner) findViewById(R.id.spinner);
        btnSave         = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                finance = new Finance();
                finance.setDescription(etDescription.getText().toString());
                finance.setValue(Double.valueOf(etValue.getText().toString()));
                finance.setDate(sdf.format(new Date(calendarView.getDate())));
                finance.setType_finance(spinner.getSelectedItem().toString());

                if(dao.salvar(finance))
                    finish();
            }
        });
    }
}
