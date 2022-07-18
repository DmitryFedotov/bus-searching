package ru.belov.bussearching.services.impl;

import static ru.belov.bussearching.utils.Constants.NAME;
import static ru.belov.bussearching.utils.Constants.STATIONS_TABLE;
import static ru.belov.bussearching.utils.EmptinessUtils.isEmpty;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import ru.belov.bussearching.db.DbHelper;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.BusService;

public class BusServiceImpl implements BusService {

    private final String LOG_TAG = BusServiceImpl.class.toString();
    private DbHelper dbHelper;

    @Inject
    public BusServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void save(String name, List<Station> stations) {
        if (isEmpty(name)){
            throw new IllegalArgumentException("Название маршрута не может быть пустым.");
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
    public String findBus(String startPoint, String endPoint) {
        return null;
    }
}
