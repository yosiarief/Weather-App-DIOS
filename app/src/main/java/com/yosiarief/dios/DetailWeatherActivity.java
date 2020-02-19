package com.yosiarief.dios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailWeatherActivity extends AppCompatActivity {
    TextView tvDate, tvTemp, tvCondition, tvHumidity, tvPressure, tvWind, tvLocation;
    ImageView imageViewIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);

        tvDate = findViewById(R.id.tv_date);
        tvTemp = findViewById(R.id.tv_celcius);
        tvCondition = findViewById(R.id.tv_clear);
        tvHumidity = findViewById(R.id.tv_humidtu);
        tvPressure = findViewById(R.id.tv_pressure);
        tvWind = findViewById(R.id.tv_wind);
        imageViewIcon = findViewById(R.id.image);
        tvLocation = findViewById(R.id.tv_city);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        long date = intent.getLongExtra("date", 0);
        float temp = intent.getFloatExtra("temp", 0.0f);
        String icon = intent.getStringExtra("icon");
        String condition = intent.getStringExtra("condition");
        Long humidity = intent.getLongExtra("humidity", 0);
        Long pressure = intent.getLongExtra("pressure", 0);
        double wind = intent.getDoubleExtra("wind", 0);
        String loc = intent.getStringExtra("location");

        String formatDate = Helper.timestampToDate(date);
        float celcius = Helper.kelvinToCelcius(temp);

        tvDate.setText(formatDate);
        tvTemp.setText(String.format("%.1f", celcius)+"\u2103");
        tvCondition.setText(condition);
        tvHumidity.setText("Humidity : "+humidity+" %");
        tvPressure.setText("Pressure : "+pressure+" hPa");
        tvWind.setText("Wind : "+wind+" km/h");
        tvLocation.setText(loc);
        Glide.with(this).load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageViewIcon);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
