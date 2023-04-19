package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Calendar;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.utils.PickerFragment;

public class addEvent extends AppCompatActivity {

    private java.util.Calendar selectedStartCalendar = Calendar.getInstance();;
    private java.util.Calendar selectedEndCalendar = Calendar.getInstance();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        ImageButton treeBtn = (ImageButton) findViewById(R.id.treeBtn);
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        final Button startDateButton = (Button) findViewById(R.id.startDateButton);
        final Button endDateButton = (Button) findViewById(R.id.endDateButton);
        final Button startTimeButton = (Button) findViewById(R.id.startTimeButton);
        final Button endTimeButton = (Button) findViewById(R.id.endTimeButton);

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

        //********************************* Start input calendar *********************************//
        // TODO: Dealing with End before Start
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
    }

}