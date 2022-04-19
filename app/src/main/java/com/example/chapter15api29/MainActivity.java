package com.example.chapter15api29;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // we"ll make HTTP request to this URL to retrieve weather conditions
    String weatherWebserviceURL =
            "http://api.openweathermap.org/data/2.5/weather?q=riyadh&appid=c4e0425479a004ea8e0751e141be2152&units=metric";
    ImageView weatherBackground;
    // Textview to show temperature and description
    TextView temperature, description,humidity,sunrise,sunset,windSpeed;
    // JSON object that contains weather information
    JSONObject jsonObj;
    ZoneOffset offset = ZoneOffset.of("+3");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        description = (TextView) findViewById(R.id.description);
        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);
        windSpeed = (TextView) findViewById(R.id.windspeed);
        weatherBackground = (ImageView) findViewById(R.id.weatherbackground);
        weather(weatherWebserviceURL);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.entries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                Log.d("Faisal option",text);
                weatherWebserviceURL =
                        "http://api.openweathermap.org/data/2.5/weather?q="+text+"&appid=c4e0425479a004ea8e0751e141be2152&units=metric";
                if(text.equals("Riyadh")){
                    offset = ZoneOffset.of("+3");
                }
                else if(text.equals("Paris")){
                    offset = ZoneOffset.of("+2");
                }
                else if(text.equals("Milton")){
                    offset = ZoneOffset.of("-4");
                }
                else if(text.equals("New York")){
                    offset = ZoneOffset.of("-4");
                }
                else if(text.equals("Moscow")){
                    offset = ZoneOffset.of("+3");
                }
                weather(weatherWebserviceURL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void weather(String URL){
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Faisal","Response received");
                        Log.d("Faisal",response.toString());
                        try {
                            String town = response.getString("main");
                            Log.d("Faisal's town",town);
                            JSONObject jsonMain = response.getJSONObject("main");
                            double temp = jsonMain.getDouble("temp");
                            double hum = jsonMain.getDouble("humidity");
                            Log.d("Faisal's temp",String.valueOf(temp));
                            temperature.setText(String.valueOf(temp)+"°C");
                            humidity.setText("Humidity: "+String.valueOf((int)hum)+"%");
                            String townResponse = response.getString("name");
                            description.setText(townResponse);
                            JSONObject jsonSYS = response.getJSONObject("sys");
                            long sunriseL = jsonSYS.getLong("sunrise");
                            long sunsetL = jsonSYS.getLong("sunset");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(("HH:mm:ss"));
                            OffsetDateTime instant = Instant.ofEpochSecond(sunriseL).atOffset(offset);
                            sunrise.setText("Sunrise: "+instant.format(formatter));
                            OffsetDateTime instant1 = Instant.ofEpochSecond(sunsetL).atOffset(offset);
                            sunset.setText("Sunset: "+instant1.format(formatter));
                            JSONObject jsonWind = response.getJSONObject("wind");
                            double speed = jsonWind.getDouble("speed");
                            windSpeed.setText("Wind Speed: "+String.valueOf(speed));
                            JSONArray jsonWeather = response.getJSONArray("weather");
                            JSONObject jsonWeatherObject = jsonWeather.getJSONObject(0);
                            String weather = jsonWeatherObject.getString("main");
                            /*
                            Test
                            String weather = "Cloudy";
                            String weather = "Rain";
                            */
                            Log.d("Faisal weather",weather);
                            if(weather.equals("Clear")){
                                Glide.with(MainActivity.this)
                                        .load("https://i.picsum.photos/id/866/536/354.jpg?hmac=tGofDTV7tl2rprappPzKFiZ9vDh5MKj39oa2D--gqhA")
                                        .into(weatherBackground);
                            }
                            else if(weather.equals("Cloudy") || weather.equals("Clouds")){
                                Glide.with(MainActivity.this)
                                        .load("https://i.ibb.co/Jd1kZXx/istockphoto-598222542-612x612.jpg")
                                        .into(weatherBackground);
                            }
                            else if(weather.equals("Rainy") || weather.equals("Rain")){
                                Glide.with(MainActivity.this)
                                        .load("https://i.ibb.co/vw50D4m/16x9-M.jpg")
                                        .into(weatherBackground);
                            }
                            else if(weather.equals("Snow")){
                                Glide.with(MainActivity.this)
                                        .load("https://i.ibb.co/sWWxS61/0015d72f-500.jpg")
                                        .into(weatherBackground);
                            }
                            else{
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Log.e("Faisal error",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println("Test1");
                        Log.d("Faisal","Error retrieving the URL");
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);
    }
}