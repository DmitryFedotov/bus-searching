package ru.belov.bussearching.db;

import static ru.belov.bussearching.utils.Constants.BUSES_STATIONS_TABLE;
import static ru.belov.bussearching.utils.Constants.BUSES_TABLE;
import static ru.belov.bussearching.utils.Constants.DB_NAME;
import static ru.belov.bussearching.utils.Constants.DB_VERSION;
import static ru.belov.bussearching.utils.Constants.STATIONS_TABLE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {



    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с остановками
        db.execSQL("CREATE TABLE " + STATIONS_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "name TEXT"
                +  ");");
        //создаем таблицу с маршрутами
        db.execSQL("CREATE TABLE " + BUSES_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "name TEXT"
                +  ");");
        //создаем результирующую таблицу
        db.execSQL("CREATE TABLE " + BUSES_STATIONS_TABLE + " ("
                + "busId INTEGER NOT NULL,"
                + "stationId INTEGER NOT NULL,"
                + "FOREIGN KEY (busId) REFERENCES " + BUSES_TABLE + "(id),"
                + "FOREIGN KEY (stationId) REFERENCES " + STATIONS_TABLE + "(id)"
                + "ON DELETE CASCADE"
                +  ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
