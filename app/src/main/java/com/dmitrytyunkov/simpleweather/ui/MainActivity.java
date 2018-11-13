     package com.dmitrytyunkov.simpleweather.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dmitrytyunkov.simpleweather.R;
import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;
import com.dmitrytyunkov.simpleweather.network.openweathermap.NetworkOpenweathermapBuilder;
import com.dmitrytyunkov.simpleweather.network.openweathermap.OpenweathermapService;
import com.dmitrytyunkov.simpleweather.presenter.WeatherPresenter;
import com.dmitrytyunkov.simpleweather.view.WeatherView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

     public class MainActivity extends AppCompatActivity implements WeatherView {

    @BindView(R.id.text_view_chance_of_precipitation_value)
    TextView textViewChanceOfPrecipitation;
    @BindView(R.id.text_view_city)
    TextView textViewCity;
    @BindView(R.id.text_view_pressure_value)
    TextView textViewPressure;
    @BindView(R.id.text_view_humidity_value)
    TextView textViewHumidity;
    @BindView(R.id.text_view_temperature)
    TextView textViewTemperature;
    @BindView(R.id.text_view_wind_value)
    TextView textViewWind;
    @BindView(R.id.text_view_weather_status)
    TextView textViewWeatherStatus;
    private Unbinder unbinder;

    private Double perssureKoef = 0.750062;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        String str = getString(R.string.no_value) + " " + getString(R.string.percent);
        textViewChanceOfPrecipitation.setText(str);
        str = getString(R.string.no_value) + " " + getString(R.string.percent);
        textViewHumidity.setText(str);
        str = getString(R.string.no_value) + " " + getString(R.string.unit_pressure);
        textViewPressure.setText(str);
        str = getString(R.string.no_value) + " " + getString(R.string.unit_speed_metric) + ", "
                + getString(R.string.north);
        textViewWind.setText(str);
        str = getString(R.string.no_value);
        textViewTemperature.setText(str);
        str = getString(R.string.no_value);
        textViewWeatherStatus.setText(str);

        NetworkOpenweathermapBuilder.init("http://api.openweathermap.org/");
        OpenweathermapService service = NetworkOpenweathermapBuilder.getOpenweathermapService();

        WeatherPresenter weatherPresenter = new WeatherPresenter(this, service);
        weatherPresenter.checkWeather();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void returnWeather(BaseWeatherModel baseWeatherModel) {
        String str = baseWeatherModel.getMain().getHumidity().toString() + " "
                + getString(R.string.percent);
        textViewHumidity.setText(str);
        str = String.valueOf(Math.round(baseWeatherModel.getMain().getPressure() * perssureKoef))
                + " " + getString(R.string.unit_pressure);
        textViewPressure.setText(str);
        str = baseWeatherModel.getWind().getSpeed().toString() + " "
                + getString(R.string.unit_speed_metric) + ", ";
        Integer windDeg = baseWeatherModel.getWind().getDeg();
        if (windDeg >= 337.5 || windDeg < 22.5)
            str += getString(R.string.north);
        else if (windDeg < 67.5)
            str += getString(R.string.northeast);
        else if(windDeg < 112.5)
            str += getString(R.string.east);
        else if(windDeg < 157.5)
            str += getString(R.string.southeast);
        else if(windDeg < 202.5)
            str += getString(R.string.south);
        else if(windDeg < 247.5)
            str += getString(R.string.southwest);
        else if(windDeg < 292.5)
            str += getString(R.string.west);
        else if(windDeg < 337.5)
            str += getString(R.string.northwest);
        textViewWind.setText(str);
        str = baseWeatherModel.getMain().getTemp().toString();
        textViewTemperature.setText(str);
        str = baseWeatherModel.getWeather().get(0).getDescription();
        textViewWeatherStatus.setText(str);
    }

    @Override
    public void returnWeatherError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
