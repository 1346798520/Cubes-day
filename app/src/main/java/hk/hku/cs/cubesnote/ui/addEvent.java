package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEvent;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;
import hk.hku.cs.cubesnote.utils.FileIO;
import hk.hku.cs.cubesnote.utils.Jsonfy;
import hk.hku.cs.cubesnote.utils.PickerFragment;

public class addEvent extends AppCompatActivity {

    private java.util.Calendar selectedStartCalendar;
    private java.util.Calendar selectedEndCalendar;
    private Boolean isAllDay = false;
    private Boolean isCountingDays = false;
    private Boolean isShownInTreeMap = false;
    private CubeEventTreemapConfig cubeEventTreemapConfig;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cubeEventTreemapConfig = (CubeEventTreemapConfig) data.getParcelableExtra("treemapConfig");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        ImageButton treeBtn = (ImageButton) findViewById(R.id.treeBtn);
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        final Button startDateButton = (Button) findViewById(R.id.startDateButton);
        final Button endDateButton = (Button) findViewById(R.id.endDateButton);
        final Button startTimeButton = (Button) findViewById(R.id.startTimeButton);
        final Button endTimeButton = (Button) findViewById(R.id.endTimeButton);
        final SwitchCompat isAllDaySwitch = (SwitchCompat) findViewById(R.id.eventSwitch);
        final SwitchCompat isCountingDaysSwitch = (SwitchCompat) findViewById(R.id.countSwitch);
        final SwitchCompat isShownInTreeMapSwitch = (SwitchCompat) findViewById(R.id.treeSwitch);
        final EditText eventTitle = (EditText) findViewById(R.id.eventTitle);
        final EditText description = (EditText) findViewById(R.id.description);

        //********************************* Start init time Btn *********************************//
        selectedStartCalendar = Calendar.getInstance();
        selectedEndCalendar = Calendar.getInstance();
        selectedEndCalendar.add(Calendar.HOUR_OF_DAY, 1);

        PickerFragment.syncDateButton(this, startDateButton, selectedStartCalendar);
        PickerFragment.syncDateButton(this, endDateButton, selectedEndCalendar);
        PickerFragment.syncTimeButton(this, startTimeButton, selectedStartCalendar);
        PickerFragment.syncTimeButton(this, endTimeButton, selectedEndCalendar);
        //*********************************  End init time Btn  *********************************//

        treeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addEvent.this, treemapSet.class);
                if (cubeEventTreemapConfig == null) {
                    intent.putExtra("start", Jsonfy.calenderToString(Calendar.getInstance()));
                    intent.putExtra("end", Jsonfy.calenderToString(selectedEndCalendar));
                } else {
                    // In case of re-open setting page when config is set already.
                    intent.putExtra("start", Jsonfy.calenderToString(cubeEventTreemapConfig.getStart()));
                    intent.putExtra("end", Jsonfy.calenderToString(cubeEventTreemapConfig.getEnd()));
                }
                startActivityForResult(intent, 1);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addEvent.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CubeEvent cubeEvent = new CubeEvent(selectedStartCalendar, selectedEndCalendar);
                cubeEvent.setTitle(eventTitle.getText().toString());
                cubeEvent.setAllDay(isAllDay);
                cubeEvent.setCountingDays(isCountingDays);
                cubeEvent.setDescription(description.getText().toString());
                cubeEvent.setShownInTreeMap(isShownInTreeMap);
                if(isShownInTreeMap) {
                    // TODO: cubeEventTreemapConfig is null
                    cubeEvent.setTreemapConfig(cubeEventTreemapConfig);
                }
                try {
                    System.out.println(cubeEvent.toJson().toString());
                    FileIO.writeJson(getApplicationContext(), cubeEvent.getId(), cubeEvent.toJson());
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
                Intent intent = new Intent(addEvent.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //********************************* Start input calendar *********************************//
        // TODO: Dealing with End before Start
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PickerFragment.DatePickerFragment(
                        (view, year, month, day) -> {
                            selectedStartCalendar.set(year, month, day);
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
                            selectedEndCalendar.set(year, month, day);
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
                            selectedStartCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            selectedStartCalendar.set(Calendar.MINUTE, minute);
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
                            selectedEndCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            selectedEndCalendar.set(Calendar.MINUTE, minute);
                            endTimeButton.setText(
                                    getString(R.string.selected_time, hourOfDay, minute));
                       });
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        //*********************************  End input calendar  *********************************//

        //********************************* Start input configs *********************************//
        isAllDaySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAllDay = isChecked;
        });

        isCountingDaysSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isCountingDays = isChecked;
        });

        isShownInTreeMapSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isShownInTreeMap = isChecked;
        });
        //*********************************  End input configs  *********************************//

        //*********************************   Start input text   *********************************//
        // TODO: Make description field scrollable & larger
        //*********************************    End input text    *********************************//
    }

}