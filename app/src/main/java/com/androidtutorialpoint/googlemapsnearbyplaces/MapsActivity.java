package com.androidtutorialpoint.googlemapsnearbyplaces;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MapsActivity extends FragmentActivity implements botttomcallback,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener,
        LocationListener {
    private static final String TAG ="" ;
    int count = 0;int width;
    int height;CircleIndicator circleIndicator;
String currentLat ,currentLon;
Marker current,temp;    LatLng latlong1;
    private GoogleMap mMap;
    double  latitude;    ViewPager viewPager;
    double longitude;   ProgressDialog progressDialog;
    private int PROXIMITY_RADIUS = 10000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;ImageView navigate;
    LocationRequest mLocationRequest;
    private BottomSheetBehavior mBottomSheetBehavior1;
    BottomSheetDialog b;
    TextView btexview; View parentview;
    View bottomSheet;LatLngBounds bounds;
    RecyclerView r;    ArrayList<Marker> markersArrayList = new ArrayList<>();
    ArrayList<MarkerOptions> markerOps=new ArrayList<>();
    Button mButton1;    LatLng origin, dest;
    TextView  iv_trigger,storeName, distance;    BitmapDescriptor icon = null, iconSelected = null;
    CoordinatorLayout coordinatorLayout;ArrayList<Markerpoints> er=new ArrayList<>();
PagerAdapter pagerAdapter;    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      MapsInitializer.initialize(MapsActivity.this);
        setContentView(R.layout.activity_maps);
        View bottomSheet = findViewById(R.id.bottom);
        fragmentManager = this.getSupportFragmentManager();
       b=new BottomSheetDialog(MapsActivity.this);
storeName=findViewById(R.id.txt_store_title);
distance=findViewById(R.id.txt_distance);
       parentview=getLayoutInflater().inflate(R.layout.bottomsheet,null);
       b.setContentView(parentview);
      viewPager= (ViewPager) b.findViewById(R.id.home_viewpager1);
       circleIndicator= (CircleIndicator) b.findViewById(R.id.indicator);

        BottomSheetBehavior bt=BottomSheetBehavior.from((View)parentview.getParent());
        bt.setPeekHeight(600);
        b.show();
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        iconSelected =BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        navigate=findViewById(R.id.compass);   progressDialog = new ProgressDialog(this);
        progressDialog.show();
        mButton1 = (Button) findViewById(R.id.button2);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // BottomSheetDialog b=new BottomSheetDialog(MapsActivity.this);
                BottomSheetBehavior bt=BottomSheetBehavior.from((View)parentview.getParent());
                bt.setPeekHeight(600);
                b.show();
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dest = current.getPosition();

                try {
                    origin = latlong1;
                    //LatLng dest =new LatLng(10.027366, 76.314181);
                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);// "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin.latitude + "," + origin.longitude + "&destination=" + dest.latitude + "," + dest.longitude + "&key=AIzaSyCpJOTGURLKM7ZcFmfYlguVbdZjV4wNMHE";

                    FetchUrl fetchUrl = new FetchUrl();
                    // Start downloading json data from Google Directions API
                    fetchUrl.execute(url);
                } catch (NumberFormatException ne) {
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(dest, 12);
                    mMap.animateCamera(yourLocation);

                }

            }
        });

        // Persistent BottomSheet
 //       init_persistent_bottomsheet();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
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
          mMap.setOnMarkerClickListener(this);try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle_night));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if (markersArrayList.size() > 0) {
            for (Marker marker : markersArrayList) {
                builder.include(marker.getPosition());
            }
            bounds = builder.build();




    }}

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latitude = location.getLatitude();
        currentLat=String.valueOf(latitude);
        longitude = location.getLongitude();
        currentLon=String.valueOf(longitude);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        latlong1=latLng;
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.man4));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        Toast.makeText(MapsActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();
        String Hospital = "hospital";

        Log.d("onClick", "Button is Clicked");
        //mMap.clear();
        String url = getUrl(latitude, longitude,"bar");
        Object[] DataTransfer = new Object[3];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        DataTransfer[2] = this;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);

        Toast.makeText(MapsActivity.this,"Nearby driving school", Toast.LENGTH_LONG).show();








        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void read(ArrayList<Markerpoints> e) {
       for(Markerpoints m :e){
           LatLng latLng=new LatLng(m.getLat(),m.getLng());
           MarkerOptions markerOptions = new MarkerOptions();
           markerOptions.position(latLng);
           markerOptions.title(m.getPlace()+ " : " + m.getViccnity());
       //    ar.add(placeName + " : " + vicinity);    for (MarkerOptions markerOptions : markerOptionsArrayList) {
           mMap.addMarker(markerOptions);
           markerOps.add(markerOptions);
           markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
           //move map camera
           mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
       }
        for (MarkerOptions markerOptions : markerOps) {
            markersArrayList.add(mMap.addMarker(markerOptions));
        }
        for (Marker marker : markersArrayList) {
                onMarkerClick(marker);
        }      LatLngBounds.Builder builder = new LatLngBounds.Builder();       if (markersArrayList.size() > 0) {
            for (Marker marker : markersArrayList) {
                builder.include(marker.getPosition());
            }
            bounds = builder.build();
    }

        if (e.size() > 0) {
this.er=e;

another();
        }
    }



     public void intit_persistent_bottomsheet() {
        View persistentbottomSheet = coordinatorLayout.findViewById(R.id.bottomsheet);

     //  RelativeLayout iv_trigger =(RelativeLayout) persistentbottomSheet.findViewById(R.id.iv_fab);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(persistentbottomSheet);


        iv_trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        if (behavior != null)
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    //showing the different states
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    // React to dragging events

                }
            });

    }
  public void another() {   pagerAdapter = new PagerAdapter(fragmentManager,er);
      viewPager.setAdapter(pagerAdapter);
      viewPager.setClipToPadding(false);
      circleIndicator.setViewPager(viewPager);
      viewPager.setOffscreenPageLimit(4);}
    @Override
    public boolean onMarkerClick(Marker marker1) {
        storeName.setText(marker1.getTitle());

        progressDialog.dismiss();

        distance.setText(String.valueOf(getDistanceFromLatLonInKm(currentLat, currentLon, String.valueOf(marker1.getPosition().latitude), String.valueOf(marker1.getPosition().longitude))) + " Km");
        current = marker1;
     //   bottomtile.setVisibility(View.VISIBLE);
        if (count == 0) {
            marker1.setIcon(iconSelected);
            temp = marker1;
            count++;
        } else {
            if (temp == marker1) {
                temp.setIcon(iconSelected);
            } else {
                swap(marker1, temp);
                temp = marker1;
            }
        }
        return false;
    }
    public static float getDistanceFromLatLonInKm(String currentLat, String currentLon, String otherLat, String otherLon) {
        try {
            String distance = "0";
            if (!currentLat.isEmpty() && !currentLon.isEmpty() && !otherLat.isEmpty() && !otherLon.isEmpty()) {

                Location currentLocation = new Location("locA");
                currentLocation.setLatitude(Double.parseDouble(currentLat));
                currentLocation.setLongitude(Double.parseDouble(currentLon));

                Location otherLocation = new Location("locB");
                otherLocation.setLatitude(Double.parseDouble(otherLat));
                otherLocation.setLongitude(Double.parseDouble(otherLon));

                distance = String.format("%.2f", currentLocation.distanceTo(otherLocation) / 1000);

            }
            return Float.parseFloat(distance);

        } catch (NumberFormatException ne) {
            return 1;
        }
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
    }
    private void swap(Marker current, Marker temp) {
        temp.setIcon(icon);
        temp.hideInfoWindow();
        current.setIcon(iconSelected);
        current.showInfoWindow();
    }   private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser1 parser = new DataParser1();

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            String distanceString = "";
            String duration = "";

            if (result.size() < 1) {
                // Toast.makeText(getActivity(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distanceString = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }


                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.DKGRAY);
                distance.setText(duration + " " + distanceString);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                builder.include(origin);
                builder.include(dest);

                LatLngBounds bounds1;

                bounds1 = builder.build();

                width = getResources().getDisplayMetrics().widthPixels - 80;
                height = getResources().getDisplayMetrics().heightPixels - 50;
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds1, width, height, 0));
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
}

