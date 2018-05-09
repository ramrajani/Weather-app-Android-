package r_square_corporation.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    // Request Codes:
    final int REQUEST_CODE = 123;
    // Request Code for permission request callback
    final int NEW_CITY_CODE = 456;
    // Request code for starting new activity for result callback

    // Base URL for the OpenWeatherMap API. More secure https is a premium feature =(
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";

    // App ID to use OpenWeather data
    final String APP_ID = "10aadfc8cc32e5cae0b292dd5faa5e9a";

    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;

    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // Location provider here
    String Location_Provide = LocationManager.GPS_PROVIDER;

    // setting variables
    boolean mUseLocation = true;
    TextView mcityname;
    TextView mcitytemp;
    ImageView mweatherimg;
    ImageButton mchangecity;

    // Location Managaer and Location Listener
    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // assigning views
        mcityname = (TextView) findViewById(R.id.cityname);
        mcitytemp = (TextView) findViewById(R.id.tempcity);
        mweatherimg = (ImageView) findViewById(R.id.weatherimage);
        mchangecity = (ImageButton) findViewById(R.id.changecity);
        //

        mchangecity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent =new Intent(MainActivity.this,ChangeCity.class);
                startActivityForResult(myIntent,NEW_CITY_CODE);
            }
        });

    }

    // adding onresume method
    @Override
    protected void onResume() {
        super.onResume();
        Intent myInte = getIntent();
        String city = myInte.getStringExtra("citynam");
        if (city != null) {

            getWeatherbycityname(city);
        } else {

            getWeatherforCurrentLocation();


        }
    }

    private void getWeatherforCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String longitude =String.valueOf(location.getLongitude());
                String latitude =String.valueOf(location.getLatitude());
                RequestParams params =new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid",APP_ID);
                letsdoSomeNetworking(params);




            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provide, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length > 0&& grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getWeatherforCurrentLocation();
            }
        }
    }
    private void letsdoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client =new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
            updateUi(weatherData);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Toast.makeText(MainActivity.this,"Request Failed",Toast.LENGTH_SHORT).show();

            }



        });

    }

    //update ui
    private void updateUi(WeatherDataModel Weatherdata){
        mcityname.setText(Weatherdata.getCityname());
        mcitytemp.setText(Weatherdata.getTemperature());
        int resourceid = getResources().getIdentifier(Weatherdata.getIconname(),"drawable",getPackageName());

        mweatherimg.setImageResource(resourceid);


    }

    // getWeatherforcityname
    private void getWeatherbycityname(String city)
    {
        RequestParams params1 =new RequestParams();
        params1.put("q",city);
        params1.put("appid",APP_ID);
        letsdoSomeNetworking(params1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager !=null)
            mLocationManager.removeUpdates(mLocationListener);
    }
}
