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
import hk.hku.cs.cubesnote.utils.FileIO;
import hk.hku.cs.cubesnote.utils.Jsonfy;
import hk.hku.cs.cubesnote.utils.PickerFragment;

public class addEvent extends AppCompatActivity {

    private java.util.Calendar selectedStartCalendar = Calendar.getInstance();;
    private java.util.Calendar selectedEndCalendar = Calendar.getInstance();;
    private Boolean isAllDay = false;
    private Boolean isCountingDays = false;
    private Boolean isShownInTreeMap = false;


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

        treeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addEvent.this, treemapSet.class);
                startActivity(intent);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addEvent.this, MainActivity.class);
                startActivity(intent);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CubeEvent cubeEvent = new CubeEvent();
                cubeEvent.setTitle(eventTitle.getText().toString());
                cubeEvent.setAllDay(isAllDay);
                cubeEvent.setSelectedStartCalendar(selectedStartCalendar);
                cubeEvent.setSelectedEndCalendar(selectedEndCalendar);
                cubeEvent.setCountingDays(isCountingDays);
                cubeEvent.setDescription(description.getText().toString());
                cubeEvent.setShownInTreeMap(isShownInTreeMap);
                if(isShownInTreeMap) {
                    // TODO: construct and set CubeEventTreemapConfig
                    cubeEvent.setTreemapConfig(null);
                }
                try {
                    System.out.println(cubeEvent.toJson().toString());
                    FileIO.writeJson(getApplicationContext(), cubeEvent.getId(), cubeEvent.toJson());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(addEvent.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //********************************* Start input calendar *********************************//
        // TODO: Dealing with End before Start
        // TODO: Give current Date and Time as default value
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new PickerFragment.DatePickerFragment(
                        (view, year, month, day) -> {
                            selectedStartCalendar.set(year, month+1, day);
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
                            selectedEndCalendar.set(year, month+1, day);
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