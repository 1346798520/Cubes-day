package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEvent;
import hk.hku.cs.cubesnote.utils.FileIO;
import hk.hku.cs.cubesnote.utils.Jsonfy;

public class CalendarView extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private ImageButton popBtn;
    private ImageButton setBtn;
    private Context myContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_calendar);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        myContext = CalendarView.this;
//        popBtn = (ImageButton) findViewById(R.id.popBtn);
        setBtn = (ImageButton) findViewById(R.id.setBtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLeftPopWindow(v);
            }
        });
//        popBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initPopWindow(v);
//            }
//        });
        Button treemapBtn = (Button) findViewById(R.id.treemapBtn);
        ImageButton recordBtn2 = (ImageButton) findViewById(R.id.recordBtn2);
        treemapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarView.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
            }
        });
        recordBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarView.this, addEvent.class);
                startActivity(intent);
            }
        });
    }
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(View view, int position, String dayText) {
        if(!dayText.equals("")) {
//            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            ArrayList<CubeEvent> eventsOfDay = FileIO.readEventsOfDay(
                    getApplicationContext(),
                    selectedDate.getYear(),
                    selectedDate.getMonth().getValue(),
                    Integer.parseInt(dayText)
            );
            displayEventsOfDay(view, eventsOfDay);
        }
    }

    private void initPopWindow(View v, CubeEvent event) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.popup_window, null, false);
        TextView title = view.findViewById(R.id.title);
        title.setText(event.getTitle());
        TextView startInfo = view.findViewById(R.id.startInfo);
        startInfo.setText(Jsonfy.calenderToString(event.getSelectedStartCalendar()));
        TextView endInfo = view.findViewById(R.id.endInfo);
        endInfo.setText(Jsonfy.calenderToString(event.getSelectedEndCalendar()));
        TextView note = view.findViewById(R.id.note);
        note.setText(event.getDescription());
        TextView imLevel = view.findViewById(R.id.imLevel);
        TextView imLevelText = view.findViewById(R.id.imLevelText);
        TextView emLevel = view.findViewById(R.id.emLevel);
        TextView emLevelText = view.findViewById(R.id.emLevelText);
        if(event.getShownInTreeMap()) {
            imLevel.setText(String.valueOf(event.getTreemapConfig().getImportance()));
            emLevel.setText(String.valueOf(event.getTreemapConfig().getEmergency()));
        } else {
            imLevel.setVisibility(View.INVISIBLE);
            imLevelText.setVisibility(View.INVISIBLE);
            emLevel.setVisibility(View.INVISIBLE);
            emLevelText.setVisibility(View.INVISIBLE);
        }
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        AppCompatImageButton leftBtn = view.findViewById(R.id.leftBtn);
        leftBtn.setOnClickListener(view1 -> {popWindow.dismiss();});
    }

    private void displayEventsOfDay(View v, ArrayList<CubeEvent> eventsOfDay) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup eventList = (ViewGroup) inflater.inflate(R.layout.day_event_list, null);
        int lastViewId = -1;
        for (CubeEvent event : eventsOfDay) {
            View view = inflater.inflate(R.layout.day_event_item, null);
            view.setId(View.generateViewId());

            TextView title = view.findViewById(R.id.title);
            title.setText(event.getTitle());

            TextView start = view.findViewById(R.id.startInfo);
            String st = Jsonfy.calenderToString(event.getSelectedStartCalendar());
            start.setText(st);

            TextView end = view.findViewById(R.id.endInfo);
            String ed = Jsonfy.calenderToString(event.getSelectedEndCalendar());
            end.setText(ed);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            if (lastViewId != -1) {
                params.topToTop = lastViewId;
                params.topMargin = 220;
            }
            lastViewId = view.getId();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initPopWindow(v, event);
                }
            });
            eventList.addView(view, params);
        }
        final PopupWindow popWindow = new PopupWindow(eventList,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    private void initLeftPopWindow(View v) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.left_menu, null, false);
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popWindow.showAtLocation(v, Gravity.LEFT, 0, 0);
    }
}