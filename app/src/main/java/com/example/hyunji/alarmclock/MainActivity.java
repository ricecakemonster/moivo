package com.example.hyunji.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // to make alarm manager
    AlarmManager alarmManager;
    private TimePicker alarmTimePicker;
    private TextView updateText;
    Context context;
    private PendingIntent pendingIntent;
    int selectedAlarmSound;


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


        //create the spinner in the main UI
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //Create an ArrayAdapter using the string array and a default spinner layout
//        String[] mTestArray; //test
//        mTestArray =   getResources().getStringArray(R.array.alarmSounds); //test


//        Toast.makeText(context, "alarmSounds Array: " + Arrays.toString(mTestArray), Toast.LENGTH_LONG).show();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.alarmSounds, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //set an onClick listne to the onItemSelected method
        spinner.setOnItemSelectedListener(this);


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


                //put in extra string into m Intent to tell the clock that
                // you pressed the "alarm on" button

                myIntent.putExtra("extra", "alarm on");     //  <----------- you can use Boolean intead!

                //put in an extra long into myIntent
                //tells the clock that you want a certain value from the dropdown menu/spinner
                myIntent.putExtra("soundChoice", selectedAlarmSound);

                Log.e("The sound id is", String.valueOf(selectedAlarmSound));


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

                //also put an extra long into the alarm off section to prevent crashes in a Null Pointer Exception
                myIntent.putExtra("soundChoice", selectedAlarmSound);

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos)
        //outputing  the id the user has selected
        selectedAlarmSound = (int) id;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Another interface callback

    }
}
