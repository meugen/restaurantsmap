package ua.meugen.android.levelup.restaurantsmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = MainActivity.class.getName();

    private static final int LOCATION_PERMISSIONS_REQUEST = 0;

    private GoogleMap googleMap;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        final SupportMapFragment fragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(final int cause) {}

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult result) {
        Log.e(TAG, "Connection failed with result: " + result);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        final int check = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (check == PackageManager.PERMISSION_GRANTED) {
            enableLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, LOCATION_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode, @NonNull final String[] permissions,
            @NonNull final int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST) {
            final boolean granted = permissions.length == 1 &&
                    Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[0]) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (granted) {
                enableLocation();
            }
        }
    }

    private void enableLocation() {
        this.googleMap.setMyLocationEnabled(true);

        final Location location = LocationServices.FusedLocationApi
                .getLastLocation(this.client);
        if (location == null) {
            final LocationRequest request = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(TimeUnit.SECONDS.toMillis(2))
                    .setFastestInterval(TimeUnit.SECONDS.toMillis(2))
                    .setNumUpdates(1);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    this.client, request, this);
        } else {
            moveCameraToLocation(location);
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        moveCameraToLocation(location);
    }

    private void moveCameraToLocation(@NonNull final Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        this.googleMap.animateCamera(cameraUpdate);
    }
}
