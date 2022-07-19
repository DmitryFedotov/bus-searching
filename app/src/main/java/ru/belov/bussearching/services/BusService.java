package ru.belov.bussearching.services;

import ru.belov.bussearching.model.Bus;
import ru.belov.bussearching.model.Station;


public interface BusService extends CrudService<Bus>{

    /**
     * Поиск маршрута.
     * @param startPoint точка отправления
     * @param endPoint точка прибытия
     * @return маршрут
     */
    Bus findBus(Station startPoint, Station endPoint);
}
