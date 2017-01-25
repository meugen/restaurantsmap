package ua.meugen.android.levelup.restaurantsmap.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ua.meugen.android.levelup.restaurantsmap.RestaurantsMap;
import ua.meugen.android.levelup.restaurantsmap.data.Content;
import ua.meugen.android.levelup.restaurantsmap.data.model.Venue;
import ua.meugen.android.levelup.restaurantsmap.data.responses.Venues;
import ua.meugen.android.levelup.restaurantsmap.providers.FoursquareContent;

public class FetchContentService extends IntentService implements FoursquareContent {

    private static final String TAG = FetchContentService.class.getName();

    public static final String VENUES_UPDATED_ACTION
            = "ua.meugen.android.levelup.restaurantsmap.VENUES_UPDATED";

    private static final String ACTION_KEY = "action";
    private static final String BOUNDS_KEY = "bounds";

    private static final int VENUES_SEARCH_BY_REGION = 1;

    public static Intent createVenuesSearchByRegionIntent(final LatLngBounds bounds) {
        final Intent intent = new Intent(RestaurantsMap.INSTANCE, FetchContentService.class);
        intent.putExtra(ACTION_KEY, VENUES_SEARCH_BY_REGION);
        intent.putExtra(BOUNDS_KEY, bounds);
        return intent;
    }

    public FetchContentService() {
        super("restaurantsmap");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final int action = intent.getIntExtra(ACTION_KEY, 0);
        if (action == VENUES_SEARCH_BY_REGION) {
            venuesSearchByRegion(intent);
        }
    }

    private void venuesSearchByRegion(final Intent intent) {
        try {
            final LatLngBounds bounds = intent.getParcelableExtra(BOUNDS_KEY);
            final Content<Venues> content = RestaurantsMap.INSTANCE
                    .getFoursquareApi().venuesSearchByRegion(bounds);
            storeVenues(content.getResponse().getVenues());
            sendBroadcast(new Intent(VENUES_UPDATED_ACTION));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void storeVenues(final List<Venue> venues) throws Exception {
        final ContentResolver resolver = this.getContentResolver();
        final Set<String> ids = fetchIds(resolver);

        final ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (Venue venue : venues) {
            ContentProviderOperation operation;
            if (ids.contains(venue.getId())) {
                operation = ContentProviderOperation.newUpdate(VENUES_URI)
                        .withSelection("id=?", new String[] { venue.getId() })
                        .withValues(venueToContentValues(venue))
                        .build();
            } else {
                operation = ContentProviderOperation.newInsert(VENUES_URI)
                        .withValues(venueToContentValues(venue))
                        .build();
            }
            operations.add(operation);
        }
        resolver.applyBatch(AUTHORITY, operations);
    }

    private Set<String> fetchIds(final ContentResolver resolver) {
        Cursor cursor = null;
        try {
            cursor = resolver.query(IDS_URI, null, null, null, null);

            final Set<String> result = new HashSet<>();
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0));
            }
            return result;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private ContentValues venueToContentValues(final Venue venue) {
        final ContentValues values = new ContentValues();
        values.put(ID_FIELD, venue.getId());
        values.put(NAME_FIELD, venue.getName());
        values.put(LOCATION_LAT_FIELD, venue.getLocation().getLat());
        values.put(LOCATION_LNG_FIELD, venue.getLocation().getLng());
        values.put(LOCATION_CC_FIELD, venue.getLocation().getCountryCode());
        values.put(LOCATION_COUNTRY_FIELD, venue.getLocation().getCountry());
        return values;
    }
}
