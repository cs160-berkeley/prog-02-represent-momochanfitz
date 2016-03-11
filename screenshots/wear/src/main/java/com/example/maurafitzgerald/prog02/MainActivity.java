package com.example.maurafitzgerald.prog02;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (delta > 5) {
                Intent sendIntent = new Intent(getApplicationContext(), WatchToPhoneService.class);
                sendIntent.putExtra("shaken", "true");
                startService(sendIntent);
            } else {
                Intent sendIntent = new Intent(getApplicationContext(), WatchToPhoneService.class);
                sendIntent.putExtra("shaken", "false");
                startService(sendIntent);
            }
//            if (mAccel > 12) {
//                //Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_LONG);
//                //toast.show();
//                Intent sendIntent = new Intent(getApplicationContext(), WatchToPhoneService.class);
//                sendIntent.putExtra("shaken", "true");
//                startService(sendIntent);
//            } else {
//                Intent sendIntent = new Intent(getApplicationContext(), WatchToPhoneService.class);
//                sendIntent.putExtra("shaken", "false");
//                startService(sendIntent);
//            }

        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private GridViewPager grid;

    public String[] names;
    public String[] parties;
    private boolean firstDataReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstDataReceived = false;

        grid = (GridViewPager) findViewById(R.id.pager);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

    }


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("d", "IN WATCH ON RECEIVE");
            Bundle data = intent.getBundleExtra("datamap");
            names = data.getStringArray("Names");
            Log.d("d", "Watch names: " + names[0]);
            parties = data.getStringArray("Parties");
            String romney = (String) data.get("Romney");
            String obama = (String) data.get("Obama");
            String county = (String) data.get("County");
            String state = (String) data.get("State");
            String[] roles = data.getStringArray("Roles");

            //if (!firstDataReceived) {
                grid.setAdapter(new GridAdapter(context, getFragmentManager(), names, parties, roles,
                        romney, obama, county, state));
                firstDataReceived = true;
            //}
        }
    }
}
