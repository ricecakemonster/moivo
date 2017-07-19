package com.example.hyunji.moivo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import java.util.Arrays;
import java.util.Calendar;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {


    AlarmManager alarmManager;
    private TextView updateText;
    Context context;
    private PendingIntent pendingIntent;
    private Spinner spinner;
    int selectedAlarmSound;


    MainActivity inst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        updateText = (TextView) findViewById(R.id.updateText);

        final Calendar myCal = Calendar.getInstance();

        final Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);

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


        Button alarmOn = (Button) findViewById(R.id.alarmOn);



        alarmOn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                int hour = myCal.get(Calendar.HOUR_OF_DAY);
                int minute = myCal.get(Calendar.MINUTE);


                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                        if (view.isShown()){
                            myCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCal.set(Calendar.MINUTE, minute);
                        }
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, 1, myTimeListener, hour, minute, false);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.show();



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



                setAlarmText("Alarm set to" + hourString + ":" + minuteString + amPm);

                myIntent.putExtra("onOff", "alarm on");

                myIntent.putExtra("soundChoice", selectedAlarmSound);

                Log.e("The sound id is", String.valueOf(selectedAlarmSound));


                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, myCal.getTimeInMillis(), pendingIntent);



            }
        });

        Button alarmOff = (Button) findViewById(R.id.alarmOff);

        alarmOff.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setAlarmText("Alarm off!");
                alarmManager.cancel(pendingIntent);

                myIntent.putExtra("onOff", "alarm off");

                myIntent.putExtra("soundChoice", selectedAlarmSound);

                sendBroadcast(myIntent);
            }
        });


    }

    private void setAlarmText(String s) {
        updateText.setText((s));
    }


}
