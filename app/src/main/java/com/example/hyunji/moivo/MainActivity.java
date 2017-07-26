package com.example.hyunji.moivo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {


    AlarmManager alarmManager;
    TextView updateText;
    Context context;
    private PendingIntent pendingIntent;
    private PendingIntent pendingIntentOnGoing;
    private Spinner spinner;
    int selectedAlarmSound;
    int hour;
    int minute;
    Calendar myCal, myCal1, myCal2, myCal3, myCal4, myCal5, myCal6;

    Typeface typefaceLorem;
    TextView sunday, monday, tuesday, wednesday, thursday, friday, saturday;
    final ArrayList<String> checkedDays = new ArrayList<>();
    final ArrayList<Integer> checkedDaysNum = new ArrayList<>();
    final ArrayList<Calendar> calendars = new ArrayList<>();
    Calendar[] calendarArray = {myCal, myCal1, myCal2, myCal3, myCal4, myCal5, myCal6};



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        this.context = this;

        typefaceLorem = Typeface.createFromAsset(getAssets(), "fonts/Lorem.ttf");

        sunday = (TextView)findViewById(R.id.sunday);
        monday = (TextView)findViewById(R.id.monday);
        tuesday = (TextView)findViewById(R.id.tuesday);
        wednesday = (TextView)findViewById(R.id.wednesday);
        thursday = (TextView)findViewById(R.id.thursday);
        friday = (TextView)findViewById(R.id.friday);
        saturday = (TextView)findViewById(R.id.saturday);

        updateText = (TextView) findViewById(R.id.updateText);

        sunday.setTypeface(typefaceLorem);
        monday.setTypeface(typefaceLorem);
        tuesday.setTypeface(typefaceLorem);
        wednesday.setTypeface(typefaceLorem);
        thursday.setTypeface(typefaceLorem);
        friday.setTypeface(typefaceLorem);
        saturday.setTypeface(typefaceLorem);
        updateText.setTypeface(typefaceLorem);

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


        final Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);

        final Intent changeViewIntent = new Intent(MainActivity.this, PopUp.class);


        final Button alarmOn = (Button) findViewById(R.id.alarmOn);
        final Button cancelAlarm = (Button) findViewById(R.id.cancel);
        final TimePicker myTimePicker = (TimePicker) findViewById(R.id.timePicker);
        final TextView alarmSetupTextView = (TextView) findViewById(R.id.alarmSetupTextView);


        alarmOn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){


                myIntent.putExtra("onOff", "alarm on");

                myIntent.putExtra("soundChoice", selectedAlarmSound);

                if (Build.VERSION.SDK_INT >= 23) {
                    myCal.set(Calendar.HOUR_OF_DAY, myTimePicker.getHour());
                    myCal.set(Calendar.MINUTE, myTimePicker.getMinute());

                } else {
                    myCal.set(Calendar.HOUR_OF_DAY, myTimePicker.getCurrentHour());
                    myCal.set(Calendar.MINUTE, myTimePicker.getCurrentMinute());
                }


                String[] dayTextViewArray = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};

                for(int i=0; i<7; i++){
                    String day = String.valueOf(dayTextViewArray[i]);
                    int layoutID = getResources().getIdentifier(day, "id", getPackageName());

                    TextView textView = (TextView) findViewById(layoutID);

                    ColorDrawable colorDrawable = (ColorDrawable) textView.getBackground();
                    int colorCode = colorDrawable.getColor();
                    int dayNumber = 0;

                    if (colorCode == Color.parseColor("#ffff8800")){
                        switch (day) {
                            case "sunday":
                                dayNumber = 1;
                                break;
                            case "monday":
                                dayNumber = 2;
                                break;
                            case "tuesday":
                                dayNumber = 3;
                                break;
                            case "wednesday":
                                dayNumber = 4;
                                break;
                            case "thursday":
                                dayNumber = 5;
                                break;
                            case "friday":
                                dayNumber = 6;
                                break;
                            case "saturday":
                                dayNumber = 7;
                                break;

                        }
                        checkedDays.add(day);
                        checkedDaysNum.add(dayNumber);
                    }
                }


                if (checkedDays.size() == 0){
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, myCal.getTimeInMillis(), pendingIntent);
                    Log.e("where?", "I'm here! No repeating alarms");
                }

                //repeating alarms
                else {
                    for (int i = 0; i < checkedDays.size(); i++) {
                        if (checkedDays.size() == 1) {
                            myCal.set(Calendar.DAY_OF_WEEK, checkedDaysNum.get(i));
                            calendars.add(myCal);
                        } else if (checkedDays.size() > 1) {
                            calendarArray[i] = (Calendar) myCal.clone();
                            calendarArray[i].set(Calendar.DAY_OF_WEEK, checkedDaysNum.get(i));
                            calendars.add(calendarArray[i]);
                        }
                    }


                    for (int i = 0; i < calendars.size(); i++) {
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, i,
                                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendars.get(i).getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                        Log.e("where?", "I'm here! Repeating alarms");


                    }
                }



                showTime("Alarm set for ");


                Log.e("The sound id is", String.valueOf(selectedAlarmSound));



                alarmOn.setVisibility(View.INVISIBLE);
                cancelAlarm.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                myTimePicker.setVisibility(View.INVISIBLE);
                alarmSetupTextView.setVisibility(View.INVISIBLE);
            }
        });


        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (alarmManager!= null) {
                    alarmManager.cancel(pendingIntent);
                }



                /// ++++++++버튼 두개로 나누기 (전체 알람 취소와 현재 알람 끄기)
                showTime("Alarm Canceled for ");

                updateText.setText("Alarm Canceled.\nSet New Alarm");
                alarmOn.setVisibility(View.VISIBLE);
                cancelAlarm.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.VISIBLE);
                myTimePicker.setVisibility(View.VISIBLE);
                alarmSetupTextView.setVisibility(View.VISIBLE);
                checkedDays.clear();
            }
        });
    }

    public void setReapeatAlarmClick(View view) {

        TextView selectDayView = (TextView) view;
        ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
        int colorCode = colorDrawable.getColor();
        if (colorCode == Color.parseColor("#00000000")) {
            view.setBackgroundColor(Color.parseColor("#ffff8800"));
        } else {
            view.setBackgroundColor(Color.parseColor("#00000000"));
        }



    }

    private void setAlarmText(String s) {
        updateText.setText((s));
    }

    private void showTime(String s){

        hour = myCal.get(Calendar.HOUR_OF_DAY);
        minute = myCal.get(Calendar.MINUTE);

        String hourString = String.valueOf(hour);
        String minuteString = String.valueOf(minute);

        String amPm = "am";
        // convert 24 hour time to 12 hour time
        if (hour > 12) {
            hourString = String.valueOf(hour - 12);
            amPm = "pm";
        }

        if (minute < 10) {
            minuteString = "0" + String.valueOf(minute);
        }
        String days = "";
        for(int i=0; i<checkedDays.size(); i++){
            String day = checkedDays.get(i);
            days += Character.toUpperCase(day.charAt(0)) + day.substring(1) + " ";

        }

//        if myCal.get(Calendar.DAY_OF_WEEK)
        setAlarmText(s + hourString + ":" + minuteString + amPm + "\n" + days);
        Log.e("checkedDays", String.valueOf(checkedDays));

    }
}
