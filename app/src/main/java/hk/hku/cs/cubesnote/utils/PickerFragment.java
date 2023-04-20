package hk.hku.cs.cubesnote.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import hk.hku.cs.cubesnote.R;

public class PickerFragment {

    public interface TimeCallerButton {
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

    public interface DateCallerButton {
        void onDateSet(DatePicker view, int year, int month, int day);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private DateCallerButton caller;

        public DatePickerFragment(DateCallerButton caller) {
            this.caller = caller;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            caller.onDateSet(view, year, month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private TimeCallerButton caller;

        public TimePickerFragment(TimeCallerButton caller) {
            this.caller = caller;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            caller.onTimeSet(view, hourOfDay, minute);
        }
    }

    public static void syncDateButton(Context context, Button btn, Calendar c) {
        btn.setText( context.getResources().getString(
                R.string.selected_date,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH)+1,
                c.get(Calendar.DATE)));
    }

    public static void syncTimeButton(Context context, Button btn, Calendar c) {
        btn.setText( context.getResources().getString(
                R.string.selected_time,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE)));
    }
}
