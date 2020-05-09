package com.example.trucktracking;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
     DataBaseHelper myDb;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME= 10000;
    private final long MIN_DIST= 1;

    private LatLng latLng;
    //OneTimeWorkRequest request=new OneTimeWorkRequest.Builder(BackgroundWorker.class).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DataBaseHelper(this);
        setContentView(R.layout.activity_maps);
        startActivity(new Intent(this,BackgroundServiceActivity.class));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.SEND_SMS,Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
      /*  PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                BackgroundWorker.class,2, TimeUnit.MINUTES
        ).build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);*/
        // ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        try{
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
        catch(SecurityException e){

            e.printStackTrace();
        }
       /* locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String strDate = dateFormat.format(date);
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Here you are"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    String phoneNumber= "7717770429";
                    String myLatitude= String.valueOf(location.getLatitude());
                    String myLongitude= String.valueOf(location.getLongitude());
                    String message= "Latitude= "+myLatitude +" Longitude="+myLongitude;
                    SmsManager smsManager= SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                  double distance=  myDb.getDistance(myLatitude,myLongitude);
                 boolean isInserted=   myDb.insertData("9570669747",myLatitude,myLongitude,strDate,String.valueOf(distance) );
                  //  WorkManager.getInstance().enqueue(request);

                 if(isInserted){
                     Toast.makeText(MapsActivity.this,"data inseted",Toast.LENGTH_LONG).show();
                     showMessage("Total Distance Today: ",String.valueOf(distance));

                 }
                    else
                     Toast.makeText(MapsActivity.this,"data  not inseted",Toast.LENGTH_LONG).show();
                }
                catch(SecurityException e){

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
        };*/

       // locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        try {
//            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
//        }
//        catch(SecurityException securityException){
//
//            securityException.printStackTrace();
//
//        }
    }
    public void showMessage(String title,String message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        //builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }
}
