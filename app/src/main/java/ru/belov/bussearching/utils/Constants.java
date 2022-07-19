package ru.belov.bussearching.utils;

public final class Constants {

    private Constants (){
        //Utility
    }

    /**
     * Колонки таблиц.
     */
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BUS_ID = "busId";
    public static final String COLUMN_STATION_ID = "stationId";

    /**
     * Название таблицы остановок.
     */
    public static final String STATIONS_TABLE = "Stations";

    /**
     * Название таблицы маршрутов.
     */
    public static final String BUSES_TABLE = "Buses";

    /**
     * Название результирующей таблицы.
     */
    public static final String BUSES_STATIONS_TABLE = "BusesStations";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "BusSearchingDB";
}
