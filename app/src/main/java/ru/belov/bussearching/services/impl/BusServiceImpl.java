package ru.belov.bussearching.services.impl;

import static ru.belov.bussearching.utils.Constants.BUSES_STATIONS_TABLE;
import static ru.belov.bussearching.utils.Constants.BUSES_TABLE;
import static ru.belov.bussearching.utils.Constants.COLUMN_BUS_ID;
import static ru.belov.bussearching.utils.Constants.COLUMN_ID;
import static ru.belov.bussearching.utils.Constants.COLUMN_NAME;
import static ru.belov.bussearching.utils.Constants.COLUMN_STATION_ID;
import static ru.belov.bussearching.utils.Constants.STATIONS_TABLE;
import static ru.belov.bussearching.utils.EmptinessUtils.isEmpty;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import ru.belov.bussearching.db.DbHelper;
import ru.belov.bussearching.model.Bus;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.BusService;

public class BusServiceImpl implements BusService {

    private final String LOG_TAG = BusServiceImpl.class.toString();
    private DbHelper dbHelper;

    public BusServiceImpl(){}


    public BusServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
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
            for (Station station : object.getStations()) {
                cv.clear();
                cv.put(COLUMN_BUS_ID, object.getId());
                cv.put(COLUMN_STATION_ID, station.getId());
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
        return null;
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
            String select = "\"SELECT * FROM " + BUSES_STATIONS_TABLE + " WHERE stationId=" + startPoint.getId()
                    + " AND busId=(SELECT busId FROM " + BUSES_STATIONS_TABLE + " WHERE stationId=" + endPoint.getId() + ")\"";
            Cursor c = db.query(STATIONS_TABLE, null, select, null, null, null, null);
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex(COLUMN_ID);
                int nameColIndex = c.getColumnIndex(COLUMN_NAME);
                result.setId(c.getInt(idColIndex));
                result.setName(c.getString(nameColIndex));
            } else {
                Log.i(LOG_TAG, "Маршрут с такой точкой отправления и прибытия не найден.");
            }
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Ошибка поиска маршрута в базе данных.", e);
        } finally {
            dbHelper.close();
        }
        return result;
    }
}
