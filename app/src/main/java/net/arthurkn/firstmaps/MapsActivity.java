package net.arthurkn.firstmaps;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Location loc = getBestLocation();
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();
        String what = "lat: " + lat + " lng: " + lng;
        Toast.makeText( getApplicationContext(), what, Toast.LENGTH_SHORT ).show();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded( loc );
    }

    @Override
    protected void onResume() {
        Location loc = getBestLocation();
        super.onResume();
        setUpMapIfNeeded( loc );
    }

    /**
     * try to get the 'best' location selected from all providers
     */
    private Location getBestLocation() {
        Location gpslocation = getLocationByProvider(LocationManager.GPS_PROVIDER);
        Location networkLocation =
                getLocationByProvider(LocationManager.NETWORK_PROVIDER);
        // if we have only one location available, the choice is easy
        if (gpslocation == null) {
            Log.d("gpsLocation null", "No GPS Location available.");
            return networkLocation;
        }
        if (networkLocation == null) {
            Log.d("networkLocation null", "No Network Location available");
            return gpslocation;
        }
        // a locationupdate is considered 'old' if its older than the configured
        // update interval. this means, we didn't get a
        // update from this provider since the last check
//        long old = System.currentTimeMillis() - getGPSCheckMilliSecsFromPrefs();
//        boolean gpsIsOld = (gpslocation.getTime() < old);
//        boolean networkIsOld = (networkLocation.getTime() < old);
        // gps is current and available, gps is better than network
//        if (!gpsIsOld) {
//            Log.d("!gpsIsOld", "Returning current GPS Location");
//            return gpslocation;
//        }
//        // gps is old, we can't trust it. use network location
//        if (!networkIsOld) {
//            Log.d("!networkIsOld", "GPS is old, Network is current, returning network");
//            return networkLocation;
//        }
        // both are old return the newer of those two
        if (gpslocation.getTime() > networkLocation.getTime()) {
            Log.d("gpsGT > nlGT", "Both are old, returning gps(newer)");
            return gpslocation;
        } else {
            Log.d("!gpsGT > nlGT", "Both are old, returning network(newer)");
            return networkLocation;
        }
    }

    /**
     * get the last known location from a specific provider (network/gps)
     */
    private Location getLocationByProvider(String provider) {
        Location location = null;
//        if (!isProviderSupported(provider)) {
//            return null;
//        }
        LocationManager locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(provider)) {
                location = locationManager.getLastKnownLocation(provider);
            }
        } catch (IllegalArgumentException e) {
            Log.d("getlocationByProvider", "Cannot acces Provider " + provider);
        }
        return location;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap( Location loc )} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded( Location loc ) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap( loc );
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap( Location loc ) {
        LatLng latlng = new LatLng( loc.getLatitude(), loc.getLongitude() );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( latlng, 10));
        mMap.addMarker(new MarkerOptions().position(latlng).title("Marker"));
    }
}
