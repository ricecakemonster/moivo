package com.example.hyunji.alarmclock;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Hyunji on 7/16/17.
 */

public class AlarmSoundService extends Service {

    MediaPlayer mediaSong;
    boolean isOn;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("In Service", "Start Command");

        //fetch the extra string values
        String state = intent.getExtras().getString("extra");

        Log.e("AlarmSound state: extra is ", state);


        //this converts the extra strings from the intent
        //to start IDs, values 0 or 1
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        Log.e("Start Id is", String.valueOf(startId));

        //notification
        //set up the notification service
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //set up an intent that goes to the Main Activity
        Intent intentMainActivity = new Intent(this.getApplicationContext(), MainActivity.class);

        //set up a pending intent (wait till the alarm goes off)
        PendingIntent pendingIntentMainActivity = PendingIntent.getActivity(this, 0, intentMainActivity, 0);

        // make the notification parameters
        Notification notificationPopUp = new NotificationCompat.Builder(this)
                .setContentTitle("Wake Up!")
                .setContentText("Click me!")
                .setSmallIcon(R.drawable.clock)
                .setContentIntent(pendingIntentMainActivity)
                .setAutoCancel(true) //automatically disappears when you click on it
                .build();


        //if else statements

        //if there's no music playing, and the user pressed "alarm on"
        //music should start playing
        if (!this.isOn && startId == 1){
            Log.e("there is no music, ", "and you want start");

            //create an instance of the media player

            mediaSong = MediaPlayer.create(this, R.raw.legomovie);
            mediaSong.start();
            this.isOn = true;
            startId = 0;


            //set up the notification start command
            notificationManager.notify(0, notificationPopUp);
        }
        //if there's music playing, and the user pressed "alarm off"
        //music should stop playing
        else if(this.isOn && startId == 0){

            Log.e("there is music, ", "and you want end");
            //stop the ringtone

            mediaSong.stop();
            mediaSong.reset();

            this.isOn = false;
            startId = 0;
        }
        //these are if the user presses random buttons
        //just to bug proof the app
        //if there is no music playing, and the user pressed "alarm off"
        //do nothing
        else if(!this.isOn && startId == 0){

            Log.e("there is no music, ", "and you want end");

            startId = 0;

        }
        //if there is music playing and the user pressed "alarm on"
        //do nothing
        else if(this.isOn && startId == 1){


            Log.e("there is music, ", "and you want start");
            startId = 0;

        }
        //in case of odd events
        else{
            Log.e("something else", "error?");
        }

        //crate an instance of the media player

        return START_NOT_STICKY; //if the service stops, it won't automatically restart.
    }

    @Override
    public void onDestroy(){
        //tell the user that app has been stopped.
        Log.e("on Destroy called", "destroy!");

        super.onDestroy();
        this.isOn = false;
    }

}
