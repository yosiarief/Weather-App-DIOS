package com.yosiarief.dios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.gson.Gson;
import com.yosiarief.dios.model.current.CurrentWeather;
import com.yosiarief.dios.model.forecast.ForecastWeatherModel;
import com.yosiarief.dios.network.RetrofitInterface;
import com.yosiarief.dios.network.RetrofitService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView tvCelcius, tvCity,tvClear, tvDate;
    RetrofitInterface retrofitInterface;
    ImageView imageView;
    ForecastWeatherModel forecastWeatherModel;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCelcius = findViewById(R.id.tv_celcius);
        tvDate = findViewById(R.id.tv_date);
        tvCity = findViewById(R.id.tv_city);
        tvClear = findViewById(R.id.tv_clear);
        imageView = findViewById(R.id.image);
        swipeRefreshLayout = findViewById(R.id.swipe);
        retrofitInterface = RetrofitService.getRetrofitClient().create(RetrofitInterface.class);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collap);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("  Digital Ocean");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callCurrentWeather();
                callForecast();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        callCurrentWeather();
        callForecast();

    }

    public void callForecast(){
        final Call<String> forecast = retrofitInterface.forecastWeather("Bandung");
        forecast.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                forecastWeatherModel = new Gson().fromJson(response.body(), ForecastWeatherModel.class);
                initRecyclerView();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void callCurrentWeather(){
        Call<String> current = retrofitInterface.currentWeathers("Bandung");
        current.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                CurrentWeather curr = new Gson().fromJson(response.body(), CurrentWeather.class);
                float celcius = Helper.kelvinToCelcius(curr.getMain().getTemp());
                tvCelcius.setText(String.format("%.1f", celcius)+"\u2103");
                tvCity.setText(curr.getName()+", "+curr.getSys().getCountry().toUpperCase());
                tvClear.setText(curr.getWeather().get(0).getDescription());
                String date = Helper.timestampToDate(curr.getDt());
                tvDate.setText(date);
                Glide.with(MainActivity.this).load("http://openweathermap.org/img/wn/"+curr.getWeather().get(0).getIcon()+"@2x.png").into(imageView);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void initRecyclerView(){
        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(R.layout.list_weather, forecastWeatherModel, MainActivity.this);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);
    }

}
