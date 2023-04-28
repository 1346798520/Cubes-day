package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.Calendar;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEvent;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;
import hk.hku.cs.cubesnote.utils.FileIO;
import hk.hku.cs.cubesnote.utils.PickerFragment;

public class addEvent extends AppCompatActivity {

    private java.util.Calendar selectedStartCalendar;
    private java.util.Calendar selectedEndCalendar;
    private Boolean isAllDay = false;
    private Boolean isCountingDays = false;
    private Boolean isShownInTreeMap = false;
    private CubeEventTreemapConfig cubeEventTreemapConfig;
    private CubeEvent oldCubeEvent;
    private boolean isEditingExistEvent = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getStringExtra("action").equals("save"))
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

        //********************************* Start init Btn text *********************************//
        Intent parentIntent = getIntent();
//        if(parentIntent.getStringExtra("action").equals("editEvent")) {
//            isEditingExistEvent = true;
//            oldCubeEvent = (CubeEvent) parentIntent.getParcelableExtra("event");
//        }

        if (isEditingExistEvent) {
            isAllDay = oldCubeEvent.getAllDay();
            isCountingDays = oldCubeEvent.getCountingDays();
            isShownInTreeMap = oldCubeEvent.getShownInTreeMap();
            selectedStartCalendar = oldCubeEvent.getSelectedStartCalendar();
            selectedEndCalendar = oldCubeEvent.getSelectedEndCalendar();
            if (isShownInTreeMap)
                cubeEventTreemapConfig = oldCubeEvent.getTreemapConfig();

            String title = oldCubeEvent.getTitle();
            if (title != null && !title.isEmpty())
                eventTitle.setText(title);
            String desc = oldCubeEvent.getDescription();
            if (desc != null && !desc.isEmpty())
                description.setText(desc);
        } else {
            selectedStartCalendar = Calendar.getInstance();
            selectedEndCalendar = Calendar.getInstance();
            selectedEndCalendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        isAllDaySwitch.setChecked(isAllDay);
        isCountingDaysSwitch.setChecked(isCountingDays);
        isShownInTreeMapSwitch.setChecked(isShownInTreeMap);
        PickerFragment.syncDateButton(this, startDateButton, selectedStartCalendar);
        PickerFragment.syncDateButton(this, endDateButton, selectedEndCalendar);
        PickerFragment.syncTimeButton(this, startTimeButton, selectedStartCalendar);
        PickerFragment.syncTimeButton(this, endTimeButton, selectedEndCalendar);
        //*********************************  End init Btn text  *********************************//

        treeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addEvent.this, treemapSet.class);
                if (cubeEventTreemapConfig == null) {
                    CubeEventTreemapConfig defaultConfig = new CubeEventTreemapConfig(
                            Calendar.getInstance(),
                            selectedEndCalendar,
                            Calendar.getInstance()
                    );
                    intent.putExtra("treeConfig", (Parcelable) defaultConfig);
                } else {
                    // In case of re-open setting page when config is set already.
                    intent.putExtra("treeConfig", (Parcelable) cubeEventTreemapConfig);
                }
                startActivityForResult(intent, 1);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CubeEvent cubeEvent;
                if (!isEditingExistEvent)
                    cubeEvent = new CubeEvent(selectedStartCalendar, selectedEndCalendar);
                else
                    cubeEvent = oldCubeEvent;
                cubeEvent.setTitle(eventTitle.getText().toString());
                cubeEvent.setAllDay(isAllDay);
                cubeEvent.setCountingDays(isCountingDays);
                cubeEvent.setDescription(description.getText().toString());
                cubeEvent.setShownInTreeMap(isShownInTreeMap);
                if(isShownInTreeMap) {
                    if(cubeEventTreemapConfig == null)
                        cubeEventTreemapConfig = new CubeEventTreemapConfig(
                                Calendar.getInstance(),
                                selectedEndCalendar,
                                Calendar.getInstance()
                        );
                    cubeEvent.setTreemapConfig(cubeEventTreemapConfig);
                }
                try {
                    System.out.println(cubeEvent.toJson().toString());
                    FileIO.writeJson(getApplicationContext(), cubeEvent.getId(), cubeEvent.toJson());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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