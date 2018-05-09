package r_square_corporation.weather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SONY on 25-12-2017.
 */

public class WeatherDataModel {
    // Member Variables
  private   String mTemperature;
    private String mCityname;
    private String mIconname;
    private  int  mCondition;
    // Weather data model from jason
    public  static  WeatherDataModel fromJson(JSONObject jsonObject){
        WeatherDataModel weatherdata = new WeatherDataModel();
        try {


            weatherdata.mCityname = jsonObject.getString("name");
            weatherdata.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherdata.mIconname= updateWeatherIcon(weatherdata.mCondition);
            double tempResult =jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedvalue =(int) Math.rint(tempResult);
            weatherdata.mTemperature =Integer.toString(roundedvalue);





            return weatherdata;
        }
        catch(JSONException e)
        {
            return null;

        }
    }


    // Get the weather image name from OpenWeatherMap's condition (marked by a number code)
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    public String getTemperature() {
        return mTemperature+"°";
    }

    public String getCityname() {
        return mCityname;
    }

    public String getIconname() {
        return mIconname;
    }
}
