package com.androidtutorialpoint.googlemapsnearbyplaces;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements botttomcallback,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener,
        LocationListener {   int count = 0;
String currentLat ,currentLon;
Marker current,temp;
    private GoogleMap mMap;
    double  latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 10000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private BottomSheetBehavior mBottomSheetBehavior1;
    BottomSheetDialog b;
    TextView btexview; View parentview;
    View bottomSheet;LatLngBounds bounds;
    RecyclerView r;    ArrayList<Marker> markersArrayList = new ArrayList<>();
    ArrayList<MarkerOptions> markerOps=new ArrayList<>();
    Button mButton1;
    TextView  iv_trigger,storeName, distance;    BitmapDescriptor icon = null, iconSelected = null;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      MapsInitializer.initialize(MapsActivity.this);
        setContentView(R.layout.activity_maps);
        View bottomSheet = findViewById(R.id.bottom);
       b=new BottomSheetDialog(MapsActivity.this);
storeName=findViewById(R.id.txt_store_title);
distance=findViewById(R.id.txt_distance);
       parentview=getLayoutInflater().inflate(R.layout.bottomsheet,null);
       b.setContentView(parentview);
        r=(RecyclerView) b.findViewById(R.id.recycle);
        BottomSheetBehavior bt=BottomSheetBehavior.from((View)parentview.getParent());
        bt.setPeekHeight(300);
        b.show();
        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_red);
        iconSelected = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_red_white);

        mButton1 = (Button) findViewById(R.id.button2);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  BottomSheetDialog b=new BottomSheetDialog(MapsActivity.this);

                BottomSheetBehavior bt=BottomSheetBehavior.from((View)parentview.getParent());
                bt.setPeekHeight(300);
                b.show();
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Toast.makeText(MapsActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();
        String Hospital = "hospital";

        Log.d("onClick", "Button is Clicked");
        //mMap.clear();
        String url = getUrl(latitude, longitude,"school");
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
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

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

    }}


  //    r.setAdapter(new RecyclerViewAdapter(e));

  //     r.setLayoutManager(new LinearLayoutManager(this));
     public void init_persistent_bottomsheet() {
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

    @Override
    public boolean onMarkerClick(Marker marker1) {
        storeName.setText(marker1.getTitle());


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
    }
}

