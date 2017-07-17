package com.example.hyunji.alarmclock;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
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


        //if else statements

        //if there's no music playing, and the user pressed "alarm on"
        //music should start playing
        if (!this.isOn && startId == 1){
            Log.e("there is no music, ", "and you want start");

            //create an instance of the media player

            mediaSong = MediaPlayer.create(this, R.raw.banana);
            mediaSong.start();
            this.isOn = true;
            startId = 0;
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
