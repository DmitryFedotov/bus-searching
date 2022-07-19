package ru.belov.bussearching.services;

import java.util.List;

public interface CrudService<T> {

    /**
     * Сохранить объект.
     * @param object объект
     */
    void create(T object);

    /**
     * Возвращает полный список объектов данного класса.
     *
     * @return список сущностей
     */
    List<T> findAll();

    /**
     * Удаляет объект с указанным идентификатором.
     *
     * @param id идентификатор объекта
     */
    void delete(long id);
}
