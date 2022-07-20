package ru.belov.bussearching.services.impl;

import static ru.belov.bussearching.utils.Constants.BUSES_STATIONS_TABLE;
import static ru.belov.bussearching.utils.Constants.BUSES_TABLE;
import static ru.belov.bussearching.utils.Constants.COLUMN_BUS_ID;
import static ru.belov.bussearching.utils.Constants.COLUMN_ID;
import static ru.belov.bussearching.utils.Constants.COLUMN_NAME;
import static ru.belov.bussearching.utils.Constants.COLUMN_STATION_ID;
import static ru.belov.bussearching.utils.Constants.STATIONS_TABLE;
import static ru.belov.bussearching.utils.EmptinessUtils.isEmpty;
import static ru.belov.bussearching.utils.EmptinessUtils.isNotEmpty;

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
import ru.belov.bussearching.model.Bus;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.BusService;

public class BusServiceImpl implements BusService {

    private final String LOG_TAG = BusServiceImpl.class.toString();
    private DbHelper dbHelper;

    public BusServiceImpl(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    @Override
    public Bus findByName(String name) {
        Bus result = new Bus();
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String select = "SELECT * FROM " + BUSES_TABLE + " WHERE name=?";
            @SuppressLint("Recycle")
            Cursor c = db.rawQuery(select, new String[]{ name });
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex(COLUMN_ID);
                int nameColIndex = c.getColumnIndex(COLUMN_NAME);
                result.setId(c.getInt(idColIndex));
                result.setName(c.getString(nameColIndex));
            } else {
                Log.i(LOG_TAG, "Пустая таблица маршрутов.");
            }
            c.close();
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка получения списка маршрутов из базы данных.", e);
        } finally {
            dbHelper.close();
        }
        return result;
    }

    @Override
    public void create(Bus object) {
        if (isEmpty(object)){
            throw new IllegalArgumentException("Название маршрута не может быть пустым.");
        }
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, object.getName());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(BUSES_TABLE, null, cv);
            dbHelper.close();
            createBusStation(object);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка сохранения маршрута в базу данных.", e);
        } finally {
            dbHelper.close();
        }
    }

    private void createBusStation(Bus object) {
        try {
            ContentValues cv = new ContentValues();
            Bus bus = findByName(object.getName());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            for (Station station : object.getStations()) {
                cv.clear();
                cv.put(COLUMN_BUS_ID, bus.getId());
                cv.put(COLUMN_STATION_ID, station.getId());
                Log.i(LOG_TAG, "insert bus_station = " + cv);
                db.insert(BUSES_STATIONS_TABLE, null, cv);
            }
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка сохранения маршрута в базу данных.", e);
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public List<Bus> findAll() {
        List<Bus> result = new ArrayList<>();
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            @SuppressLint("Recycle")
            Cursor c = db.query(BUSES_TABLE, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                int nameColIndex = c.getColumnIndex(COLUMN_NAME);
                int idColIndex = c.getColumnIndex(COLUMN_ID);
                do {
                    result.add(new Bus(c.getInt(idColIndex), c.getString(nameColIndex)));
                } while (c.moveToNext());
            } else {
                Log.i(LOG_TAG, "Пустая таблица маршрутов.");
            }
            c.close();
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка получения списка маршрутов из базы данных.", e);
        } finally {
            dbHelper.close();
        }
        return result;
    }

    @Override
    public void delete(long id) {
        if (isEmpty(id)){
            throw new IllegalArgumentException("ID маршрута при удалении не может быть пустым.");
        }
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(BUSES_TABLE, "id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка удаления маршрута из базы данных.", e);
        } finally {
            dbHelper.close();
        }
    }

    @Override
    public Bus findBus(Station startPoint, Station endPoint) {
        if (isEmpty(startPoint) || isEmpty(endPoint)) {
            throw new IllegalArgumentException("Для получения маршрута необходимо передать точку отправления и точку прибытия");
        }
        Bus result = new Bus();
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String select = "SELECT * FROM " + BUSES_STATIONS_TABLE + " AS t1, " + BUSES_STATIONS_TABLE + " AS t2"
                    //+ " JOIN " + BUSES_STATIONS_TABLE + " AS t2 ON t2.busId = t1.busId"
                    + " WHERE t2.busId = t1.busId and t1.stationId = ? AND t2.stationId = ?";
            @SuppressLint("Recycle")
            Cursor cursor = db.query(BUSES_STATIONS_TABLE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int nameColIndex = cursor.getColumnIndex(COLUMN_STATION_ID);
                int idColIndex = cursor.getColumnIndex(COLUMN_BUS_ID);
                do {
                   Log.i(LOG_TAG, "busId = " + cursor.getInt(idColIndex) + " stationId = " + cursor.getString(nameColIndex));
                } while (cursor.moveToNext());
            } else {
                Log.i(LOG_TAG, "Пустая таблица маршрутов с остановками.");
            }
            Log.i(LOG_TAG, select);
            @SuppressLint("Recycle")
            Cursor c = db.rawQuery(select, new String[] {String.valueOf(startPoint.getId()), String.valueOf(endPoint.getId())});
            Log.i(LOG_TAG, "Station1.getId() = " + startPoint.getId() + " getName() = " + startPoint.getName());
            Log.i(LOG_TAG, "Station2.getId() = " + endPoint.getId() + " getName() = " + endPoint.getName());
            if (c.moveToFirst() && isNotEmpty(c.getCount())) {
                int idColIndex = c.getColumnIndex(COLUMN_ID);
                int nameColIndex = c.getColumnIndex(COLUMN_NAME);
                result.setId(c.getInt(idColIndex));
                result.setName(c.getString(nameColIndex));
            } else {
                Log.i(LOG_TAG, "Маршрут с такой точкой отправления и прибытия не найден.");
            }
            c.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Ошибка поиска маршрута в базе данных.", e);
        } finally {
            dbHelper.close();
        }
        return result;
    }
}
