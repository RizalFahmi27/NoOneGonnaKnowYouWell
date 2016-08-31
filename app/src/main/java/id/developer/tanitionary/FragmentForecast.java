package id.developer.tanitionary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.developer.tanitionary.weather_model.Weather;

/**
 * Created by Rizal Fahmi on 21-Aug-16.
 */
public class FragmentForecast extends Fragment implements WeatherFragmentCallbacks {


    String[] days = new String[]{"Ming","Sen","Sel","Rabu","Kam","Jum","Sab"};
    ActivityWeather activityWeather;
    Context context;
    TextView day[] = new TextView[5];
    TextView curr[] = new TextView[5];
    TextView temp[] = new TextView[5];
    ImageView icon[] = new ImageView[5];
    LinearLayout layoutDay[] = new LinearLayout[5];
    Weather myWeather;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_5days_forecast,null);
        day[0] = (TextView) layout.findViewById(R.id.day1);
        day[1] = (TextView) layout.findViewById(R.id.day2);
        day[2] = (TextView) layout.findViewById(R.id.day3);
        day[3] = (TextView) layout.findViewById(R.id.day4);
        day[4] = (TextView) layout.findViewById(R.id.day5);

        curr[0] = (TextView) layout.findViewById(R.id.weatherFragment1);
        curr[1] = (TextView) layout.findViewById(R.id.weatherFragment2);
        curr[2] = (TextView) layout.findViewById(R.id.weatherFragment3);
        curr[3] = (TextView) layout.findViewById(R.id.weatherFragment4);
        curr[4] = (TextView) layout.findViewById(R.id.weatherFragment5);

        temp[0] = (TextView) layout.findViewById(R.id.temperatureFragment1);
        temp[1] = (TextView) layout.findViewById(R.id.temperatureFragment2);
        temp[2] = (TextView) layout.findViewById(R.id.temperatureFragment3);
        temp[3] = (TextView) layout.findViewById(R.id.temperatureFragment4);
        temp[4] = (TextView) layout.findViewById(R.id.temperatureFragment5);

        icon[0] = (ImageView) layout.findViewById(R.id.weatherIconFragment1);
        icon[1] = (ImageView) layout.findViewById(R.id.weatherIconFragment2);
        icon[2] = (ImageView) layout.findViewById(R.id.weatherIconFragment3);
        icon[3] = (ImageView) layout.findViewById(R.id.weatherIconFragment4);
        icon[4] = (ImageView) layout.findViewById(R.id.weatherIconFragment5);

        layoutDay[0] = (LinearLayout) layout.findViewById(R.id.layoutday1);
        layoutDay[1] = (LinearLayout) layout.findViewById(R.id.layoutday2);
        layoutDay[2] = (LinearLayout) layout.findViewById(R.id.layoutday3);
        layoutDay[3] = (LinearLayout) layout.findViewById(R.id.layoutday4);
        layoutDay[4] = (LinearLayout) layout.findViewById(R.id.layoutday5);



        return layout;
    }

    public static FragmentForecast newInstance(String strArgs){
        FragmentForecast fragmentForecast = new FragmentForecast();
        Bundle args = new Bundle();
        args.putString("stringArgs1",strArgs);
        fragmentForecast.setArguments(args);
        return fragmentForecast;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            context = getActivity();
            activityWeather = (ActivityWeather) getActivity();
        }
        catch (IllegalStateException e){
            throw new IllegalStateException("Activity must implement callbacks");
        }



    }

    @Override
    public void onMessageFromActivityToFragment(Weather weather) {

        myWeather = weather;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());

        for(int i=0;i<5;i++){
            if (weather.iconData[i] != null && weather.iconData[i].length > 0) {
                Bitmap bmp = BitmapFactory.decodeByteArray(weather.iconData[i], 0, weather.iconData.length);
                icon[i].setImageBitmap(bmp);
                Log.d("URL", "This message will appear if icon is loaded successfully");
            }
            String dayNow = days[(calendar.get(Calendar.DAY_OF_WEEK)-1)+i];
            curr[i].setText(weather.currentCondition[i].getCondition() + "\n(" + weather.currentCondition[i].getDescr() + ")");
            Log.d("URL", "Temp get : " + weather.temperature[i].getTemp());
            temp[i].setText("" + (weather.temperature[i].getTemp()) + (char) 0x00B0);
            day[i].setText(dayNow);
        }

        for(int i=0;i<5;i++){
            final int index = i;
            layoutDay[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDay(index);
                }
            });
        }
    }

    private void selectDay(int index){
        layoutDay[index].setBackgroundColor(Color.parseColor("#056107"));

        Bundle bundle = new Bundle();
        bundle.putString("desc",myWeather.currentCondition[index].getDescr());
        bundle.putString("condition",myWeather.currentCondition[index].getCondition());
        bundle.putString("temp",myWeather.temperature[index].getTemp());
        bundle.putInt("date",index);
        activityWeather.onMessageFromFragmentToActivity("daily",bundle);

        //Unset the rest layout
        for(int i=0;i<5;i++){
            if(i!=index)
            layoutDay[i].setBackgroundColor(Color.parseColor("#1b9a1e"));
        }
    }
}
