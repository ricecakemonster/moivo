package com.example.hyunji.moivo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
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

        TextView displayText = (TextView) findViewById(R.id.popTextView);
        Intent intent = getIntent();

        String textLine = intent.getStringExtra("text");

        displayText.setText(textLine);


//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int) (width * .8), (int) (height * .6));


        Button turnOffAlarm = (Button) findViewById(R.id.turnOffAlarm);

        turnOffAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(PopUp.this, AlarmReceiver.class);
                myIntent.putExtra("onOff", "alarm off");

                //also put an extra long into the alarm off section to prevent crashes in a Null Pointer Exception
                myIntent.putExtra("soundChoice", "minion");

                //stop the ringtone
                sendBroadcast(myIntent);

                Intent weatherIntent = new Intent(PopUp.this, Weather.class);
                startActivity(weatherIntent);

            }
        });

    }

}
