package com.example.maurafitzgerald.prog02;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.kimo.lib.faker.Faker;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "qBftKBQCoKHGtmECAgIJphxTd";
    private static final String TWITTER_SECRET = "of5YLaDjE6KGrtjtmkoY68sY8nUHTpjU6WGVt4p6LKMZ2v2QrL";


    private ImageButton goButton;
    private GoogleApiClient mGoogleApiClient;

    private BroadcastReceiver mainReceiver;
    private Location lastLoc;
    private double latFromLoc;
    private double lngFromLoc;
    private LocationListener locListener;
    private LocationManager locManager;
    private MyLocation myLoc;
    private TwitterLoginButton loginButton;
    private boolean loggedIn = false;
    private String obamaVote = "";
    private String romneyVote = "";
    private boolean votesComplete = false;
    private boolean listStarted;



    private static final String MAIN_STRING_ACTIVITY = "/shaken";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //listStarted = true;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);


        TextView text = (TextView) findViewById(R.id.currentLoc);
        TextView or = (TextView) findViewById(R.id.or);
        EditText zip = (EditText) findViewById(R.id.zipcode);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "FaunaOne-Regular.ttf");

        text.setTypeface(custom_font);
        or.setTypeface(custom_font);
        zip.setTypeface(custom_font);

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


        mainReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Boolean shaken = (Boolean) intent.getExtras().get("Shaken");
                if (shaken != null && shaken.equals(true)) {
                    votesComplete = false;
                    Log.d("d", "WATCH SHAKEN");
                    // TODO: replace this with a RANDOM zip code generated
                    String zipcode = Faker.with(getBaseContext()).Address.zipCode();
                    Log.d("d", "FAKER ZIP: " + zipcode);
                    zipcode = zipcode + ", United States";
                    final Geocoder geocoder = new Geocoder(getBaseContext());
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(zipcode, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            double lat = address.getLatitude();
                            double lng = address.getLongitude();
                            setLocation(lat, lng, "");
                        }
                    } catch (IOException e) {
                        // handle exception
                    }
                    RepData newRepData = new RepData(myLoc);
                    while (newRepData.apiComplete == false  || votesComplete == false) {
                    }
                    newRepData.percent_obama = obamaVote;
                    newRepData.percent_romney = romneyVote;

                    Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    watchIntent.putExtra("DATA", newRepData);
                    finishActivity(1010);
                    startService(watchIntent);
                    listRepresentatives(newRepData);
                }
            }
        };

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        locListener = locationListener;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locManager = locationManager;
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
                if (loggedIn == false) {
                    Toast.makeText(getBaseContext(),
                            "Please log in to Twitter.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(),
                            "Loading representatives...",
                            Toast.LENGTH_SHORT).show();
                    Switch s = (Switch) findViewById(R.id.switch_location);
                    EditText zipfield = (EditText) findViewById(R.id.zipcode);
                    String zipcode = zipfield.getText().toString();
                    //Log.d("d","LastLoc: " + lastLoc.toString());

                    if ((!s.isChecked() && zipcode.equals("")) || (s.isChecked() && lastLoc == null)) {
                        Toast.makeText(getBaseContext(),
                                "Please enter a valid zip code or allow use of your location.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (s.isChecked()) {
                            zipcode = "";
                            setLocation(latFromLoc, lngFromLoc, zipcode);
                        } else if (!zipcode.equals("")) {
                            String zipPlain = zipcode.toString();
                            zipcode = zipcode + ", United States";
                            final Geocoder geocoder = new Geocoder(getBaseContext());
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(zipcode, 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    double lat = address.getLatitude();
                                    double lng = address.getLongitude();
                                    setLocation(lat, lng, zipPlain);
                                }
                            } catch (IOException e) {
                                // handle exception
                            }
                        }
                        RepData finalData;
                        RepData repData = new RepData(myLoc);
                        while (repData.apiComplete == false || votesComplete == false) {
                        }
                        if (repData.emptyAPI) {
                            myLoc.zip = "";
                            RepData repData1 = new RepData(myLoc);
                            while (repData1.apiComplete == false) {
                            }
                            finalData = repData1;
                        } else {
                            finalData = repData;
                        }

                        finalData.percent_obama = obamaVote;
                        finalData.percent_romney = romneyVote;

                        Intent intent = new Intent(getBaseContext(), PhoneToWatchService.class);
                        intent.putExtra("DATA", finalData);
                        startService(intent);

                        listRepresentatives(finalData);
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loggedIn = true;
        loginButton.onActivityResult(requestCode, resultCode, data);
        loginButton.setVisibility(View.INVISIBLE);
    }


    public void setLocation(double lat, double lng, String zip) {
        final Geocoder geocoder = new Geocoder(getBaseContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    lat, lng, 10);
            Address address = null;
            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getSubAdminArea() != null) {
                    address = addresses.get(i);
                    break;
                }
            }
            Log.d("d", "IN SET LOCATION");
            String county = String.valueOf(address.getSubAdminArea());
            String state = String.valueOf(address.getAdminArea());
            String dataCounty = county.replace(" County", "");
            new getElectionData(dataCounty).execute();
            this.myLoc = new MyLocation(county, state, lat, lng, zip);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }


    public void listRepresentatives(RepData repd) {
        Intent intent = new Intent(this, ListRepresentatives.class);
        // TODO: implement the location-based input
        intent.putExtra("DATA", repd);
        startActivityForResult(intent, 1010);
    }


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
        Log.d("on", "ON CONNECTED");

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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void setVotes(String obama, String romney) {
        this.obamaVote = obama;
        this.romneyVote = romney;
        this.votesComplete = true;
    }

    private class getElectionData extends AsyncTask<String, Void, String> {
        String county;
        public getElectionData(String my_county) {
            this.county = my_county;
        }

        protected String doInBackground(String ... strings) {
            try {
                InputStream stream =
                        getApplicationContext().getAssets().open("election_data.json");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                String dataReturned = stringBuilder.toString();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(dataReturned);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String currentCounty = jsonObject.optString("county-name");
                        if (currentCounty.equals(county)) {
                            String obama_vote = jsonObject.optString("obama-percentage");
                            String romney_vote = jsonObject.optString("romney-percentage");
                            setVotes(obama_vote, romney_vote);
                            break;
                        }
                    }
                } catch (JSONException e) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "finished";
        }

        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(String result) {
        }
    }


    private class MyLocationListener implements LocationListener {
        //TODO: replace the Geocoder call with one to get latitude/longitude coords from API
        @Override
        public void onLocationChanged(Location loc) {

            lastLoc = loc;
            lngFromLoc = loc.getLongitude();
            //Log.v("LONG", lngFromLoc);
            latFromLoc = loc.getLatitude();
            //Log.v("LAT", latFromLoc);

            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
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

