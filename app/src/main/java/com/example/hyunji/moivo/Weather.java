package com.example.hyunji.moivo;

    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.graphics.Typeface;
    import android.os.Bundle;
    import android.preference.PreferenceManager;
    import android.support.v7.app.AppCompatActivity;
    import android.text.Html;
    import android.util.Log;
    import android.widget.Button;
    import android.widget.TextView;

public class Weather extends AppCompatActivity {
    float longitude;
    float latitude;
    TextView cityField, detailsField, currentTemperatureField, tempAtNoonField, tempComeHomeField,
            weatherNowIcon, weatherNoonIcon, weatherComingHomeIcon, noonTextView, homeTextView;
    Typeface weatherFont;
    Button button;
    String textLine;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        getSupportActionBar().hide();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Float latitude = prefs.getFloat("latitude", 0);
        Float longitude = prefs.getFloat("longitude", 0);

        textLine = "Latitude: " + String.valueOf(latitude) + "\n Longitude: " + String.valueOf(longitude);
        Log.e("location in Weather: ", textLine);


        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        cityField = (TextView)findViewById(R.id.city_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        tempAtNoonField = (TextView)findViewById(R.id.weatherAtNoon);
        tempComeHomeField = (TextView) findViewById(R.id.weatherComingHome);
        weatherNowIcon = (TextView)findViewById(R.id.weatherNow_icon);
        weatherNoonIcon = (TextView)findViewById(R.id.weatherNoon_icon);
        weatherComingHomeIcon = (TextView)findViewById(R.id.weatherCH_icon);
        weatherNowIcon.setTypeface(weatherFont);
        weatherNoonIcon.setTypeface(weatherFont);
        weatherComingHomeIcon.setTypeface(weatherFont);
        noonTextView = (TextView)findViewById(R.id.textView5);
        homeTextView = (TextView)findViewById(R.id.textView6);



        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse()
        {
            public void processFinish(String weather_city, String current_weather_description,
                                      String current_weather_temperature, String current_weather_iconText,
                                      String noon_Time, String noon_weather_description,
                                      String noon_weather_temperature, String noon_weather_iconText,
                                      String home_Time, String home_weather_description,
                                      String home_weather_temperature, String home_weather_iconText)
            {
                Log.e("icon", current_weather_iconText);
                Log.e("noonicon", noon_weather_iconText);
                Log.e("homeicon", home_weather_iconText);
                cityField.setText(weather_city);
                currentTemperatureField.setText(current_weather_temperature);
                weatherNowIcon.setText(Html.fromHtml(current_weather_iconText));
                tempAtNoonField.setText(noon_weather_temperature);
                weatherNoonIcon.setText(Html.fromHtml(noon_weather_iconText));
                tempComeHomeField.setText(home_weather_temperature);
                weatherComingHomeIcon.setText(Html.fromHtml(home_weather_iconText));
                noonTextView.setText(noon_Time);
                homeTextView.setText(home_Time);

            }
        });

        String stringLatitude = String.valueOf(latitude);
        String stringLongitude = String.valueOf(longitude);
        asyncTask.execute(stringLatitude, stringLongitude);




    }
}




