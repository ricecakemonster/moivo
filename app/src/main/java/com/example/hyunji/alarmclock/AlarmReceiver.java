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
//        Toast.makeText(context, "Working!", Toast.LENGTH_SHORT).show();

        //fetch extra strings from the intent
        //tells the app whether the user pressed the alarm on button or the alarm off button
        String getYourString = intent.getExtras().getString("extra");

        Log.e("What is the key?", getYourString);

        //fetch the extra longs from the intent
        //tells the app which value the user picked from the drop down menu/spinner
        int getSelectedAlarmSound = intent.getExtras().getInt("soundChoice");



        Log.e("The Alarm Sound Choice is : ", Integer.toString(getSelectedAlarmSound));
        //Create an intent to the alarm sound
        Intent serviceIntent = new Intent(context, AlarmSoundService.class);

        //pass the extra string from Main Activity to the AlarmSoundService

        serviceIntent.putExtra("extra", getYourString);

        serviceIntent.putExtra("extraSound", getSelectedAlarmSound);
        //Strat the alarm sound service
        context.startService(serviceIntent);

    }
}
