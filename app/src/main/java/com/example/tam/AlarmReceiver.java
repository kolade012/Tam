package com.example.tam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method will be called when the alarm is triggered
        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show();

        // You can perform any specific actions here when the alarm triggers
        // For example, show a notification, play a sound, etc.
        // This depends on your application's requirements
    }
}

