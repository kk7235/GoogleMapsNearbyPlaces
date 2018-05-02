package com.androidtutorialpoint.googlemapsnearbyplaces;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = "GeofenceTransitions";

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "onHandleIntent");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            //String errorMessage = GeofenceErrorMessages.getErrorString(this,
            //      geofencingEvent.getErrorCode());
            Log.e(TAG, "Goefencing Error " + geofencingEvent.getErrorCode());
            return;
        }
        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        Location location =geofencingEvent.getTriggeringLocation();
     //  List<Geofence> geofenceList=geofencingEvent.getTriggeringGeofences();
        Markerpoints geofenceDomain=getGeofenceDomain(location);

        Log.i(TAG, "geofenceTransition = " + geofenceTransition + " Enter : " + Geofence.GEOFENCE_TRANSITION_ENTER + "Exit : " + Geofence.GEOFENCE_TRANSITION_EXIT);
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL){

            showNotification("You are 500M away from", geofenceDomain.getPlace());

        } else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            Log.i(TAG, "Showing Notification...");
         //   showNotification("Grand Hyper Mart", geofenceDomain.getExitMessage());
        } else {
            // Log the error.
           // showNotification("Error", "Error");
            Log.e(TAG, "Error ");
        }
    }

    private Markerpoints getGeofenceDomain(Location location) {






          Markerpoints nearestLocation = PublicValues.m.get(0);
            Float smalldistance = getDistanceFromLatLonInKm(nearestLocation.getLat(),nearestLocation.getLng(),location.getLatitude(),location.getLongitude());

        for (int i=1;i<PublicValues.m.size();i++){

           Markerpoints locationDomain = PublicValues.m.get(i);
            Float distance = getDistanceFromLatLonInKm(location.getLatitude(), location.getLongitude(), locationDomain.getLat(), locationDomain.getLng());

            if (distance < smalldistance) {
                smalldistance = distance;
                nearestLocation = locationDomain;
            }
        }
        return nearestLocation;
    }


    public void showNotification(String text, String bigText) {

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        //Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Bar locator")
                .setContentText(text)
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }
 public static float getDistanceFromLatLonInKm(Double currentLat, Double currentLon, Double otherLat, Double otherLon) {

    String distance = "";
    //  if (!currentLat.isEmpty() && !currentLon.isEmpty() && !otherLat.isEmpty() && !otherLon.isEmpty()) {
    Location currentLocation = new Location("locA");
    currentLocation.setLatitude(currentLat);
    currentLocation.setLongitude(currentLon);

    Location otherLocation = new Location("locB");
    otherLocation.setLatitude(otherLat);
    otherLocation.setLongitude(otherLon);

    distance = String.format("%.2f", currentLocation.distanceTo(otherLocation) / 1000);
    //  }
    return Float.parseFloat(distance);
}}

