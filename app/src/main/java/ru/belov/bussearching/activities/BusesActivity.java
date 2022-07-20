package ru.belov.bussearching.activities;

import static ru.belov.bussearching.utils.EmptinessUtils.isNotEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import ru.belov.bussearching.R;
import ru.belov.bussearching.model.Bus;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.BusService;
import ru.belov.bussearching.services.StationService;
import ru.belov.bussearching.services.impl.BusServiceImpl;
import ru.belov.bussearching.services.impl.StationServiceImpl;

public class BusesActivity extends AppCompatActivity {

    private BusService busService;
    ListView busesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);

        busService = new BusServiceImpl(this);
        busesList = findViewById(R.id.listViewBuses);
        busesList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        updateListView();
    }

    /**
     * Обновить ListView.
     */
    private void updateListView() {
        List<String> busesNames = new ArrayList<>();
        List<Bus> buses = busService.findAll();
        if (isNotEmpty(buses)) {
            for (Bus bus : buses) {
                busesNames.add(bus.getName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, busesNames);
        busesList.setAdapter(adapter);
    }

    public void createBusButtonClick(View view) {
        LinearLayout layout = findViewById(R.id.createBusLayout);
        ListView stationsList = findViewById(R.id.listViewStationsAdd);
        stationsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        List<String> stationsNames = new ArrayList<>();
        StationService stationService = new StationServiceImpl(this);
        List<Station> stations = stationService.findAll();
        for(Station station : stations){
            stationsNames.add(station.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, stationsNames);
        stationsList.setAdapter(adapter);
        layout.setVisibility(View.VISIBLE);
    }

    @SuppressLint("ShowToast")
    public void saveBusButtonClick(View view) {
        ListView stationsList = findViewById(R.id.listViewStationsAdd);
        TextInputEditText inputEditText = findViewById(R.id.textInputBusName);
        if (isNotEmpty(inputEditText) && stationsList.getCheckedItemCount() >= 2) {
            List<Station> stations = new ArrayList<>();
            StationService stationService = new StationServiceImpl(this);
            SparseBooleanArray sp = stationsList.getCheckedItemPositions();
            for (int i = 0; i < sp.size(); i++) {
                if (sp.valueAt(i)) {
                    stations.add(stationService.findByName(stationsList.getItemAtPosition(i).toString()));
                }
            }
            busService.create(new Bus(inputEditText.getText().toString(), stations));
            updateListView();
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            LinearLayout layout = findViewById(R.id.createBusLayout);
            layout.setVisibility(View.INVISIBLE);
            inputEditText.getText().clear();
        } else {
            Toast.makeText(this, "Заполните название маршрута и выберите не менее 2 остановок!", Toast.LENGTH_SHORT);
        }
    }

    public void deleteBusButtonClick(View view) {
        if (isNotEmpty(busesList.getCheckedItemPosition())) {
            Bus deleteBus = busService.findByName(busesList.getItemAtPosition(busesList.getCheckedItemPosition()).toString());
            busService.delete(deleteBus.getId());
            updateListView();
        }
    }

    public void navigateSearchBusButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void navigateStationsButtonClick(View view) {
        Intent intent = new Intent(this, StationsActivity.class);
        startActivity(intent);
    }
}