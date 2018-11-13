package com.dmitrytyunkov.simpleweather.network.openweathermap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkOpenweathermapBuilder {
    private static String url;
    private static Retrofit retrofit;

    public NetworkOpenweathermapBuilder(String url) {
        this.url = url;
    }

    public static void init(String url) {
        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
    }

    public static OpenweathermapService getOpenweathermapService() {
        return retrofit.create(OpenweathermapService.class);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
