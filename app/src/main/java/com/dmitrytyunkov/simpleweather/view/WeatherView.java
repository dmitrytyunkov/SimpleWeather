package com.dmitrytyunkov.simpleweather.view;


import android.graphics.Bitmap;
import android.location.Location;

import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;

public interface WeatherView {
    void returnWeather(BaseWeatherModel baseWeatherModel);

    void returnWeatherError(String error);

    void returnWeatherImg(Bitmap bitmap);

    void returnLocation(Location location);

    void returnLocationError(String error);
}
