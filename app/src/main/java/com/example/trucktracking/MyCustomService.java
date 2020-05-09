package com.example.trucktracking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MyCustomService extends Service {


    private Timer timer;
    private TimerTask timerTask;
    private Integer counter = 0;
    DataBaseHelper myDb;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 10000;
    private final long MIN_DIST = 1;

    private LatLng latLng;

    public MyCustomService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopTimerTask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarted.class);
        this.sendBroadcast(broadcastIntent);
    }

    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                Log.e("Akash", "awdf");
                // displayNotification("Address","-");
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        try {
                            Date date = Calendar.getInstance().getTime();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                            String strDate = dateFormat.format(date);
                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //   mMap.addMarker(new MarkerOptions().position(latLng).title("Here you are"));
                            //    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                            String phoneNumber = "7717770429";
                            String myLatitude = String.valueOf(location.getLatitude());
                            String myLongitude = String.valueOf(location.getLongitude());
                            String message = "Latitude= " + myLatitude + " Longitude=" + myLongitude;
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                            double distance = myDb.getDistance(myLatitude, myLongitude);
                            boolean isInserted = myDb.insertData("9570669747", myLatitude, myLongitude, strDate, String.valueOf(distance));
                            //  WorkManager.getInstance().enqueue(request);
                            // Log.e("Akash",myLatitude+"  -"+myLongitude);

                            displayNotification("Address", myLatitude + "-" + myLongitude);
                        } catch (SecurityException e) {

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
              Handler mHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        try {
                            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
                        } catch (SecurityException securityException) {

                            securityException.printStackTrace();

                        }
                    }
                };

              
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    private void displayNotification(String task, String desc) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chanel = new NotificationChannel("abc", "def", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(chanel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "abc")
                    .setContentTitle(task)
                    .setContentText(desc)
                    .setSmallIcon(R.mipmap.ic_launcher);
            manager.notify(1, builder.build());

        }
    }

    private void stopTimerTask() {
        if (timer != null)
            timer.cancel();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
