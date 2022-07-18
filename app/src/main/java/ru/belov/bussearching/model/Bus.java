package ru.belov.bussearching.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bus {

    private int id;
    private String name;
    private List<Station> stations = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bus bus = (Bus) o;
        return id == bus.id && name.equals(bus.name) && stations.equals(bus.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stations);
    }

    @NonNull
    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stations=" + stations +
                '}';
    }
}
