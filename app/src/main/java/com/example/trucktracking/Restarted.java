package com.example.trucktracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class Restarted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      //  Log.e("Ankush Broadcast", "Service tried to stop")
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyCustomService.class));
        } else {
            context.startService(new Intent(context,MyCustomService.class));
        }
    }
}
