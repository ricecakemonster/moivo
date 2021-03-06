package com.example.hyunji.moivo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Hyunji on 7/18/17.
 */

public class PopUp extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));


        Button turnOffAlarm = (Button) findViewById(R.id.setTime);

        turnOffAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(PopUp.this, AlarmReceiver.class);
                myIntent.putExtra("onOff", "alarm off");

                //also put an extra long into the alarm off section to prevent crashes in a Null Pointer Exception
                myIntent.putExtra("soundChoice", "minion");

                //stop the ringtone
                sendBroadcast(myIntent);

//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PopUp.this);
//                Float latitude = prefs.getFloat("latitude", 0);
//                Float longitude = prefs.getFloat("longitude", 0);

                Intent weatherIntent = new Intent(PopUp.this, Weather.class);
                startActivity(weatherIntent);

                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancelAll();

            }
        });

    }

}
