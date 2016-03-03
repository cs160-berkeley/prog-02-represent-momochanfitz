package com.example.maurafitzgerald.prog02;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by maurafitzgerald on 2/20/16.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TOAST = "/send_toast";
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";
    private static final String MAIN_STRING_ACTIVITY = "/shaken";



    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(TOAST) ) {
            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            sendIntegerBroadcast(Integer.parseInt(value), ACTION_STRING_ACTIVITY);

        } else if (messageEvent.getPath().equalsIgnoreCase(MAIN_STRING_ACTIVITY)) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            if (value.equals("shakeON")) {
                sendBooleanBroadcast(true, MAIN_STRING_ACTIVITY);
            } else if (value.equals("shakeOFF")) {
                sendBooleanBroadcast(false, MAIN_STRING_ACTIVITY);
            }
        }  else {
            super.onMessageReceived( messageEvent );
        }

    }

    private void sendIntegerBroadcast(Integer repId, String action) {
        Intent new_intent = new Intent();
        new_intent.setAction(action);
        new_intent.putExtra("RepID", repId);
        sendBroadcast(new_intent);
    }

    private void sendBooleanBroadcast(Boolean shaken, String action) {
        Intent new_intent = new Intent();
        new_intent.setAction(action);
        new_intent.putExtra("Shaken", shaken);
        sendBroadcast(new_intent);
    }


}
