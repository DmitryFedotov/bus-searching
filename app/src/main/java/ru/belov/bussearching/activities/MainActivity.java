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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        EditText editTextStartPoint = findViewById(R.id.textViewStartPoint);
        EditText editTextEndPoint = findViewById(R.id.textViewEndPoint);
        TextView result = findViewById(R.id.textViewResult);
        result.setVisibility(View.INVISIBLE);
        if (isNotEmpty(editTextStartPoint.getText().toString())
                && isNotEmpty(editTextEndPoint.getText().toString())) {
            StationService stationService = new StationServiceImpl(this);
            Station startPoint = stationService.findByName(editTextStartPoint.getText().toString());
            Station endPoint = stationService.findByName(editTextEndPoint.getText().toString());
            if (isNotEmpty(startPoint)) {
                if (isNotEmpty(endPoint)) {
                    BusService busService = new BusServiceImpl(this);
                    Bus bus = busService.findBus(startPoint, endPoint);
                    if (isNotEmpty(bus)) {
                        String success = "Маршрут: " + bus.getName();
                        result.setText(success);
                    } else {
                        result.setText("Такой маршрут не найден!");
                    }
                    result.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Такой конечной точки нет в БД", Toast.LENGTH_SHORT);
                }
            } else {
                Toast.makeText(this, "Такой точки отправления нет в БД", Toast.LENGTH_SHORT);
            }
        }
    }
}