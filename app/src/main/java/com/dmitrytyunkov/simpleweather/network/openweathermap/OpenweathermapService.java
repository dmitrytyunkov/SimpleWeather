package com.dmitrytyunkov.simpleweather.network.openweathermap;

import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenweathermapService {
    @GET("/data/2.5/weather")
    Call<BaseWeatherModel> getBaseWeatherModel(@Query("q") String city,
                                               @Query("units") String unit,
                                               @Query("lang") String lang,
                                               @Query("appid") String appid);
}
