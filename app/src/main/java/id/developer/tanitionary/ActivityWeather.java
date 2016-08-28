package id.developer.tanitionary;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import id.developer.tanitionary.R;
import id.developer.tanitionary.weather_model.Weather;

public class ActivityWeather extends AppCompatActivity implements WeatherCallbacks{
    Animation rotationAnimation;
    FragmentTransaction fragmentTransaction;
    FragmentForecast fg;
    LocationListener locationListener;
    LocationManager locationManager;
    long startTime, currentTime;
    final Handler timeHandler = new Handler();
    private boolean isReadyToUpdate=false;
    TextView lastUpdateText, temperatureText, currentWeatherText, cityName, dateText;
    ImageView updateIcon, weatherIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentTransaction = getFragmentManager().beginTransaction();
        fg = FragmentForecast.newInstance("weather");
        fragmentTransaction.replace(R.id.fragmentWeather,fg);
        fragmentTransaction.commit();

        //Initialize View
        lastUpdateText = (TextView) findViewById(R.id.lastUpdate);
        updateIcon = (ImageView) findViewById(R.id.updateButton);
        temperatureText = (TextView) findViewById(R.id.temperature);
        dateText = (TextView) findViewById(R.id.weatherDate);
        cityName = (TextView) findViewById(R.id.cityName);
        currentWeatherText = (TextView) findViewById(R.id.currentWeather);
        weatherIcon = (ImageView) findViewById(R.id.currentWeatherIcon);

        update();

    }

    @Override
    public void onMessageFromFragmentToActivity(String sender, String strValue) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureLocation();
                return;
        }
    }

    private void update(){

        checkLocationService();
        rotationAnimation = AnimationUtils.loadAnimation(this,R.anim.rotate_animation);
        updateIcon.setAnimation(rotationAnimation);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(ActivityWeather.this,"Lat : "+location.getLatitude()+" Lang : "+location.getLongitude(),Toast.LENGTH_SHORT).show();
                Log.d("Loc","Lat : "+location.getLatitude()+" Lang : "+location.getLongitude());
                isReadyToUpdate = true;
                rotationAnimation.cancel();
                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(new String[]{String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude())});
            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        updateLocation();
        //startTime = SystemClock.elapsedRealtime();
        //Log.d("Timer","Start : "+SystemClock.elapsedRealtime());
        final Runnable updateTime = new Runnable() {
            int time=0;
            @Override
            public void run() {
                time+=300000;
                    lastUpdateText.setText("Terakhir diperbarui " + time + " detik lalu");
                    Log.d("Timer", "" + currentTime);
                    timeHandler.postDelayed(this,300000);
            }
        };
        timeHandler.postDelayed(updateTime,300000);
    }



    private void checkLocationService() {
        boolean network_enabled=false;

        int locationMode = 0;
        String locationProviders;
        try {
//            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                try {
                    locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }

                network_enabled = (locationMode != Settings.Secure.LOCATION_MODE_OFF);

            }else{
                locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                network_enabled= (!TextUtils.isEmpty(locationProviders));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(!network_enabled){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Mohon aktifkan location terlbih dahulu");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Aktifkan Lokasi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }
    }

    private void updateLocation(){


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(ActivityWeather.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                },10);
                return;
            }
        }
        configureLocation();
    }

    private void configureLocation() {
        Looper looper = Looper.myLooper();
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,looper);
        final Handler myHandler = new Handler(looper);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locationManager.removeUpdates(locationListener);
                Toast.makeText(ActivityWeather.this,"Gagal Memeperbarui Cuaca",Toast.LENGTH_SHORT).show();
                isReadyToUpdate = false;
                rotationAnimation.cancel();
            }
        },120000);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        Log.d("Loc","Lat : Got here");


    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = (( new WeatherHttpClient()).getWeatherData(params[0],params[1]));
            try{

                weather = JSONWeatherParser.getWeather(data);

                weather.iconData = (( new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if(weather.iconData != null && weather.iconData.length>0){
                Bitmap bmp = BitmapFactory.decodeByteArray(weather.iconData,0,weather.iconData.length);
                weatherIcon.setImageBitmap(bmp);
            }

            cityName.setText(weather.location.getCity()+","+weather.location.getCountry());
            currentWeatherText.setText(weather.currentCondition.getCondition() + "("+ weather.currentCondition.getDescr()+")");
            temperatureText.setText(""+ Math.round((weather.temperature.getTemp()))+ (char) 0x00B0+")");
        }
    }
}
