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

import hk.hku.cs.cubesnote.R;

public class MainActivity extends AppCompatActivity {
    private ImageButton setBtn;
    private Context myContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addEvent.class);
                startActivity(intent);
            }
        });
        recordBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addEvent.class);
                startActivity(intent);
            }
        });
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Calendar.class);
                startActivity(intent);
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
}