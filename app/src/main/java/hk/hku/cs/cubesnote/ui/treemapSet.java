package hk.hku.cs.cubesnote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import hk.hku.cs.cubesnote.R;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;

public class treemapSet extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treemap_setting);
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(treemapSet.this, addEvent.class);
                startActivity(intent);
            }
        });
        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            // TODO: construct and return CubeEventTreemapConfig
            CubeEventTreemapConfig cfg = null;
             intent.putExtra("treemapConfig", cfg);
            intent.putExtra("data", "cfg!!!");
            setResult(RESULT_OK, intent);
            finish();
        });
        Spinner importance_spinner = (Spinner) findViewById(R.id.importance_spinner);
        ArrayAdapter<String>  importance_Adapter = new ArrayAdapter<>(treemapSet.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.level));
        importance_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importance_spinner.setAdapter(importance_Adapter);
        Spinner emergency_spinner = (Spinner) findViewById(R.id.emergency_spinner);
        ArrayAdapter<String>  emergency_Adapter = new ArrayAdapter<>(treemapSet.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.level));
        emergency_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emergency_spinner.setAdapter(emergency_Adapter);
    }
}
