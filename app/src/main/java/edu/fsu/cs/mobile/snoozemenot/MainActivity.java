package edu.fsu.cs.mobile.snoozemenot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.TimePicker;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private static final int CREATE_ACTIVITY_REQUEST_CODE = 0;

    Toolbar myToolbar;
    FloatingActionButton createAlarm;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        //use alarm trigger to trip alarm when time is met
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);
        manager.set(AlarmManager.RTC_WAKEUP, System)
        */

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);

        createAlarm = findViewById(R.id.create_alarm);
        createAlarm.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this,CreateUpdateAlarm.class);
            myIntent.putExtra("mode","create");
            startActivityForResult(myIntent, CREATE_ACTIVITY_REQUEST_CODE);
        }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {
            case R.id.print_code:
            {
                Intent myIntent =  new Intent(getBaseContext(), PrintActivity.class);
                startActivity(myIntent);
                break;
            }
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_ACTIVITY_REQUEST_CODE) {
            String timeString = data.getStringExtra("time");
            String amPmString = data.getStringExtra("amPm");
            String[] minHour = timeString.split("[:]");
            int hourTime = Integer.valueOf(minHour[0]);
            int minTime = Integer.valueOf(minHour[1]);
            if (amPmString.equals("PM") && hourTime > 12)
                hourTime += 12;
            if (amPmString.equals("AM") && hourTime == 12)
                hourTime = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hourTime);
            calendar.set(Calendar.MINUTE, minTime);
            Intent intent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            System.out.print("Test = " + hourTime + " " + minTime + "\n");
        }
    }
}
