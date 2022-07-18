package ru.belov.bussearching.services.impl;

import static ru.belov.bussearching.utils.Constants.NAME;
import static ru.belov.bussearching.utils.Constants.STATIONS_TABLE;
import static ru.belov.bussearching.utils.EmptinessUtils.isEmpty;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.belov.bussearching.db.DbHelper;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.StationService;

public class StationServiceImpl implements StationService {

    private final String LOG_TAG = StationServiceImpl.class.toString();
    private DbHelper dbHelper;

    @Inject
    public StationServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void save(String name) {
        if (isEmpty(name)){
            throw new IllegalArgumentException("Название остановки не может быть пустым.");
        }
        try {
            ContentValues cv = new ContentValues();
            cv.put(NAME, name);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(STATIONS_TABLE, null, cv);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка сохранения маршрута в базу данных.", e);
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public List<Station> getAllStations() {
        List<Station> result = new ArrayList<>();
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            @SuppressLint("Recycle")
            Cursor c = db.query(STATIONS_TABLE, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                int nameColIndex = c.getColumnIndex("name");
                int idColIndex = c.getColumnIndex("id");

                do {
                    result.add(new Station(c.getInt(idColIndex), c.getString(nameColIndex)));
                } while (c.moveToNext());
            } else {
                Log.i(LOG_TAG, "Пустая таблица остановок.");
            }
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка получения списка остановок из базы данных.", e);
        } finally {
            dbHelper.close();
        }
        return result;
    }
}
