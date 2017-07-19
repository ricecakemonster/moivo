package com.example.hyunji.moivo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
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
    int soundSource;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("In Service", "Start Command");

        String state = intent.getExtras().getString("onOff"); //receiving the data form AlarmReceiver

        Log.e("AlarmSound state: extra is ", state);


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



        //set up an intent that goes to the Main Activity
        Intent intentMainActivity = new Intent(this.getApplicationContext(), PopUp.class);
        intentMainActivity.putExtra("text", "Turn off Alarm \n and \n Show me the Weather");


        //set up a pending intent (wait till the alarm goes off)
        PendingIntent pendingIntentMainActivity = PendingIntent.getActivity(this, 0, intentMainActivity, 0);

        // make the notification parameters
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationPopUp = new NotificationCompat.Builder(this)
                .setContentTitle("Wake Up!")
                .setContentText("Click me!")
                .setSmallIcon(R.drawable.clock)
                .setContentIntent(pendingIntentMainActivity)
                .setAutoCancel(true)//automatically disappears when you click on it
                .setSound(soundUri)
                .build();

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);




        int soundNum = intent.getExtras().getInt("extraSound");

        if (!this.isOn && startId == 1){
            Log.e("there is no music, ", "and you want start");

            switch (soundNum) {
                case 0:
                    mediaSong = MediaPlayer.create(this, R.raw.banana);
                    break;
                case 1:
                    mediaSong = MediaPlayer.create(this, R.raw.legomovie);
                    break;
                case 2:
                    mediaSong = MediaPlayer.create(this, R.raw.minion);
                    break;
            }

            mediaSong.start();
            this.isOn = true;
            startId = 0;


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
