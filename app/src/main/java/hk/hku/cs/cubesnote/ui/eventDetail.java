package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEvent;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;
import hk.hku.cs.cubesnote.utils.FileIO;

public class eventDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_window);

        TextView title = (TextView) findViewById(R.id.title);
        TextView startInfo = (TextView) findViewById(R.id.startInfo);
        TextView endInfo = (TextView) findViewById(R.id.endInfo);
        TextView note = (TextView) findViewById(R.id.note);
        TextView imLevel = (TextView) findViewById(R.id.imLevel);
        TextView emLevel = (TextView) findViewById(R.id.emLevel);
        ImageButton leftBtn = (ImageButton) findViewById(R.id.leftBtn);
        ImageButton pencilBtn = (ImageButton) findViewById(R.id.pencilBtn);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CubeEvent cube = (CubeEvent) bundle.getSerializable("event");
            Log.i("test", "" + cube.getId());
//
            title.setText(cube.getTitle());
            startInfo.setText(dateFormat.format(cube.getSelectedStartCalendar().getTime()));
            endInfo.setText(dateFormat.format(cube.getSelectedEndCalendar().getTime()));
            note.setText(cube.getDescription());
            CubeEventTreemapConfig config = cube.getTreemapConfig();
            imLevel.setText(Integer.toString(config.getImportance()));
            emLevel.setText(Integer.toString(config.getEmergency()));

            leftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            pencilBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteConfirm(cube.getId());
                }
            });
        }
    }
    public void deleteConfirm (String eventId) {
        AlertDialog.Builder deleteWindow = new AlertDialog.Builder(this);
        deleteWindow.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FileIO.deleteEvent(eventDetail.this, eventId);
                Toast toast= new Toast(getApplicationContext());
                toast.setText("Event has been deleted.");
                toast.show();
                Intent intent = new Intent(eventDetail.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
            }
        });
        deleteWindow.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        deleteWindow.setMessage("Delete the event?");
        deleteWindow.setTitle("Warning");
        deleteWindow.show();
    }
}