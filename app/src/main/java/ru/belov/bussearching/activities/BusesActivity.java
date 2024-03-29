package ru.belov.bussearching.activities;

import static ru.belov.bussearching.utils.EmptinessUtils.isNotEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private LinearLayout createBusLayout;
    private LinearLayout stationsLayout;
    private LinearLayout horizontalLayoutStations;
    private LinearLayout navigationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses);

        busService = new BusServiceImpl(this);
        busesList = findViewById(R.id.listViewBuses);
        busesList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        createBusLayout = findViewById(R.id.createBusLayout);
        stationsLayout = findViewById(R.id.linerLayoutBuses);
        horizontalLayoutStations = findViewById(R.id.horizontalLayoutStations);
        navigationLayout = findViewById(R.id.navigationLayout);

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
                busesNames.add(bus.getName() + bus.stationsToString());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, busesNames);
        busesList.setAdapter(adapter);
    }

    public void createBusButtonClick(View view) {
        ListView stationsList = findViewById(R.id.listViewStationsAdd);
        stationsList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        List<String> stationsNames = new ArrayList<>();
        StationService stationService = new StationServiceImpl(this);
        List<Station> stations = stationService.findAll();
        for(Station station : stations){
            stationsNames.add(station.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, stationsNames);
        stationsList.setAdapter(adapter);
        createBusLayout.setVisibility(View.VISIBLE);
        stationsLayout.setVisibility(View.INVISIBLE);
        horizontalLayoutStations.setVisibility(View.INVISIBLE);
        navigationLayout.setVisibility(View.INVISIBLE);
    }

    public void saveBusButtonClick(View view) {
        ListView stationsList = findViewById(R.id.listViewStationsAdd);
        TextInputEditText inputEditText = findViewById(R.id.textInputBusName);
        if (isNotEmpty(Objects.requireNonNull(inputEditText.getText()).toString())
                && stationsList.getCheckedItemCount() >= 2) {
            List<Station> stations = new ArrayList<>();
            StationService stationService = new StationServiceImpl(this);
            SparseBooleanArray sp = stationsList.getCheckedItemPositions();
            for (int i = 0; i < sp.size(); i++) {
                int key = sp.keyAt(i);
                if (sp.get(key)) {
                    stations.add(stationService.findByName(stationsList.getItemAtPosition(key).toString()));
                }
            }
            busService.create(new Bus(inputEditText.getText().toString(), stations));
            updateListView();
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e){
            Log.d(this.getClass().toString(), "Ошибка закрытия клавиатуры, клавиатура не найдена.");
        }
        createBusLayout.setVisibility(View.INVISIBLE);
        stationsLayout.setVisibility(View.VISIBLE);
        horizontalLayoutStations.setVisibility(View.VISIBLE);
        navigationLayout.setVisibility(View.VISIBLE);
        inputEditText.getText().clear();
    }

    public void deleteBusButtonClick(View view) {
        if (busesList.getCheckedItemPosition() != -1) {
            String searchBus = busesList.getItemAtPosition(busesList.getCheckedItemPosition()).toString();
            searchBus = searchBus.substring(0, searchBus.indexOf("(") - 1);
            Bus deleteBus = busService.findByName(searchBus);
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