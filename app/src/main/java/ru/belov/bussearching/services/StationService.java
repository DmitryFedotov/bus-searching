package ru.belov.bussearching.services;

import java.util.List;

import ru.belov.bussearching.model.Station;

public interface StationService {

    /**
     * Сохранить остановку.
     * @param name название остановки
     */
    void save(String name);

    /**
     * Получить список всех остановок.
     * @return
     */
    List<Station> getAllStations();
}
