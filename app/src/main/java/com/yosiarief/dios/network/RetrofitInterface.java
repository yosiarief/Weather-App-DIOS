package com.yosiarief.dios.network;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @POST("weather")
    Call<String> currentWeathers(@Query("q") String query);

    @POST("forecast")
    Call<String> forecastWeather(@Query("q") String query);

}
