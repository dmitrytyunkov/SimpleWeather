package com.dmitrytyunkov.simpleweather.ui;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dmitrytyunkov.simpleweather.R;
import com.dmitrytyunkov.simpleweather.location.CurrentLoccation;
import com.dmitrytyunkov.simpleweather.model.BaseWeatherModel;
import com.dmitrytyunkov.simpleweather.network.openweathermap.NetworkOpenweathermapBuilder;
import com.dmitrytyunkov.simpleweather.network.openweathermap.OpenweathermapService;
import com.dmitrytyunkov.simpleweather.presenter.WeatherPresenter;
import com.dmitrytyunkov.simpleweather.view.WeatherView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTouch;
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
    private Double longitude = 181.00;
    private Double latitude = 91.00;
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

        if (longitude > 180 || latitude > 90)
            checkWeatherByCityName();
        else
            checkWeatherByCoord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void returnWeather(BaseWeatherModel baseWeatherModel) {
        city = baseWeatherModel.getName() + "," + baseWeatherModel.getSys().getCountry();
        longitude = baseWeatherModel.getCoord().getLon();
        latitude = baseWeatherModel.getCoord().getLat();

        String str = String.valueOf(Math.round(baseWeatherModel.getMain().getHumidity()))
                + " " + getString(R.string.percent);
        textViewHumidity.setText(str);
        str = String.valueOf(Math.round(baseWeatherModel.getMain().getPressure() * perssureKoef))
                + " " + getString(R.string.unit_pressure);
        textViewPressure.setText(str);
        if (unit.equals("metric"))
            str = baseWeatherModel.getWind().getSpeed().toString() + " "
                    + getString(R.string.unit_speed_metric) + ", ";
        else
            str = baseWeatherModel.getWind().getSpeed().toString() + " "
                    + getString(R.string.unit_speed_imperial) + ", ";
        Double windDeg = baseWeatherModel.getWind().getDeg();
        if (windDeg >= 337.5 || windDeg < 22.5)
            str += getString(R.string.north);
        else if (windDeg < 67.5)
            str += getString(R.string.northeast);
        else if (windDeg < 112.5)
            str += getString(R.string.east);
        else if (windDeg < 157.5)
            str += getString(R.string.southeast);
        else if (windDeg < 202.5)
            str += getString(R.string.south);
        else if (windDeg < 247.5)
            str += getString(R.string.southwest);
        else if (windDeg < 292.5)
            str += getString(R.string.west);
        else if (windDeg < 337.5)
            str += getString(R.string.northwest);
        textViewWind.setText(str);
        str = String.valueOf(Math.round(baseWeatherModel.getMain().getTemp()));
        textViewTemperature.setText(str);
        str = baseWeatherModel.getWeather().get(0).getDescription();
        textViewWeatherStatus.setText(str);
        str = baseWeatherModel.getName();
        textViewCity.setText(str);

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
    public void returnLocation(Location location) {
        Log.d("LOCATION", location.getLongitude() + " "
                + location.getLatitude());
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        checkWeatherByCoord();
    }

    @Override
    public void returnLocationError(String error) {
        Log.d("LOCATION", error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void returnWeatherError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @OnCheckedChanged(R.id.toggle_button_unit)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            unit = "imperial";
        } else {
            unit = "metric";
        }
        checkWeatherByCoord();
    }

    @OnClick(R.id.text_view_my_location)
    public void onClickMyLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 1);
            }
            return;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        CurrentLoccation.getLocation(locationManager, this);
    }

    @OnClick(R.id.text_view_change_city)
    public void onClickChangeCity(View view) {
        String[] cities = {"Omsk,ru", "Novosibirsk,ru", "New York,us"};

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = layoutInflater.inflate(R.layout.popup_select_cities, null);

        final PopupWindow popupWindow = new PopupWindow(layout,
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                true);

        ((AutoCompleteTextView) layout.findViewById(R.id.auto_complete_text_view))
                .setAdapter(new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line, cities));

        ((AutoCompleteTextView) layout.findViewById(R.id.auto_complete_text_view)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                city = ((AutoCompleteTextView) layout.findViewById(R.id.auto_complete_text_view)).getText().toString();
                popupWindow.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(layout, 0, 0, Gravity.NO_GRAVITY);
        }
        else {
            popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                checkWeatherByCityName();
            }
        });
    }

    public void checkWeatherByCityName() {
        NetworkOpenweathermapBuilder.init("http://api.openweathermap.org/");
        OpenweathermapService service = NetworkOpenweathermapBuilder.getOpenweathermapService();

        WeatherPresenter weatherPresenter = new WeatherPresenter(this, service);
        weatherPresenter.checkWeather(city, lang, unit, appid);
    }

    public void checkWeatherByCoord() {
        NetworkOpenweathermapBuilder.init("http://api.openweathermap.org/");
        OpenweathermapService service = NetworkOpenweathermapBuilder.getOpenweathermapService();

        WeatherPresenter weatherPresenter = new WeatherPresenter(this, service);
        weatherPresenter.checkWeather(longitude, latitude, lang, unit, appid);
    }
}
