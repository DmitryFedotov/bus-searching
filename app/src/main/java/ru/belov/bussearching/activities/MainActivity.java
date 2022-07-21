package ru.belov.bussearching.activities;

import static ru.belov.bussearching.utils.EmptinessUtils.isNotEmpty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.belov.bussearching.R;
import ru.belov.bussearching.model.Bus;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.BusService;
import ru.belov.bussearching.services.StationService;
import ru.belov.bussearching.services.impl.BusServiceImpl;
import ru.belov.bussearching.services.impl.StationServiceImpl;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerStartPoint;
    private Spinner spinnerEndPoint;
    private StationService stationService;
    private Station startPoint;
    private Station endPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stationService = new StationServiceImpl(this);
        spinnerStartPoint = findViewById(R.id.spinnerStartPoint);
        spinnerEndPoint = findViewById(R.id.spinnerEndPoint);
        updateSpinners();
    }

    /**
     * Update Spinners.
     */
    private void updateSpinners() {
        List<String> stationsNames = new ArrayList<>();
        List<Station> stations = stationService.findAll();
        for(Station station : stations){
            stationsNames.add(station.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stationsNames);
        spinnerStartPoint.setAdapter(adapter);
        spinnerEndPoint.setAdapter(adapter);

        AdapterView.OnItemSelectedListener startItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                startPoint = stationService.findByName(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        AdapterView.OnItemSelectedListener endItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                endPoint = stationService.findByName(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerStartPoint.setOnItemSelectedListener(startItemSelectedListener);
        spinnerEndPoint.setOnItemSelectedListener(endItemSelectedListener);
    }

    public void navigateStationsButtonClick(View view) {
        Intent intent = new Intent(this, StationsActivity.class);
        startActivity(intent);
    }

    public void navigateBusesButtonClick(View view) {
        Intent intent = new Intent(this, BusesActivity.class);
        startActivity(intent);
    }

    @SuppressLint("ShowToast")
    public void findBusButtonClick(View view) {
        TextView result = findViewById(R.id.textViewResult);
        result.setVisibility(View.INVISIBLE);
        if (isNotEmpty(startPoint) && isNotEmpty(endPoint)) {
            if (!startPoint.equals(endPoint)) {
                BusService busService = new BusServiceImpl(this);
                Bus bus = busService.findBus(startPoint, endPoint);
                if (isNotEmpty(bus)) {
                    String success = "Маршрут: " + bus.getName();
                    result.setText(success);
                } else {
                    result.setText("Такой маршрут не найден!");
                }
            } else {
                result.setText("Выберите разные остановки!");
            }
            result.setVisibility(View.VISIBLE);
        }
    }
}