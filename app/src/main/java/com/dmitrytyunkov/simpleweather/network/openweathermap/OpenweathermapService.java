package com.dmitrytyunkov.simpleweather.network.openweathermap;

import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface OpenweathermapService {
    @GET("/data/2.5/weather")
    Call<BaseWeatherModel> getBaseWeatherModel(@Query("q") String city,
                                               @Query("units") String unit,
                                               @Query("lang") String lang,
                                               @Query("appid") String appid);

    @GET("/data/2.5/weather")
    Call<BaseWeatherModel> getBaseWeatherModel(@Query("lon") Double longitude,
                                               @Query("lat") Double latitude,
                                               @Query("units") String unit,
                                               @Query("lang") String lang,
                                               @Query("appid") String appid);

    @GET
    Call<ResponseBody> getWeatherImg(@Url String url);
}
