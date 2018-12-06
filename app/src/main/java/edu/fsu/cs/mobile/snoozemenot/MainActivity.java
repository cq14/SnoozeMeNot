package edu.fsu.cs.mobile.snoozemenot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button Create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Create = findViewById(R.id.create_button);
        Create.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this,CreateUpdateAlarm.class);
            startActivity(myIntent);
        }
        });
    }
}
