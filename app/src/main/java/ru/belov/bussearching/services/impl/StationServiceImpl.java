package ru.belov.bussearching.services.impl;

import static ru.belov.bussearching.utils.Constants.BUSES_STATIONS_TABLE;
import static ru.belov.bussearching.utils.Constants.COLUMN_ID;
import static ru.belov.bussearching.utils.Constants.COLUMN_NAME;
import static ru.belov.bussearching.utils.Constants.COLUMN_STATION_ID;
import static ru.belov.bussearching.utils.Constants.SELECT_FROM;
import static ru.belov.bussearching.utils.Constants.STATIONS_TABLE;
import static ru.belov.bussearching.utils.EmptinessUtils.isEmpty;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.belov.bussearching.db.DbHelper;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.StationService;

public class StationServiceImpl implements StationService {

    private final String LOG_TAG = StationServiceImpl.class.toString();
    private DbHelper dbHelper;

    public StationServiceImpl(Context context) {
        this.dbHelper = new DbHelper(context);
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
            String select = SELECT_FROM + STATIONS_TABLE + " WHERE name=?";
            @SuppressLint("Recycle")
            Cursor c = db.rawQuery(select, new String[]{ name });
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
    public List<Station> getStationsByBusId(long busId) {
        List<Station> result = new ArrayList<>();
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String select = SELECT_FROM + BUSES_STATIONS_TABLE + " WHERE busId = ?";
            Cursor c = db.rawQuery(select, new String[] { String.valueOf(busId) });
            if (c.moveToFirst()) {
                int stationIdColIndex = c.getColumnIndex(COLUMN_STATION_ID);
                do {
                    result.add(findById(c.getInt(stationIdColIndex)));
                } while (c.moveToNext());
            } else {
                Log.i(LOG_TAG, "Пустая таблица остановок.");
            }
            c.close();
        } catch (Exception e){
            Log.e(LOG_TAG, "Ошибка получения списка остановок по маршруту.");
        } finally {
            dbHelper.close();
        }
        return result;
    }

    @Override
    public Station findById(long id) {
        Station result = new Station();
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String select = SELECT_FROM + STATIONS_TABLE + " WHERE id=?";
            @SuppressLint("Recycle")
            Cursor c = db.rawQuery(select, new String[]{ String.valueOf(id) });
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
}
