package ru.belov.bussearching.services;


import java.util.List;

import ru.belov.bussearching.model.Station;

public interface StationService extends CrudService<Station>{

    /**
     * Найти остановку по названию.
     * @param name название остановки
     * @return объект остановки
     */
    Station findByName(String name);

    /**
     * Получить заполненный список {@link Station} по списку {@link Station} у которых заполнено только название.
     * @param stations список остановок с заполненными названиями
     * @return список из БД
     */
    List<Station> getStationsByName(List<Station> stations);
}
