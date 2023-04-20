package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Calendar;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;
import hk.hku.cs.cubesnote.utils.Jsonfy;
import hk.hku.cs.cubesnote.utils.PickerFragment;

public class treemapSet extends AppCompatActivity {

    private CubeEventTreemapConfig cubeEventTreemapConfig;
    private int importance;
    private int emergency;
    private Calendar selectedStart;
    private Calendar selectedEnd;
    private Boolean isLinearEmergency;
    private Calendar selectedLinearStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.treemap_setting);

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        Button startDateButton = (Button) findViewById(R.id.sDateBtn);
        Button startTimeButton = (Button) findViewById(R.id.sTimeBtn);
        Button endDateButton = (Button) findViewById(R.id.eDateBtn);
        Button endTimeButton = (Button) findViewById(R.id.eTimeBtn);
        Button beginDateButton = (Button) findViewById(R.id.beginDateButton);

        Spinner importance_spinner = (Spinner) findViewById(R.id.importance_spinner);
        ArrayAdapter<String>  importance_Adapter = new ArrayAdapter<>(treemapSet.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.level));
        importance_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importance_spinner.setAdapter(importance_Adapter);
        Spinner emergency_spinner = (Spinner) findViewById(R.id.emergency_spinner);
        ArrayAdapter<String>  emergency_Adapter = new ArrayAdapter<>(treemapSet.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.level));
        emergency_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emergency_spinner.setAdapter(emergency_Adapter);

        //********************************* Start init time Btn *********************************//
        Intent parentIntent = getIntent();
        selectedStart = Jsonfy.stringToCalendar(parentIntent.getExtras().getString("start"));
        selectedEnd =  Jsonfy.stringToCalendar(parentIntent.getExtras().getString("end"));
        selectedLinearStart = Jsonfy.stringToCalendar(parentIntent.getExtras().getString("start"));

        PickerFragment.syncDateButton(this, startDateButton, selectedStart);
        PickerFragment.syncDateButton(this, endDateButton, selectedEnd);
        PickerFragment.syncTimeButton(this, startTimeButton, selectedStart);
        PickerFragment.syncTimeButton(this, endTimeButton, selectedEnd);
        PickerFragment.syncDateButton(this, beginDateButton, selectedLinearStart);
        //*********************************  End init time Btn  *********************************//

        cubeEventTreemapConfig  = new CubeEventTreemapConfig(selectedStart, selectedEnd, selectedLinearStart);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("treemapConfig", cubeEventTreemapConfig);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        saveBtn.setOnClickListener(v -> {
            // TODO: uncomment after finishing config update
            // cubeEventTreemapConfig.setImportance(importance);
            // cubeEventTreemapConfig.setEmergency(emergency);
            cubeEventTreemapConfig.setStart(selectedStart);
            cubeEventTreemapConfig.setEnd(selectedEnd);
            // cubeEventTreemapConfig.setLinearEmergency(isLinearEmergency);
            // if (isLinearEmergency)
            //     cubeEventTreemapConfig.setLinearEmergencyBegin(selectedLinearStart);
            Intent intent = new Intent();
            intent.putExtra("treemapConfig", cubeEventTreemapConfig);
            setResult(RESULT_OK, intent);
            finish();
        });

        //********************************* Start input calendar *********************************//
        // TODO: Dealing with End before Start
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PickerFragment.DatePickerFragment(
                        (view, year, month, day) -> {
                            selectedStart.set(year, month, day);
                            startDateButton.setText(
                                    getString(R.string.selected_date, year, month+1, day));
                        });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PickerFragment.DatePickerFragment(
                        (view, year, month, day) -> {
                            selectedEnd.set(year, month, day);
                            endDateButton.setText(
                                    getString(R.string.selected_date, year, month+1, day));
                        });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PickerFragment.TimePickerFragment(
                        (view, hourOfDay, minute) -> {
                            selectedStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            selectedStart.set(Calendar.MINUTE, minute);
                            startTimeButton.setText(
                                    getString(R.string.selected_time, hourOfDay, minute));
                        });
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PickerFragment.TimePickerFragment(
                        (view, hourOfDay, minute) -> {
                            selectedEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            selectedEnd.set(Calendar.MINUTE, minute);
                            endTimeButton.setText(
                                    getString(R.string.selected_time, hourOfDay, minute));
                        });
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        beginDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PickerFragment.DatePickerFragment(
                        (view, year, month, day) -> {
                            selectedLinearStart.set(year, month, day);
                            beginDateButton.setText(
                                    getString(R.string.selected_date, year, month+1, day));
                        });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        //*********************************  End input calendar  *********************************//

        //********************************* Start input configs *********************************//
        // TODO: For each input, update private vars.
        //*********************************  End input configs  *********************************//
    }
}
