package com.pixonsoft.crud_financeiro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pixonsoft.crud_financeiro.model.Finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mobile6 on 11/26/15.
 */
public class FinancesDao {

    public static final String ID           = "_id";
    public static final String DESCRIPTION  = "description";
    public static final String VALUE        = "value";
    public static final String TYPE_FINANCE = "type_finance";
    public static final String DATE         = "date";

    private MeuBD meuBD;
    private SQLiteDatabase db;

    public FinancesDao(Context context){
        meuBD = new MeuBD(context);
    }

    public boolean salvar(Finance finance){

        if(finance.get_id() > 0)
            return this.atualizar(finance);
        else
            return this.inserir(finance);
    }

    protected boolean inserir(Finance finance){

        ContentValues valores = new ContentValues();

        valores.put(FinancesDao.DESCRIPTION, finance.getDescription());
        valores.put(FinancesDao.VALUE, finance.getValue());
        valores.put(FinancesDao.TYPE_FINANCE, finance.getType_finance());
        valores.put(FinancesDao.DATE, finance.getDate().toString());

        db = meuBD.getWritableDatabase();
        long resultado = db.insert(MeuBD.TABLE_FINANCES, null, valores);
        db.close();

        if(resultado == -1){
            return false; //"Erro ao cadastrar o livro";
        }else{
            return true; //"Livro cadastrado com sucesso";
        }
    }

    protected boolean atualizar(Finance finance){

        String WHERE = FinancesDao.ID + " = " + finance.get_id();

        ContentValues valores = new ContentValues();

        valores.put(FinancesDao.DESCRIPTION, finance.getDescription());
        valores.put(FinancesDao.VALUE, finance.getValue());
        valores.put(FinancesDao.TYPE_FINANCE, finance.getType_finance());
        valores.put(FinancesDao.DATE, finance.getDate().toString());

        db = meuBD.getWritableDatabase();
        long resultado = db.update(MeuBD.TABLE_FINANCES, valores, WHERE, null);
        db.close();

        if(resultado == -1){
            return false; //"Erro ao cadastrar o livro";
        }else{
            return true; //"Livro cadastrado com sucesso";
        }
    }

    public List<Finance> getFinances() throws ParseException {

        List<Finance> finances = new ArrayList<>();

        Cursor cursor;

        db = meuBD.getReadableDatabase();

        cursor = db.query(MeuBD.TABLE_FINANCES, null, null, null, null, null, null);
        if(cursor != null){

            cursor.moveToFirst();

            while (cursor.moveToNext()){

                Finance finance = new Finance();

                finance.set_id(cursor.getInt(cursor.getColumnIndex(ID)));
                finance.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                finance.setValue(cursor.getDouble(cursor.getColumnIndex(VALUE)));
                finance.setType_finance(cursor.getString(cursor.getColumnIndex(TYPE_FINANCE)));
                finance.setDate(cursor.getString(cursor.getColumnIndex(DATE)));

                finances.add(finance);
            }
        }
        return finances;
    }
}
