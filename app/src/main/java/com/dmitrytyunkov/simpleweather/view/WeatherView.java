package com.dmitrytyunkov.simpleweather.view;


import android.graphics.Bitmap;

import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;

public interface WeatherView {
    void returnWeather(BaseWeatherModel baseWeatherModel);
    void returnWeatherError(String error);
    void returnWeatherImg(Bitmap bitmap);
}
