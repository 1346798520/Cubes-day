package hk.hku.cs.cubesnote.ui;

import static java.lang.Math.round;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
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

import java.util.ArrayList;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // addEvent, from recordBtn & recordBtn2
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


            // TODO: call update Treemap
//                for(CubeEvent e : eventList) {
//                    System.out.println(e.toJson());
//                }
//                break;
        }
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
        ArrayList<String> eventNames = new ArrayList<>();

        for (CubeEvent event: eventList) {
            if (event.getTreemapConfig() != null) {
                // rating value and event names
                Float emergency = (float) event.getTreemapConfig().getEmergency();
                Float importance = (float) event.getTreemapConfig().getImportance();
                String eventName = event.getTitle();

                Float drawingValue = emergency * importance;
                values.add(drawingValue);
                eventNames.add(eventName);
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
            btnAdd.setText(eventNames.get(i));  // text of button

            // setting the stroke of the button
            int strokeWidth = 5;
            int roundRadius = 15; // 8dp 圆角半径
            int strokeColor = Color.parseColor("#2E3135");//边框颜色
            int fillColor = Color.parseColor("#326dff");//内部填充颜色

            GradientDrawable gd = new GradientDrawable();//创建drawable
            gd.setColor(fillColor);
            gd.setCornerRadius(roundRadius);
            gd.setStroke(strokeWidth, strokeColor);
            btnAdd.setBackgroundDrawable(gd);



            btnAdd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btnAdd.setId(btnIDIndex);
            buttonIDList.add(btnIDIndex);

            btnIDIndex++;
            llContentView.addView(btnAdd);
        }
    }
}