package ru.belov.bussearching.services;

import java.util.List;

import ru.belov.bussearching.model.Station;

public interface BusService {

    /**
     * Сохранить маршрут.
     * @param name номер маршрута
     * @param stations список остановок
     */
    void save(String name, List<Station> stations);

    /**
     * Найти маршрут.
     * @param startPoint точка отправления
     * @param endPoint точка прибытия
     * @return номер маршрута
     */
    String findBus(String startPoint, String endPoint);
}
