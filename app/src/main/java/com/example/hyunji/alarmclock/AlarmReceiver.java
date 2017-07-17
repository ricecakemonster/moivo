package com.example.hyunji.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;



public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyActivity", "In the receiver");
        Toast.makeText(context, "Working!", Toast.LENGTH_SHORT).show();

        //fetch extra strings from the intent
        String getYourString = intent.getExtras().getString("extra");

        Log.e("What is the key?", getYourString);

        //Create an intent to the alarm sound
        Intent serviceIntent = new Intent(context, AlarmSoundService.class);

        //pass the extra string from Main Activity to the AlarmSoundService

        serviceIntent.putExtra("extra", getYourString);
        //Strat the alarm sound service
        context.startService(serviceIntent);

    }
}
