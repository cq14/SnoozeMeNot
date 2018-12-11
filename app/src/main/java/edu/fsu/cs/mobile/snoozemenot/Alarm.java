package edu.fsu.cs.mobile.snoozemenot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import static android.content.Context.VIBRATOR_SERVICE;

public class Alarm extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Alarm Alert",Toast.LENGTH_LONG).show();
        //make phone vibrate
        //added permission to service
        Vibrator vibrate = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        vibrate.vibrate(10000);
    }
}
