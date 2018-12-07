package edu.fsu.cs.mobile.snoozemenot;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton createAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAlarm = findViewById(R.id.create_alarm);
        createAlarm.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this,CreateUpdateAlarm.class);
            startActivity(myIntent);
        }
        });
    }
}
