     package com.dmitrytyunkov.simpleweather.ui;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmitrytyunkov.simpleweather.R;
import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;
import com.dmitrytyunkov.simpleweather.network.openweathermap.NetworkOpenweathermapBuilder;
import com.dmitrytyunkov.simpleweather.network.openweathermap.OpenweathermapService;
import com.dmitrytyunkov.simpleweather.presenter.WeatherPresenter;
import com.dmitrytyunkov.simpleweather.view.WeatherView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
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
    @BindView(R.id.image_view_weather)
    ImageView imageViewWeather;
    private Unbinder unbinder;

    private Double perssureKoef = 0.750062;
    private String unit = "metric";
    private String city = "Omsk,RU";
    private String lang = "en";
    private String appid = "2848389bb79b98268b336c39d6eea8c7";

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

        if (Locale.getDefault().getLanguage().equals("ru"))
            lang = "ru";

        NetworkOpenweathermapBuilder.init("http://api.openweathermap.org/");
        OpenweathermapService service = NetworkOpenweathermapBuilder.getOpenweathermapService();

        WeatherPresenter weatherPresenter = new WeatherPresenter(this, service);
        weatherPresenter.checkWeather(city, lang, unit, appid);
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
        if(unit.equals("metric"))
            str = baseWeatherModel.getWind().getSpeed().toString() + " "
                    + getString(R.string.unit_speed_metric) + ", ";
        else
            str = baseWeatherModel.getWind().getSpeed().toString() + " "
                    + getString(R.string.unit_speed_imperial) + ", ";
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
        str = String.valueOf(Math.round(baseWeatherModel.getMain().getTemp()));
        textViewTemperature.setText(str);
        str = baseWeatherModel.getWeather().get(0).getDescription();
        textViewWeatherStatus.setText(str);
        /*str = baseWeatherModel.getName();
        textViewCity.setText(str);*/

        NetworkOpenweathermapBuilder.init("https://openweathermap.org/");
        OpenweathermapService service = NetworkOpenweathermapBuilder.getOpenweathermapService();
        String imgUrl = "/img/w/" + baseWeatherModel.getWeather().get(0).getIcon() + ".png";

        WeatherPresenter weatherPresenter = new WeatherPresenter(this, service);
        weatherPresenter.downloadImg(imgUrl);
    }

    @Override
    public void returnWeatherImg(Bitmap bitmap) {
        imageViewWeather.setImageBitmap(bitmap);
    }

    @Override
    public void returnWeatherError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @OnCheckedChanged(R.id.toggle_button_unit)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            unit = "imperial";
            NetworkOpenweathermapBuilder.init("http://api.openweathermap.org/");
            OpenweathermapService service = NetworkOpenweathermapBuilder.getOpenweathermapService();

            WeatherPresenter weatherPresenter = new WeatherPresenter(this, service);
            weatherPresenter.checkWeather(city, lang, unit, appid);
        }
        else {
            unit = "metric";
            NetworkOpenweathermapBuilder.init("http://api.openweathermap.org/");
            OpenweathermapService service = NetworkOpenweathermapBuilder.getOpenweathermapService();

            WeatherPresenter weatherPresenter = new WeatherPresenter(this, service);
            weatherPresenter.checkWeather(city, lang, unit, appid);
        }
    }
}
