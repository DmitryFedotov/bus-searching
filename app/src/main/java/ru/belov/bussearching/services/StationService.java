package ru.belov.bussearching.services;


import java.util.List;

import ru.belov.bussearching.model.Station;

public interface StationService extends CrudService<Station>{

    /**
     * Получить заполненный список остановок {@link Station} по маршруту {@link ru.belov.bussearching.model.Bus}.
     * @param busId id маршрута
     * @return заполненный список остановок
     */
    List<Station> getStationsByBusId(long busId);
}
