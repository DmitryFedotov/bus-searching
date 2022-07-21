package ru.belov.bussearching.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bus {

    private long id;
    private String name;
    private List<Station> stations = new ArrayList<>();

    public Bus() {}

    public Bus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Bus(String name, List<Station> stations) {
        this.name = name;
        this.stations = stations;
    }

    public Bus(int id, String name, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String stationsToString() {
        if (stations.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" ( ");
        for(int i = 0; i < stations.size(); i++) {
            builder.append(stations.get(i).getName());
            if (stations.size() > 1 && stations.size() - 1 != i ) {
                builder.append(" -> ");
            }
        }
        builder.append(")");
        return builder.toString();
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
