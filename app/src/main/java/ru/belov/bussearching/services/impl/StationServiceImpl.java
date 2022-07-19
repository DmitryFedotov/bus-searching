package ru.belov.bussearching.services.impl;

import static ru.belov.bussearching.utils.Constants.COLUMN_ID;
import static ru.belov.bussearching.utils.Constants.COLUMN_NAME;
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

    public StationServiceImpl() {}

    public StationServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    public boolean update(Station station) {
        if (isEmpty(station)) {
            Log.e(LOG_TAG, "Невозможно обновить остановку, передан пустой аргумент.");
            return false;
        }
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, station.getName());
            db.update(STATIONS_TABLE, cv, COLUMN_ID + "=" + station.getId(), null);
            return true;
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка обновления остановки в базе данных.");
        } finally {
            dbHelper.close();
        }
        return false;
    }


    @Override
    public void create(Station object) {
        if (isEmpty(object)){
            throw new IllegalArgumentException("Название остановки не может быть пустым.");
        }
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, object.getName());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(STATIONS_TABLE, null, cv);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка сохранения остановки в базу данных.", e);
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public List<Station> findAll() {
        List<Station> result = new ArrayList<>();
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            @SuppressLint("Recycle")
            Cursor c = db.query(STATIONS_TABLE, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                int nameColIndex = c.getColumnIndex(COLUMN_NAME);
                int idColIndex = c.getColumnIndex(COLUMN_ID);

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

    @Override
    public void delete(long id) {
        if (isEmpty(id)){
            throw new IllegalArgumentException("ID остановки при удалении не может быть пустым.");
        }
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(STATIONS_TABLE, "id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка удаления остановки из базы данных.", e);
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public Station findByName(String name) {
        Station result = new Station();
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String select = "\"SELECT * FROM " + STATIONS_TABLE + "WHERE name=" + name + "\"";
            @SuppressLint("Recycle")
            Cursor c = db.query(STATIONS_TABLE, null, select, null, null, null, null);
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex(COLUMN_ID);
                int nameColIndex = c.getColumnIndex(COLUMN_NAME);
                result.setId(c.getInt(idColIndex));
                result.setName(c.getString(nameColIndex));
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

    @Override
    public List<Station> getStationsByName(List<Station> stations) {
        List<Station> result = new ArrayList<>();
        for (Station station :
             stations) {
            result.add(findByName(station.getName()));
        }
        return result;
    }
}
