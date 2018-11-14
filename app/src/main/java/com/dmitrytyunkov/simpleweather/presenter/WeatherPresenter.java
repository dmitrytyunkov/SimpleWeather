package com.dmitrytyunkov.simpleweather.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;
import com.dmitrytyunkov.simpleweather.network.openweathermap.OpenweathermapService;
import com.dmitrytyunkov.simpleweather.view.WeatherView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherPresenter {
    private WeatherView weatherView;
    private OpenweathermapService openweathermapService;

    public WeatherPresenter(WeatherView weatherView, OpenweathermapService openweathermapService) {
        this.weatherView = weatherView;
        this.openweathermapService = openweathermapService;
    }

    public void checkWeather(String city, String lang, String unit, String appid) {
        openweathermapService.getBaseWeatherModel(city, unit, lang, appid).enqueue(new Callback<BaseWeatherModel>() {
            @Override
            public void onResponse(Call<BaseWeatherModel> call, Response<BaseWeatherModel> response) {
                if (response.code() != 200) {
                    Log.d("REQUEST ERROR", response.code() + ' ' + response.message());
                    weatherView.returnWeatherError(response.code() + ' ' + response.message());
                }
                else {
                    Log.d("REQUEST OK", response.body().toString());
                    weatherView.returnWeather(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseWeatherModel> call, Throwable t) {
                Log.d("REQUEST ERROR", t.getMessage());
                weatherView.returnWeatherError(t.getMessage());
            }
        });
    }

    public void checkWeather(Double longitude, Double latitude, String lang, String unit, String appid) {
        openweathermapService.getBaseWeatherModel(longitude, latitude, unit, lang, appid).enqueue(new Callback<BaseWeatherModel>() {
            @Override
            public void onResponse(Call<BaseWeatherModel> call, Response<BaseWeatherModel> response) {
                if (response.code() != 200) {
                    Log.d("REQUEST ERROR", response.code() + ' ' + response.message());
                    weatherView.returnWeatherError(response.code() + ' ' + response.message());
                }
                else {
                    Log.d("REQUEST OK", response.body().toString());
                    weatherView.returnWeather(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseWeatherModel> call, Throwable t) {
                Log.d("REQUEST ERROR", t.getMessage());
                weatherView.returnWeatherError(t.getMessage());
            }
        });
    }

    public void downloadImg(String imgUrl) {
        openweathermapService.getWeatherImg(imgUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() != 200)
                    Log.d("REQUEST ERROR", response.code() + ' ' + response.message());
                else {
                    weatherView.returnWeatherImg(BitmapFactory.decodeStream(response.body().byteStream()));
                    Log.d("REQUEST OK", "Image weather downloaded");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
