package ua.meugen.android.levelup.restaurantsmap.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.TimeUnit;

import ua.meugen.android.levelup.restaurantsmap.R;
import ua.meugen.android.levelup.restaurantsmap.fragments.ConnectionErrorFragment;
import ua.meugen.android.levelup.restaurantsmap.providers.FoursquareContent;
import ua.meugen.android.levelup.restaurantsmap.services.FetchContentService;

public final class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnCameraIdleListener, FoursquareContent {

    private static final String TAG = MainActivity.class.getName();

    private static final String MAP_FRAGMENT_TAG = "map";
    private static final int LOCATION_PERMISSIONS_REQUEST = 0;

    private final BroadcastReceiver venuesUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            loadVenues();
        }
    };

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
    protected void onStart() {
        super.onStart();

        registerReceiver(this.venuesUpdatedReceiver,
                new IntentFilter(FetchContentService.VENUES_UPDATED_ACTION));
    }

    @Override
    protected void onStop() {
        unregisterReceiver(this.venuesUpdatedReceiver);

        super.onStop();
    }

    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        final FragmentManager manager = getSupportFragmentManager();

        SupportMapFragment fragment = (SupportMapFragment) manager
                .findFragmentByTag(MAP_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance(new GoogleMapOptions()
                    .zoomControlsEnabled(true).zoomGesturesEnabled(true));
            manager.beginTransaction().replace(R.id.container, fragment,
                    MAP_FRAGMENT_TAG).commit();
        }
        fragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(final int cause) {}

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult result) {
        Log.e(TAG, "Connection failed with result: " + result);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                ConnectionErrorFragment.newInstance(result)).commit();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnCameraIdleListener(this);
        loadVenues();
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

    @Override
    public void onCameraIdle() {
        final LatLngBounds bounds = this.googleMap.getProjection().getVisibleRegion()
                .latLngBounds;
        startService(FetchContentService.createVenuesSearchByRegionIntent(bounds));
    }

    private void loadVenues() {
        getSupportLoaderManager().restartLoader(0, null, new VenuesLoaderCallbacks());
    }

    private void venuesLoaded(final Cursor cursor) {
        while (cursor.moveToNext()) {
            final LatLng position = new LatLng(cursor.getDouble(1),
                    cursor.getDouble(2));
            this.googleMap.addMarker(new MarkerOptions()
                    .title(cursor.getString(0))
                    .position(position));
        }
    }

    private class VenuesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
            final CursorLoader loader = new CursorLoader(MainActivity.this);
            loader.setUri(VENUES_URI);
            loader.setProjection(new String[] {
                    NAME_FIELD, LOCATION_LAT_FIELD, LOCATION_LNG_FIELD });
            return loader;
        }

        @Override
        public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
            venuesLoaded(cursor);
        }

        @Override
        public void onLoaderReset(final Loader<Cursor> loader) {}
    }
}
