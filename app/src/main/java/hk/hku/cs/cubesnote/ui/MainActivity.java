package hk.hku.cs.cubesnote.ui;

import static java.lang.Math.round;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hk.hku.cs.cubesnote.utils.FileIO;
import squarify.library.*;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEvent;

public class MainActivity extends AppCompatActivity {
    private ImageButton setBtn;
    private Context myContext;

    private ArrayList<CubeEvent> eventList;
    private ArrayList<CubeEvent> eventListInTree;
    private ArrayList<Integer> buttonIDList;
    Integer[] colorPreviews = {R.drawable.red_rec, R.drawable.blue_rec, R.drawable.yellow_rec, R.drawable.green_rec, R.drawable.black_rec};
    String[] colors = {"#ff4040", "#326dff", "#fce903", "#55b079", "#010b13"};
    private int colorIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // For debugging use
        // FileIO.clearAllEventFiles(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventList = FileIO.readAllEvents(getApplicationContext());
        myContext = MainActivity.this;
        setBtn = (ImageButton) findViewById(R.id.setBtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLeftPopWindow(v);
            }
        });
        ImageButton recordBtn = (ImageButton) findViewById(R.id.recordBtn);
        ImageButton recordBtn2 = (ImageButton) findViewById(R.id.recordBtn2);
        Button calendarBtn = (Button) findViewById(R.id.calendarBtn);
        TextView today = (TextView) findViewById(R.id.datetime);
        todayDate(today);

        ConstraintLayout llContentView = (ConstraintLayout) this.findViewById(R.id.events);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addEvent.class);
                startActivityForResult(intent, 1);
            }
        });
        recordBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addEvent.class);
                startActivityForResult(intent, 1);
            }
        });
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
            }
        });

        llContentView.post(new Runnable() {
            @Override
            public void run() {
                Integer width = llContentView.getWidth();
                Integer height = llContentView.getHeight();
                drawTreeEvents(height, width, eventList, llContentView);
            }
        });
    }
    private void initLeftPopWindow(View v) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.left_menu, null, false);
        final PopupWindow leftMenu = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        leftMenu.setTouchable(true);
        leftMenu.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        leftMenu.showAtLocation(v, Gravity.LEFT, 0, 0);
        leftMenuButtons(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTreemap();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // addEvent, from recordBtn & recordBtn2
            updateTreemap();
        }
    }

    private void updateTreemap() {
        eventList = FileIO.readAllEvents(getApplicationContext());

        ConstraintLayout llContentView = (ConstraintLayout) this.findViewById(R.id.events);
        llContentView.post(new Runnable() {
            @Override
            public void run() {
                Integer width = llContentView.getWidth();
                Integer height = llContentView.getHeight();
                drawTreeEvents(height, width, eventList, llContentView);
            }
        });
    }

    private void drawTreeEvents(Integer height, Integer width,
                                         ArrayList<CubeEvent> eventList,
                                         ConstraintLayout llContentView) {
//        FileIO.clearAllEventFiles(getApplicationContext());

        // data preparation
        int btnIDIndex = 1000;
        llContentView.removeAllViews();
        buttonIDList = new ArrayList<>();
        eventListInTree = new ArrayList<>();

        // if there is any event
        ImageButton recordBtn = (ImageButton) findViewById(R.id.recordBtn);
        TextView corner = (TextView) findViewById(R.id.corner);
        if (eventList.size() == 0) {
            // recordBtn and corner need to be visible if there is no event
            recordBtn.setVisibility(View.VISIBLE);
            recordBtn.setEnabled(true);
            corner.setVisibility(View.VISIBLE);

            return;
        }

        // invisible to show the events
        recordBtn.setVisibility(View.INVISIBLE);
        recordBtn.setEnabled(false);
        corner.setVisibility(View.INVISIBLE);

        ArrayList<Float> values = new ArrayList<>();
        ArrayList<Float> importances = new ArrayList<>();
        ArrayList<String> eventNames = new ArrayList<>();
        ArrayList<Calendar> eventDays = new ArrayList<>();
        ArrayList<Boolean> isCountDown = new ArrayList<>();

        for (CubeEvent event: eventList) {
            if (event.getTreemapConfig() != null) {
                // rating value and event names
                float emergency = (float) event.getTreemapConfig().getEmergency();
                float importance = (float) event.getTreemapConfig().getImportance();
                Calendar startDay = event.getTreemapConfig().getStart();
                Calendar endDay = event.getTreemapConfig().getEnd();
                Boolean linearEmergency = event.getTreemapConfig().getLinearEmergency();
                Boolean countDown = event.getCountingDays();
                String eventName = event.getTitle();

                if (linearEmergency) {
                    int leftDays = daysBetween(endDay);
                    int totalDays = daysBetween(startDay, endDay);
                    float factor = 1.0f - (float) leftDays / (float) totalDays;
                    emergency = leftDays == 0? 5 : emergency + (5 - emergency) * factor;
                }
                Float drawingValue = emergency * importance;
                values.add(drawingValue);
                importances.add(importance);
                eventNames.add(eventName);
                eventDays.add(endDay);
                isCountDown.add(countDown);
                eventListInTree.add(event);
            }
        }
        // if there is any event to show in treemap
        if (eventListInTree.size() == 0) {
            // recordBtn and corner need to be visible if there is no event
            recordBtn.setVisibility(View.VISIBLE);
            recordBtn.setEnabled(true);
            corner.setVisibility(View.VISIBLE);

            return;
        }

        ArrayList<SquarifyRect> rects;  // The rects list will contain geometry for each rectangle to draw
        Squarify s = new Squarify(values, 0, 0, width, height);  // get the treemap rectangles
        rects = s.getRects();


        Log.i("testing", ""+height);
        Log.i("testing", ""+width);

        for (int i = 0; i < rects.size(); i++) {
            // Draw a rectangle
            SquarifyRect r = rects.get(i);
            Log.i("test", "Id: " + r.getId() + ", Value: " + round(r.getValue()) + ", x: " + r.getX() + ", y: " + r.getY() + ", Dx:" + r.getDx() + ", Dy: " + r.getDy());

            // define the location and constraint of the button (constraint layout)
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams((int)r.getDx(),(int)r.getDy());
            params.leftToLeft = R.id.events;
            params.topToTop = R.id.events;
            params.leftMargin = (int) r.getX();
            params.topMargin = (int) r.getY();

            // drawing the button
            Button btnAdd = new Button(MainActivity.this);
            btnAdd.setPadding(50,50,50,50);
            btnAdd.setLayoutParams(params);
            String leftDay = "";
            if (isCountDown.get(r.getId())){
                leftDay = "\n" + daysBetween(eventDays.get(r.getId())) + " Days Left";
            }
            btnAdd.setText(eventNames.get(r.getId()) +leftDay);  // text of button


            // setting the stroke of the button
            int strokeWidth = 5;
            int roundRadius = 15; // 8dp
            int strokeColor = Color.parseColor("#2E3135");  // color of stroke
            int fillColor = Color.parseColor(colors[colorIndex]);  // color of background
            float[] hsvParam = new float[3];
            Color.colorToHSV(fillColor, hsvParam);
            hsvParam[1] = hsvParam[1] * (float)(importances.get(r.getId()) / 5.0);  // adjust the Saturation of color
            fillColor = Color.HSVToColor(hsvParam);


            GradientDrawable gd = new GradientDrawable();// create drawable
            gd.setColor(fillColor);
            gd.setCornerRadius(roundRadius);
            gd.setStroke(strokeWidth, strokeColor);
            btnAdd.setBackgroundDrawable(gd);



            btnAdd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btnAdd.setId(btnIDIndex);
            buttonIDList.add(btnIDIndex);

            btnIDIndex++;

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("event", eventListInTree.get(r.getId()));
                    Intent intent = new Intent(MainActivity.this, eventDetail.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2);
                }
            });

            llContentView.addView(btnAdd);
        }

    }
    private void todayDate(TextView today) {
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy EEEE");
        String str = sdf.format(date);
        today.setText(str);
    }
    private static int daysBetween(Calendar targetDay){
        Calendar cal = Calendar.getInstance();
        long time1 = cal.getTimeInMillis();
        long time2 = targetDay.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        if (between_days < 0) {
            return 0;
        }

        return Integer.parseInt(String.valueOf(between_days));
    }
    private static int daysBetween(Calendar startDay, Calendar targetDay){
        long time1 = startDay.getTimeInMillis();
        long time2 = targetDay.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    private void leftMenuButtons(View view) {
        ImageButton redBtn = (ImageButton) view.findViewById(R.id.redBtn);
        ImageButton blueBtn = (ImageButton) view.findViewById(R.id.blueBtn);
        ImageButton yellowBtn = (ImageButton) view.findViewById(R.id.yellowBtn);
        ImageButton greenBtn = (ImageButton) view.findViewById(R.id.greenBtn);
//        ImageButton blackBtn = (ImageButton) view.findViewById(R.id.blackBtn);
        TextView colorPreview = (TextView) view.findViewById(R.id.colorPreview);

        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("test", "I've been clicked.");
                changeColor(0, colorPreview);
            }
        });
        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(1, colorPreview);
            }
        });
        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(2, colorPreview);
            }
        });
        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(3, colorPreview);
            }
        });
//        blackBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeColor(4, colorPreview);
//            }
//        });
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeColor(Integer i, TextView colorPreview) {
        colorPreview.setBackground(getDrawable(colorPreviews[i]));
        colorIndex = i;
        ConstraintLayout llContentView = (ConstraintLayout) this.findViewById(R.id.events);
        llContentView.post(new Runnable() {
            @Override
            public void run() {
                Integer width = llContentView.getWidth();
                Integer height = llContentView.getHeight();
                drawTreeEvents(height, width, eventList, llContentView);
            }
        });
    }
}