     package com.dmitrytyunkov.simpleweather.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dmitrytyunkov.simpleweather.R;
import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;
import com.dmitrytyunkov.simpleweather.network.openweathermap.NetworkOpenweathermapBuilder;
import com.dmitrytyunkov.simpleweather.network.openweathermap.OpenweathermapService;
import com.dmitrytyunkov.simpleweather.presenter.WeatherPresenter;
import com.dmitrytyunkov.simpleweather.view.WeatherView;

public class MainActivity extends AppCompatActivity implements WeatherView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkOpenweathermapBuilder.init("http://api.openweathermap.org/");
        OpenweathermapService service = NetworkOpenweathermapBuilder.getOpenweathermapService();

        WeatherPresenter weatherPresenter = new WeatherPresenter(this, service);
        weatherPresenter.checkWeather();
    }

    @Override
    public void returnWeather(BaseWeatherModel baseWeatherModel) {
        Toast.makeText(this, baseWeatherModel.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void returnWeatherError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
