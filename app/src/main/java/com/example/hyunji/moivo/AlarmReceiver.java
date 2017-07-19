package com.example.hyunji.moivo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;



public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MyActivity", "In the receiver");

        String getYourString = intent.getExtras().getString("onOff");

        int getSelectedAlarmSound = intent.getExtras().getInt("soundChoice");



        Log.e("The Alarm Sound Choice is : ", Integer.toString(getSelectedAlarmSound));
        Intent serviceIntent = new Intent(context, AlarmSoundService.class);

        serviceIntent.putExtra("onOff", getYourString);

        serviceIntent.putExtra("extraSound", getSelectedAlarmSound);
        context.startService(serviceIntent);

    }
}
