package hk.hku.cs.cubesnote.ui;

import static java.lang.Math.round;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hk.hku.cs.cubesnote.utils.FileIO;
import hk.hku.cs.cubesnote.utils.Jsonfy;
import squarify.library.*;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEvent;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;

public class MainActivity extends AppCompatActivity {
    private ImageButton setBtn;
    private Context myContext;
    private int btnIDIndex = 1000;
    private ArrayList<CubeEvent> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Intent intent = new Intent(MainActivity.this, Calendar.class);
                startActivity(intent);
            }
        });
        CubeEvent event = null;
        CubeEventTreemapConfig config = null;

        llContentView.post(new Runnable() {
            @Override
            public void run() {
                Integer width = llContentView.getWidth();
                Integer height = llContentView.getHeight();
                drawTreeEvents(height, width, event, config, llContentView);
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

        switch (requestCode) {
            case (1): // addEvent, from recordBtn & recordBtn2
                eventList = FileIO.readAllEvents(getApplicationContext());
                // TODO: call update Treemap
                for(CubeEvent e : eventList)
                    System.out.println(e.toJson());
                break;
        }
    }

    private List<Integer> drawTreeEvents(Integer height, Integer width,
                                         CubeEvent event, CubeEventTreemapConfig config,
                                         ConstraintLayout llContentView) {
        List<Integer> btnAddIDs = new ArrayList<>();

        ArrayList<SquarifyRect> rects;  // The rects list will contain geometry for each rectangle to draw
        ArrayList<Float> values = new ArrayList<Float>(Arrays.asList(50f, 25f, 100f, 10f, 75f)); // Values defining the squarified layout
        Squarify s = new Squarify(values, 0, 0, width, height);
        rects = s.getRects();


        Log.i("testing", ""+height);
        Log.i("testing", ""+width);

        for (int i = 0; i < rects.size(); i++) {
            // Draw a rectangle
            SquarifyRect r = rects.get(i);
            Log.i("test", "Id: " + r.getId() + ", Value: " + round(r.getValue()) + ", x: " + r.getX() + ", y: " + r.getY() + ", Dx:" + r.getDx() + ", Dy: " + r.getDy());

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams((int)r.getDx(),(int)r.getDy());
            params.leftToLeft = R.id.events;
            params.topToTop = R.id.events;
            params.leftMargin = (int) r.getX();
            params.topMargin = (int) r.getY();


            Button btnAdd = new Button(MainActivity.this);
            btnAdd.setPadding(0,0,0,0);
            btnAdd.setLayoutParams(params);
            btnAdd.setText(""+r.getValue());


            btnAdd.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btnAdd.setId(btnIDIndex);
            btnAddIDs.add(btnIDIndex);

            btnIDIndex++;
            llContentView.addView(btnAdd);
        }
        return btnAddIDs;


    }
}