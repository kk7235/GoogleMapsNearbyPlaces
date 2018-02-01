package com.androidtutorialpoint.googlemapsnearbyplaces;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button b;Context context;
    LocationManager locationManager ;
    boolean GpsStatus ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b= (Button) findViewById(R.id.button);
        context = getApplicationContext();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckGpsStatus() ;
                if(GpsStatus == true&&isNetworkConnected()==true)
                {
                    Intent i = new Intent(MainActivity.this,MapsActivity.class);
                    startActivity(i);
                }
                else {
                    if (isNetworkConnected() == true) {
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(MainActivity.this);
                        dialog1.setMessage("Location is not available");
                        dialog1.setTitle("Error");
                        dialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //this will navigate user to the device location settings screen
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert1 = dialog1.create();
                        alert1.show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("Error");
                        dialog.setMessage("Internet is not available");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //this will navigate user to the device location settings screen
                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert = dialog.create();
                        alert.show();
                    }


                }  }
        });

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    }

