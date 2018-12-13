package edu.fsu.cs.mobile.snoozemenot;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int CREATE_ACTIVITY_REQUEST_CODE = 0;
    private static final String MYPREFERENCES = "key";

    Toolbar myToolbar;
    FloatingActionButton createAlarm;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    FrameLayout frameLayout;
    Gson gson;
    SharedPreferences shref;

    private List<AlarmObject> alarmObjectList;
    private RecyclerView recyclerView;
    private AlarmObjectAdapter alarmAdapter;

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.frame_overlay);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        gson = new Gson();
        shref = this.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        String response = shref.getString("alarms","");
        if(response.isEmpty())
            alarmObjectList = new ArrayList<AlarmObject>();
        else
            alarmObjectList = gson.fromJson(response, new TypeToken<List<AlarmObject>>() {}.getType());

        alarmAdapter = new AlarmObjectAdapter(alarmObjectList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(alarmAdapter);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
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

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                    }

                    @Override public void onLongItemClick(View view, final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Are you sure you want to delete this alarm?")
                                .setTitle("Alarm Delete");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alarmObjectList.remove(position);
                                alarmAdapter.notifyDataSetChanged();
                                SharedPreferences.Editor editor;
                                shref = MainActivity.this.getSharedPreferences(MYPREFERENCES, MainActivity.this.MODE_PRIVATE);

                                gson = new Gson();
                                String jsonAlarms = gson.toJson(alarmObjectList);

                                editor = shref.edit();
                                editor.remove("alarms").apply();
                                editor.putString("alarms", jsonAlarms);
                                editor.commit();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                })
        );
    }

    AlarmReceiver broadcastReceiver = new AlarmReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            scannerView = new ZXingScannerView(getApplicationContext());
            frameLayout.addView(scannerView);
            createAlarm.setEnabled(false);
            scannerView.setResultHandler(MainActivity.this);
            scannerView.startCamera();
        }
    };

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

            String timeString, amPmString;
            if (data != null) {
                timeString = data.getStringExtra("time");
                amPmString = data.getStringExtra("amPm");
                String nameString = data.getStringExtra("name");
                String[] minHour = timeString.split("[:]");
                int hourTime = Integer.valueOf(minHour[0]);
                int minTime = Integer.valueOf(minHour[1]);
                if (amPmString.equals("PM") && hourTime < 12)
                    hourTime += 12;
                if (amPmString.equals("AM") && hourTime == 12)
                    hourTime = 0;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourTime);
                calendar.set(Calendar.MINUTE, minTime);
                calendar.set(Calendar.SECOND, 0);
                long time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
                if (System.currentTimeMillis() > time) {
                    if (calendar.AM_PM == 0)
                        time = time + (1000 * 60 * 60 * 12);
                    else
                        time = time + (1000 * 60 * 60 * 24);
                }
                Intent intent = new Intent(this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                registerReceiver(broadcastReceiver, new IntentFilter("ALARM_CREATED"));
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                System.out.print("Test = " + hourTime + " " + minTime + "\n");
                AlarmObject alarmObject = new AlarmObject(timeString, amPmString, nameString, true);
                alarmObjectList.add(alarmObject);
                alarmAdapter.notifyDataSetChanged();

                SharedPreferences.Editor editor;
                shref = this.getSharedPreferences(MYPREFERENCES, this.MODE_PRIVATE);

                gson = new Gson();
                String jsonAlarms = gson.toJson(alarmObjectList);

                editor = shref.edit();
                editor.remove("alarms").apply();
                editor.putString("alarms", jsonAlarms);
                editor.commit();
            }
        }
    }

    @Override
    public void handleResult(Result result){
        if(result.getText().equals("SnoozeMeNot")){
            scannerView.stopCamera();
            Intent intent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            alarmManager.cancel(pendingIntent);
            createAlarm.setEnabled(true);
            frameLayout.removeView(scannerView);
        }
    }
}
