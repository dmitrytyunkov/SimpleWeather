package com.dmitrytyunkov.simpleweather.presenter;

import android.util.Log;

import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;
import com.dmitrytyunkov.simpleweather.network.openweathermap.OpenweathermapService;
import com.dmitrytyunkov.simpleweather.view.WeatherView;

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

    public void checkWeather() {
        // Заменить параметры запроса на переменные
        openweathermapService.getBaseWeatherModel("Omsk,ru", "metric", "ru", "2848389bb79b98268b336c39d6eea8c7").enqueue(new Callback<BaseWeatherModel>() {
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
                Log.d("ERROR", t.getMessage());
                weatherView.returnWeatherError(t.getMessage());
            }
        });
    }
}
