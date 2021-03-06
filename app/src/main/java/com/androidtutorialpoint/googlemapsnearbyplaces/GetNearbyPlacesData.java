package com.androidtutorialpoint.googlemapsnearbyplaces;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by navneet on 23/7/16.
 */
 public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    botttomcallback dataCallback;
    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            dataCallback = (botttomcallback) params[2];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
         ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        ArrayList<Markerpoints> ar=new ArrayList<>();
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Markerpoints m=new Markerpoints();
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));m.setLat(lat);
            double lng = Double.parseDouble(googlePlace.get("lng"));m.setLng(lng);
            String placeName = googlePlace.get("place_name");m.setPlace(placeName);
            String vicinity = googlePlace.get("vicinity");m.setViccnity(vicinity);
            String rating= googlePlace.get("rating");m.setRating(rating);
            String photo= googlePlace.get("photo");m.setPhoto(photo);
            String open=googlePlace.get("open");m.setOpen(open);
            LatLng latLng = new LatLng(lat, lng);
            ar.add(m);

            //markerOptions.position(latLng);
           // markerOptions.title(placeName + " : " + vicinity);

            //ar.add(placeName + " : " + vicinity);
            //mMap.addMarker(markerOptions);
           // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
           // mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
        dataCallback.read(ar);;
    }
}
