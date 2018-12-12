package edu.fsu.cs.mobile.snoozemenot;

import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateUpdateAlarm extends AppCompatActivity {

    Toolbar myToolbar;
    EditText time_entry;
    Spinner am_pm;
    RadioButton qr, gps;
    MaterialButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_alarm);
        myToolbar = findViewById(R.id.create_edit_app_bar);
        setSupportActionBar(myToolbar);
        String mode="";
        time_entry=findViewById(R.id.time_entry);
        am_pm=findViewById(R.id.am_pm);
        qr=findViewById(R.id.qr_radiobutton);
        gps=findViewById(R.id.gps_radiobutton);
        submit= findViewById(R.id.submit_button);
        mode = getIntent().getStringExtra("mode");

        //Dealing with the Label only
        if(mode.equals("create"))
        {
            Log.i("Create","create mode");
            myToolbar.setTitle("Create New Alarm");
            submit.setText("Create");
        }
        else
        {
            Log.i("Create","edit mode");
            myToolbar.setTitle("Edit Alarm");
            submit.setText("Update");
            //To Do: predefine these values based on existing alarm info
            //get from alarm manager?
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimeValid(time_entry.getText())){
                    time_entry.setError("Set valid time.");
                } else {
                    time_entry.setError(null);
                    //Create alarm here
                }
            }
        });

    }

    private boolean isTimeValid(@Nullable Editable text) {
        //TO:DO - Correct implementation to see if time is valid
        return text != null && text.length() <= 5;
    }
}
