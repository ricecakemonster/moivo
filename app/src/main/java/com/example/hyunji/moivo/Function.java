package com.example.hyunji.moivo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class Function {
    static double tempNow;
    static double tempNoon;
    static double tempHome;
    static Context context;
    private static final String OPEN_WEATHER_CURRENT_URL =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_FORECAST_URL =
            "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "2ff191836252d3a7f4c4eeed4a00adfb";
    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;"; //sunny
            } else {
                icon = "&#xf02e;"; //clear night
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;"; //thunderstorm
                    break;
                case 3 : icon = "&#xf01c;"; //sprinkle
                    break;
                case 7 : icon = "&#xf014;"; //fog
                    break;
                case 8 : icon = "&#xf013;"; //cloudy
                    break;
                case 6 : icon = "&#xf01b;"; //snow
                    break;
                case 5 : icon = "&#xf019;"; //rain
                    break;
            }
        }
        return icon;
    }

    public interface AsyncResponse {
        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8, String output9, String output10, String output11, String output12, double output13, double output14, double output15);
    }

    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {
        public AsyncResponse delegate = null;//Call back interface
        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }
            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null){
                    JSONObject current = json.getJSONObject("current");
                    JSONObject noon = json.getJSONObject("noon"); //around noon
                    JSONObject home = json.getJSONObject("home"); //about time for kids to come home
                    JSONObject details = current.getJSONArray("weather").getJSONObject(0);
                    JSONObject detailsNoon = noon.getJSONArray("weather").getJSONObject(0);
                    JSONObject detailsHome = current.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = current.getJSONObject("main");
                    JSONObject mainNoon = noon.getJSONObject("main");
                    JSONObject mainHome = home.getJSONObject("main");
                    String city = current.getString("name").toUpperCase(Locale.US) + ", " +
                            current.getJSONObject("sys").getString("country");
                    String descriptionNow = details.getString("description").toUpperCase(Locale.US);
                    String descriptionNoon = detailsNoon.getString("description").toUpperCase(Locale.US);
                    String descriptionHome = detailsHome.getString("description").toUpperCase(Locale.US);
                    String temperatureNow = String.format("%.2f", main.getDouble("temp"))+ "°";
                    String temperatureNoon = String.format("%.2f", mainNoon.getDouble("temp"))+ "°";

                    String temperatureHome = String.format("%.2f", mainHome.getDouble("temp"))+ "°";

                    String iconTextNow = setWeatherIcon(details.getInt("id"), 20, 20);

                    String iconTextNoon = setWeatherIcon(detailsNoon.getInt("id"), 20, 20);

                    String iconTextHome = setWeatherIcon(detailsHome.getInt("id"), 20, 20);

                    DateFormat df = DateFormat.getDateTimeInstance();
                    DateFormat df1 = DateFormat.getDateTimeInstance();
                    String timeNoon = df.format(new Date(noon.getLong("dt")*1000));
                    String timeHome = df1.format(new Date(home.getLong("dt")*1000));
                    Log.e("test", "working");

                    tempNow = main.getDouble("temp");
                    tempNoon = mainNoon.getDouble("temp");
                    tempHome = mainHome.getDouble("temp");

                    delegate.processFinish(city, descriptionNow, temperatureNow, iconTextNow, timeNoon,
                            descriptionNoon, temperatureNoon, iconTextNoon, timeHome, descriptionHome, temperatureHome,
                            iconTextHome, tempNow, tempNoon, tempHome);

                }

            } catch (JSONException e) {

//Log.e(LOG_TAG, "Cannot process JSON results", e);

            }
        }
    }

    public static JSONObject getWeatherJSON(String lat, String lon){
        try {
            URL currentUrl = new URL(String.format(OPEN_WEATHER_CURRENT_URL, lat, lon));
            URL forecastUrl = new URL(String.format(OPEN_WEATHER_FORECAST_URL, lat, lon));
            HttpURLConnection currentConnection =
                    (HttpURLConnection)currentUrl.openConnection();
            HttpURLConnection forecastConnection =
                    (HttpURLConnection)forecastUrl.openConnection();
            currentConnection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);
            forecastConnection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(currentConnection.getInputStream()));
            BufferedReader reader1 = new BufferedReader(
                    new InputStreamReader(forecastConnection.getInputStream()));
            StringBuffer currentJson = new StringBuffer(1024);
            StringBuffer forecastJson = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                currentJson.append(tmp).append("\n");
            reader.close();
            String tmp1="";
            while((tmp1=reader1.readLine())!=null)
                forecastJson.append(tmp1).append("\n");
            reader1.close();
            JSONObject currentData = new JSONObject(currentJson.toString());
            JSONObject forecastData = new JSONObject(forecastJson.toString());
            JSONArray jsonArray = forecastData.getJSONArray("list");
            JSONObject forecastData1 = jsonArray.getJSONObject(1);
            JSONObject forecastData2 = jsonArray.getJSONObject(2);

            //putting current data and forecast data together
            JSONObject combinedData = new JSONObject();
            combinedData.put("current", currentData);
            combinedData.put("noon", forecastData1);
            combinedData.put("home", forecastData2);

            //Log.e("combined", String.valueOf(combinedData));
            if(currentData.getInt("cod") != 200 ||  forecastData.getInt("cod") != 200 ){
                return null;
            }
            return combinedData;
        }catch(Exception e){
            return null;
        }
    }
}
