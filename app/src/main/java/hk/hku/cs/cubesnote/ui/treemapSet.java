package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Calendar;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;
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
        SwitchCompat isLinearSwitch = (SwitchCompat) findViewById(R.id.treeSwitch);

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

        //********************************* Start init Btn text *********************************//
        Intent parentIntent = getIntent();
        cubeEventTreemapConfig =  (CubeEventTreemapConfig) parentIntent.getParcelableExtra("treeConfig");
        importance = cubeEventTreemapConfig.getImportance();
        emergency = cubeEventTreemapConfig.getEmergency();
        selectedStart = cubeEventTreemapConfig.getStart();
        selectedEnd =  cubeEventTreemapConfig.getEnd();
        isLinearEmergency = cubeEventTreemapConfig.getLinearEmergency();
        if(isLinearEmergency)
            selectedLinearStart = cubeEventTreemapConfig.getLinearEmergencyBegin();


        importance_spinner.setSelection(importance-1);
        emergency_spinner.setSelection(emergency-1);

        isLinearSwitch.setChecked(isLinearEmergency);

        PickerFragment.syncDateButton(this, startDateButton, selectedStart);
        PickerFragment.syncDateButton(this, endDateButton, selectedEnd);
        PickerFragment.syncTimeButton(this, startTimeButton, selectedStart);
        PickerFragment.syncTimeButton(this, endTimeButton, selectedEnd);
        PickerFragment.syncDateButton(this, beginDateButton, selectedLinearStart);
        //*********************************  End init Btn text  *********************************//

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("action", "cancel");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        saveBtn.setOnClickListener(v -> {
            cubeEventTreemapConfig.setImportance(importance);
            cubeEventTreemapConfig.setEmergency(emergency);
            cubeEventTreemapConfig.setStart(selectedStart);
            cubeEventTreemapConfig.setEnd(selectedEnd);
            cubeEventTreemapConfig.setLinearEmergency(isLinearEmergency);
            if (isLinearEmergency)
                cubeEventTreemapConfig.setLinearEmergencyBegin(selectedLinearStart);
            Intent intent = new Intent();
            intent.putExtra("action", "save");
            intent.putExtra("treemapConfig", (Parcelable) cubeEventTreemapConfig);
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
        SpinnerActivity spinnerActivity = new SpinnerActivity();
        importance_spinner.setOnItemSelectedListener(spinnerActivity);
        emergency_spinner.setOnItemSelectedListener(spinnerActivity);
        isLinearSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isLinearEmergency = isChecked;
        });
        //*********************************  End input configs  *********************************//
    }

    private class SpinnerActivity implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // String name = parent.getResources().getResourceEntryName(parent.getId());
            int val = Integer.parseInt(parent.getItemAtPosition(pos).toString());
            if (parent.getId() == R.id.importance_spinner)
                treemapSet.this.importance = val;
            if (parent.getId() == R.id.emergency_spinner)
                treemapSet.this.emergency = val;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Log.e("TreemapSet","Dude, how did you make it here? D:");
        }
    }
}
