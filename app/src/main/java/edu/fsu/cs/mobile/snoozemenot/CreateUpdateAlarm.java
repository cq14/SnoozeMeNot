package edu.fsu.cs.mobile.snoozemenot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateUpdateAlarm extends AppCompatActivity {

    TextView label;
    EditText time_entry;
    Spinner am_pm;
    CheckBox qr,gps;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_alarm);
        String mode="";
        label=findViewById(R.id.label_header);
        time_entry=findViewById(R.id.edit_time);
        am_pm=findViewById(R.id.am_pm);
        qr=findViewById(R.id.qr_check);
        gps=findViewById(R.id.gps_check);
        submit=findViewById(R.id.submit_button);
        mode = getIntent().getStringExtra("mode");

        //Dealing with the Label only
        if(mode.equals("create"))
        {
            Log.i("Create","create mode");
            label.setText("Create New Alarm");
            submit.setText("Create");
        }
        else
        {
            Log.i("Create","edit mode");
            label.setText("Edit Existing Alarm");
            submit.setText("Update");
            //To Do: predefine these values based on existing alarm info
            //get from alarm manager?
        }



    }
}
