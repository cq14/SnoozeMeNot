package edu.fsu.cs.mobile.snoozemenot;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUpdateAlarm extends AppCompatActivity {

    Toolbar myToolbar;
    TextInputLayout timeTextLayout, nameTextLayout;
    TextInputEditText time_entry, name_entry;
    Spinner am_pm;
    RadioButton qr, gps;
    MaterialButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_alarm);
        myToolbar = findViewById(R.id.create_edit_app_bar);
        timeTextLayout = findViewById(R.id.time_text_input);
        nameTextLayout = findViewById(R.id.name_input);
        setSupportActionBar(myToolbar);
        String mode = "";
        time_entry = findViewById(R.id.time_entry);
        name_entry = findViewById(R.id.name_entry);
        am_pm = findViewById(R.id.am_pm);
        submit = findViewById(R.id.submit_button);
        mode = getIntent().getStringExtra("mode");

        //Dealing with the Label only
        if (mode.equals("create")) {
            Log.i("Create", "create mode");
            myToolbar.setTitle("Create New Alarm");
            submit.setText("Create");
        } else {
            Log.i("Create", "edit mode");
            myToolbar.setTitle("Edit Alarm");
            submit.setText("Update");
            //To Do: predefine these values based on existing alarm info
            //get from alarm manager?
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimeValid(time_entry.getText())) {
                    timeTextLayout.setError("Set valid time");
                } else {
                    timeTextLayout.setError(null);
                    //Create alarm here
                    //--------Send back data to main------
                    Intent myIntent = new Intent();
                    String passBack = time_entry.getText().toString();
                    String amPm = am_pm.getSelectedItem().toString();
                    myIntent.putExtra("time", passBack);
                    myIntent.putExtra("amPm", amPm);
                    setResult(RESULT_OK, myIntent);
                    finish();
                }

                if(!isNameValid(name_entry.getText())) {
                    nameTextLayout.setError("Name required");
                } else {
                    nameTextLayout.setError(null);
                }
            }

        });

    }

    private boolean isTimeValid(@Nullable Editable text) {
        String TIME12HOURS_PATTERN = "^(0?[1-9]|1[0-2]):[0-5][0-9]$";
        Pattern pattern = Pattern.compile(TIME12HOURS_PATTERN);
        Matcher matcher = pattern.matcher(text.toString());
        return matcher.matches();
    }

    private boolean isNameValid(@Nullable Editable text) {return text != null;}
}