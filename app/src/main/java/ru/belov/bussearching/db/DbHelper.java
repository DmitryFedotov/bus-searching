package ru.belov.bussearching.db;

import static ru.belov.bussearching.utils.Constants.STATIONS_TABLE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с остановками
        db.execSQL("create table " + STATIONS_TABLE + " ("
                + "id integer primary key autoincrement,"
                + "name text"
                +  ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
