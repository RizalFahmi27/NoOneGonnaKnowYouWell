package id.developer.tanitionary;

/**
 * Created by Naufal on 8/13/2016.
 */
public class ObjectWeather {

    String day, weather, temperature;

    public ObjectWeather(String day, String weather, String temperature){
        this.day = day;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getDay() {
        return day;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeather() {
        return weather;
    }
}
