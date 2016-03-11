package com.example.maurafitzgerald.prog02;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.ResultCallback;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;


public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();
    private Integer repID;
    private String shaken;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .build();
        //and actually connect it
        mWatchApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }

//    public int onStartCommand (Intent intent, int flags, int startId) {
//        repID = (Integer) intent.getExtras().get("RepID");
//        return START_STICKY;
//    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        Log.d("T", "in onconnected");
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                        Log.d("T", "found nodes");

                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Which cat do we want to feed? Grab this info from INTENT
        // which was passed over when we called startService
        //Bundle extras = intent.getExtras();


        this.repID = intent.getExtras().getInt("RepID");
        this.shaken = (String) intent.getExtras().get("shaken");
        // Send the message with the cat name
        new Thread(new Runnable() {
            @Override
            public void run() {
                //first, connect to the apiclient
                mWatchApiClient.connect();
                //Log.d("shaken from watch", ""+shaken);
                if (shaken != null && shaken.equals("true")) {
                    String WEARABLE_PATH = "/shaken";
                    sendMessage(WEARABLE_PATH, "shakeON");
                } else if (shaken != null && shaken.equals("false")) {
                    sendMessage("/shaken", "shakeOFF");
                } else {
                    Log.d("d", "SENDING TO PHONE");
                    String WEARABLE_PATH = "/send_toast";
                    sendMessage(WEARABLE_PATH, repID.toString());
                }

            }
        }).start();

        return START_STICKY;
    }

    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}

    private void sendMessage(final String path, final String text ) {

        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mWatchApiClient, node.getId(), path, text.getBytes());
        }
    }

}
