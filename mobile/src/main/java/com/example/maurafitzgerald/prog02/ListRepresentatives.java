package com.example.maurafitzgerald.prog02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ListRepresentatives extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.maurafitzgerald.prog02.MESSAGE";
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";


    //TODO: Remove fake zip code, implement location
    private Toolbar actionBar;
    public Representative[] reps = new Representative[3];
    private RepData repData;
    private Integer repID = null;
    private BroadcastReceiver activityReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_representatives);

        activityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                repID = (Integer) intent.getExtras().get("RepID");
                showRepresentative(repID);
            }
        };
        actionBar = (Toolbar) findViewById(R.id.list_reps_toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter(ACTION_STRING_ACTIVITY);
            registerReceiver(activityReceiver, intentFilter);
        }

        Intent intent = getIntent();

        this.repData = (RepData)intent.getSerializableExtra("DATA");
        Integer zip = repData.zipCode;
        getSupportActionBar().setTitle(zip.toString());

        this.repID = (Integer) intent.getExtras().get("CurrentRep");
        Log.d("in list representatives", "" + this.repID);


        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //textView.setText(message);

        final ListView repList = (ListView) findViewById(R.id.listReps);

        ArrayList<Representative> representatives = repData.getAllRepresentatives();

        final RepresentativeArrayAdapter adapter =
                new RepresentativeArrayAdapter(this, representatives);

        repList.setAdapter(adapter);
    }

    public void showRepresentative(int position) {
        Intent intent = new Intent(this, ShowRepresentative.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        intent.putExtra("DATA", repData);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                unregisterReceiver(activityReceiver);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop(){
        super.onStop();
        //unregisterReceiver(activityReceiver);
    }
}
