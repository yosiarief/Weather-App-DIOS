package com.yosiarief.dios.network;

import com.yosiarief.dios.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitService {
    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);


//    public static Retrofit getRetrofitClient() {
//        return buildClient(BaseConfig.getBaseUrl());
//    }
    public static Retrofit getRetrofitClient() {
        return buildClient("https://api.openweathermap.org/data/2.5/");
    }

    private static Retrofit buildClient(final String url) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url1 = originalHttpUrl.newBuilder()
                                .addQueryParameter("appid", "112dcfc88f9913841c0d2963541f4c38")
                                .build();

                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url1);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(logging);
        }

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .client(okHttpClient.build())
                .build();
    }
}
