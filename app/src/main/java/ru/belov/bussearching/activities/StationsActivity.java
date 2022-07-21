package ru.belov.bussearching.activities;

import static ru.belov.bussearching.utils.EmptinessUtils.isNotEmpty;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.belov.bussearching.R;
import ru.belov.bussearching.dialogs.StationDialogFragment;
import ru.belov.bussearching.model.Station;
import ru.belov.bussearching.services.StationService;
import ru.belov.bussearching.services.impl.StationServiceImpl;

public class StationsActivity extends AppCompatActivity implements StationDialogFragment.NoticeDialogListener{

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
        StationDialogFragment dialog = new StationDialogFragment();
        dialog.show(getSupportFragmentManager(), "station");
    }

    public void deleteStationButtonClick(View view) {
        if (stationsList.getCheckedItemPosition() != -1) {
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        TextInputEditText inputEditText = Objects.requireNonNull(dialog.getDialog()).findViewById(R.id.textInputStation);
        if (isNotEmpty(Objects.requireNonNull(inputEditText.getText()).toString())) {
            stationService.create(new Station(inputEditText.getText().toString()));
        }
        updateListView();
    }
}