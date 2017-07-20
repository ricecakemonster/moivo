package com.example.hyunji.moivo;

    import android.content.Intent;
    import android.graphics.Typeface;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.text.Html;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;

public class Weather extends AppCompatActivity {
    float longitude;
    float latitude;
    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    Typeface weatherFont;
    Button button;
    String textLine;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main1);

        Intent intent = new Intent(Weather.this, WeatherLocation.class);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode==1)
            {
                longitude = data.getFloatExtra("longitude", 0);
                latitude = data.getFloatExtra("latitude", 0);
//                Log.e("Weather:longitude", String.valueOf(longitude));
                textLine = "Latitude: " + String.valueOf(latitude) + "\n Longitude: " + String.valueOf(longitude);
                Log.e("location in Weather: ", textLine);


                weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
                cityField = (TextView)findViewById(R.id.city_field);
                updatedField = (TextView)findViewById(R.id.updated_field);
                detailsField = (TextView)findViewById(R.id.details_field);
                currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
                humidity_field = (TextView)findViewById(R.id.humidity_field);
                pressure_field = (TextView)findViewById(R.id.pressure_field);
                weatherIcon = (TextView)findViewById(R.id.weather_icon);
                weatherIcon.setTypeface(weatherFont);

                Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
                    public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                        cityField.setText(weather_city);
                        updatedField.setText(weather_updatedOn);
                        detailsField.setText(weather_description);
                        currentTemperatureField.setText(weather_temperature);
                        humidity_field.setText("Humidity: "+weather_humidity);
                        pressure_field.setText("Pressure: "+weather_pressure);
                        weatherIcon.setText(Html.fromHtml(weather_iconText));

                    }
                });

                String stringLatitude = String.valueOf(latitude);
                String stringLongitude = String.valueOf(longitude);
                asyncTask.execute(stringLatitude, stringLongitude); //  asyncTask.execute("Latitude", "Longitude")


            }
        }
    }








}


