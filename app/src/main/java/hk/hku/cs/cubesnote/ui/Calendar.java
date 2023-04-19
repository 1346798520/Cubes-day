package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;

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

public class Calendar extends AppCompatActivity {
    private ImageButton popBtn;
    private ImageButton setBtn;
    private Context myContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_calendar);
        myContext = Calendar.this;
        popBtn = (ImageButton) findViewById(R.id.popBtn);
        setBtn = (ImageButton) findViewById(R.id.setBtn);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLeftPopWindow(v);
            }
        });
        popBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });
        Button treemapBtn = (Button) findViewById(R.id.treemapBtn);
        ImageButton recordBtn2 = (ImageButton) findViewById(R.id.recordBtn2);
        treemapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, MainActivity.class);
                startActivity(intent);
            }
        });
        recordBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, addEvent.class);
                startActivity(intent);
            }
        });
    }
    private void initPopWindow(View v) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.popup_window, null, false);
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