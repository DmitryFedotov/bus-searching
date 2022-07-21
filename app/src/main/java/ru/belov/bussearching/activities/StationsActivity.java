package ru.belov.bussearching.activities;

import static ru.belov.bussearching.utils.EmptinessUtils.isNotEmpty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.belov.bussearching.R;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.StationService;
import ru.belov.bussearching.services.impl.StationServiceImpl;

public class StationsActivity extends AppCompatActivity {

    private StationService stationService;
    private ListView stationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);

        stationsList = findViewById(R.id.listViewBuses);
        stationsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        stationService = new StationServiceImpl(this);
        updateListView();
    }

    /**
     * Обновить ListView.
     */
    private void updateListView() {
        List<String> stationsNames = new ArrayList<>();
        List<Station> stations = stationService.findAll();
        for(Station station : stations){
            stationsNames.add(station.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, stationsNames);
        stationsList.setAdapter(adapter);
    }

    public void createStationButtonClick(View view) {
        LinearLayout layout = findViewById(R.id.createBusLayout);
        layout.setVisibility(View.VISIBLE);
    }

    public void saveStationButtonClick(View view) {
        TextInputEditText inputEditText = findViewById(R.id.textInputBusName);
        if (isNotEmpty(Objects.requireNonNull(inputEditText.getText()).toString())) {
            stationService.create(new Station(inputEditText.getText().toString()));
        }
        updateListView();
        try {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e){
            Log.d(this.getClass().toString(), "Ошибка закрытия клавиатуры, клавиатура не найдена.");
        }
        LinearLayout layout = findViewById(R.id.createBusLayout);
        layout.setVisibility(View.INVISIBLE);
        inputEditText.getText().clear();
    }

    public void deleteStationButtonClick(View view) {
        if (isNotEmpty(stationsList.getCheckedItemPosition())) {
            Station deleteStation = stationService.findByName(stationsList.getItemAtPosition(stationsList.getCheckedItemPosition()).toString());
            stationService.delete(deleteStation.getId());
            updateListView();
        }
    }

    public void navigateSearchBusButtonClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void navigateBusesButtonClick(View view) {
        Intent intent = new Intent(this, BusesActivity.class);
        startActivity(intent);
    }
}