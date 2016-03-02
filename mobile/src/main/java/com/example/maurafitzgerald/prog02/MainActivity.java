package com.example.maurafitzgerald.prog02;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks {

    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    //private Button mFredButton;
    //private Button mLexyButton;
    private ImageButton goButton;
    private RepData repData;
    private GoogleApiClient mGoogleApiClient;
    private BroadcastReceiver mainReceiver;
    private static final String MAIN_STRING_ACTIVITY = "/shaken";
    private boolean shaken;



    // Key for sending messages
    public final static String FAKE_ZIPCODE = "94704";
    public final static String FAKE_RANDOM_ZIPCODE = "90266";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mainReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean shaken = (Boolean) intent.getExtras().get("Shaken");
                if (shaken != null && shaken.equals(true)) {
                    RepData newRepData = new RepData(FAKE_RANDOM_ZIPCODE);
                    newRepData.shuffleRepresentatives();
                    newRepData.county = "RANDOM COUNTY";
                    newRepData.state = "RANDOM STATE";
                    finishActivity(1010);
                    listRepresentatives(newRepData);
                }
            }
        };


        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        if (mainReceiver != null) {
            IntentFilter intentFilter = new IntentFilter(MAIN_STRING_ACTIVITY);
            registerReceiver(mainReceiver, intentFilter);
        }


        goButton = (ImageButton) findViewById(R.id.go);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch s = (Switch) findViewById(R.id.switch_location);
                String zipcode = "";
                if (s.isChecked()) {
                    zipcode = FAKE_ZIPCODE;
                } else {
                    EditText zipfield = (EditText) findViewById(R.id.zipcode);
                    zipcode = zipfield.getText().toString();
                    if (zipcode.equals(null) || zipcode.equals("")) {
                        //TODO: implement checking GPS for current location
                        zipcode = FAKE_ZIPCODE;
                    }
                }
                RepData repData = new RepData(zipcode);


                Intent intent = new Intent(getBaseContext(), PhoneToWatchService.class);
                intent.putExtra("DATA", repData);
                startService(intent);
                listRepresentatives(repData);
            }
        });


    }

    public void listRepresentatives(RepData repd) {
        Intent intent = new Intent(this, ListRepresentatives.class);
        // TODO: implement the location-based input

        intent.putExtra("DATA", repd);
        startActivityForResult(intent, 1010);
    }

    ;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Suspended", "Location services suspended. Please reconnect.");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyLocationListener implements LocationListener {
        //TODO: replace the Geocoder call with one to get latitude/longitude coords from API
        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("LONG", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("LAT", latitude);

        /*------- To get city name from coordinates -------- */
//            String cityName = null;
//            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.ENGLISH);
//            List<Address> addresses;
//            try {
//                addresses = gcd.getFromLocation(loc.getLatitude(),
//                        loc.getLongitude(), 1);
//                if (addresses.size() > 0) {
//                    System.out.println(addresses.get(0).getLocality());
//                    cityName = addresses.get(0).getLocality();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                    + cityName;

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

}

