package com.example.trucktracking;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BackgroundServiceActivity extends AppCompatActivity {
    private Intent mServiceIntent ;
    private MyCustomService mYourService;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_service);
        init();
    }

    private void init() {
        mYourService =new MyCustomService();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning( Class<?> serviceClass) {
        ActivityManager manager =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName() == service.service.getClassName()) {
                Log.e("Ankush Service status", "Running");
                return true;
            }
        }
        Log.e("Ankush Service status", "Not running");
        return false ;
    }
}
