package com.example.hyunji.moivo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Start the app when the device boots.
 */

public class AlarmBootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        }
}

