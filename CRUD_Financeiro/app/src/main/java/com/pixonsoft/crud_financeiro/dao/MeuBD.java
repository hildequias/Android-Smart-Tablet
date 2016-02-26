package com.pixonsoft.crud_financeiro.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mobile6 on 11/26/15.
 */
public class MeuBD extends SQLiteOpenHelper {

    public static final String TABLE_FINANCES   = "finances";
    public static final String NOME_BD          = "BDFinances";
    public static final int VERSION_BD          = 1;

    public MeuBD(Context context) {
        super(context, NOME_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_FINANCES);
        sql.append("(");
        sql.append(FinancesDao.ID);
        sql.append(" integer primary key autoincrement, ");
        sql.append(FinancesDao.DESCRIPTION);
        sql.append(" text, ");
        sql.append(FinancesDao.VALUE);
        sql.append(" text, ");
        sql.append(FinancesDao.TYPE_FINANCE);
        sql.append(" text, ");
        sql.append(FinancesDao.DATE);
        sql.append(" text);");

        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINANCES);
        onCreate(db);
    }
}
