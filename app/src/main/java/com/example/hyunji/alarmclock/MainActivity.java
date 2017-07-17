package com.example.hyunji.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    // to make alarm manager
    AlarmManager alarmManager;
    private TimePicker alarmTimePicker;
    private TextView updateText;
    Context context;
    private PendingIntent pendingIntent;


    MainActivity inst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        // initialize our alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize our timepicker
        alarmTimePicker =(TimePicker) findViewById(R.id.timePicker);

        //initialize text update box
        updateText = (TextView) findViewById(R.id.updateText);

        // create an instance of a calendar
        final Calendar myCal = Calendar.getInstance();

        //create an intent to the Alarm Receiver class
        final Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);

        //initialize the start button
        Button alarmOn = (Button) findViewById(R.id.alarmOn);



        alarmOn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {



                //get the string values of the hour and minute
                Integer hour = alarmTimePicker.getHour();
                Integer minute = alarmTimePicker.getMinute();

                myCal.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                myCal.set(Calendar.MINUTE, alarmTimePicker.getMinute());

                //convert the int values to stirngs

                String hourString = String.valueOf(hour);
                String minuteString = String.valueOf(minute);

                String amPm = "AM";
                // convert 24 hour time to 12 hour time
                if (hour > 12) {
                    hourString = String.valueOf(hour - 12);
                    amPm = "PM";
                }

                if (minute < 10) {
                    minuteString = "0" + String.valueOf(minute);
                }



                //method that change the update text Textbox
                setAlarmText("Alarm set to" + hourString + ":" + minuteString + amPm);


                //put in extra string into myIntent to tell the clock that
                // you pressed the "alarm on" button

                myIntent.putExtra("extra", "alarm on");     //  <----------- you can use Boolean intead!
                //create a pending intent that delays the intent
                //until the specified calendar time
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, myCal.getTimeInMillis(), pendingIntent);
            }
        });

        //initialize the stop button
        Button alarmOff = (Button) findViewById(R.id.alarmOff);
        //create an onClick listener to stop the alarm or undo an alarm set

        alarmOff.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //method that change the update text Textbox
                setAlarmText("Alarm off!");
                //cancel the alarm
                alarmManager.cancel(pendingIntent);

                //put extra string into myIntent to tell the clock
                // that you pressed the "alarm off"" button

                myIntent.putExtra("extra", "alarm off");

                //stop the ringtone
                sendBroadcast(myIntent);
            }
        });


    }

    private void setAlarmText(String s) {
        updateText.setText((s));
    }



    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

}
