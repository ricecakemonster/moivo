package com.example.hyunji.moivo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {


    AlarmManager alarmManager;
    private TextView updateText;
    Context context;
    private PendingIntent pendingIntent;
    private Spinner spinner;
    int selectedAlarmSound;
    int hour;
    int minute;
    Calendar myCal;


    MainActivity inst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        updateText = (TextView) findViewById(R.id.updateText);

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAlarmSound = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        myCal = Calendar.getInstance();

        Button setTime = (Button) findViewById(R.id.turnOffAlarm);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                        if (view.isShown()){
                            myCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCal.set(Calendar.MINUTE, minute);
                        }
                        showTime("Time Selected: ");

                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, 1, myTimeListener, hour, minute, false);
                timePickerDialog.setTitle("Choose Time:");
                timePickerDialog.show();

            }
        });


        final Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);

        final Intent changeViewIntent = new Intent(MainActivity.this, PopUp.class);


        Button alarmOn = (Button) findViewById(R.id.alarmOn);

        alarmOn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                showTime("Alarm set for ");

                myIntent.putExtra("onOff", "alarm on");

                myIntent.putExtra("soundChoice", selectedAlarmSound);

                Log.e("The sound id is", String.valueOf(selectedAlarmSound));


                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                alarmManager.set(AlarmManager.RTC_WAKEUP, myCal.getTimeInMillis(), pendingIntent);

                changeViewIntent.putExtra("text", "Alarm is set. \n Do you want to cancel?");
                Log.e("before staring new Intent", "working");
                startActivityForResult(changeViewIntent, 100);
                Log.e("after starting new Intent", "working");


            }
        });

        Button cancelAlarm = (Button) findViewById(R.id.cancelAlarm);

        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (alarmManager!= null) {
                    alarmManager.cancel(pendingIntent);
                }
                showTime("Alarm Canceled for ");
            }
        });
    }

    private void setAlarmText(String s) {
        updateText.setText((s));
    }

    private void showTime(String s){

        hour = myCal.get(Calendar.HOUR_OF_DAY);
        minute = myCal.get(Calendar.MINUTE);

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

        setAlarmText(s + hourString + ":" + minuteString + amPm);

    }
}
