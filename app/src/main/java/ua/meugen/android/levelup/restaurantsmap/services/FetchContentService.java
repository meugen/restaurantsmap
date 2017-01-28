package ua.meugen.android.levelup.restaurantsmap.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ua.meugen.android.levelup.restaurantsmap.RestaurantsMap;
import ua.meugen.android.levelup.restaurantsmap.data.Content;
import ua.meugen.android.levelup.restaurantsmap.data.model.Venue;
import ua.meugen.android.levelup.restaurantsmap.data.model.VenueDetails;
import ua.meugen.android.levelup.restaurantsmap.data.responses.VenueDetailsResponse;
import ua.meugen.android.levelup.restaurantsmap.data.responses.VenuesResponse;
import ua.meugen.android.levelup.restaurantsmap.providers.FoursquareContent;
import ua.meugen.android.levelup.restaurantsmap.receivers.StartServiceOnConnected;

public class FetchContentService extends IntentService implements FoursquareContent {

    private static final String TAG = FetchContentService.class.getName();

    public static final String VENUES_UPDATED_ACTION
            = "ua.meugen.android.levelup.restaurantsmap.VENUES_UPDATED";
    public static final String VENUE_DETAILS_UPDATED_ACTION
            = "ua.meugen.android.levelup.restaurantsmap.VENUE_DETAILS_UPDATED";

    private static final String ACTION_KEY = "action";
    private static final String BOUNDS_KEY = "bounds";
    private static final String VENUE_ID_KEY = "venueId";

    private static final int VENUES_SEARCH_BY_REGION = 1;
    private static final int VENUE_DETAILS = 2;

    private static StartServiceOnConnected connectedReceiver;

    public static Intent createVenuesSearchByRegionIntent(final LatLngBounds bounds) {
        final Intent intent = new Intent(RestaurantsMap.INSTANCE, FetchContentService.class);
        intent.putExtra(ACTION_KEY, VENUES_SEARCH_BY_REGION);
        intent.putExtra(BOUNDS_KEY, bounds);
        return intent;
    }

    public static Intent createVenueDetails(final String venueId) {
        final Intent intent = new Intent(RestaurantsMap.INSTANCE, FetchContentService.class);
        intent.putExtra(ACTION_KEY, VENUE_DETAILS);
        intent.putExtra(VENUE_ID_KEY, venueId);
        return intent;
    }

    public FetchContentService() {
        super("restaurantsmap");
    }

    private static synchronized void handleWhenConnected(final Intent intent) {
        if (connectedReceiver == null) {
            connectedReceiver = new StartServiceOnConnected();
            connectedReceiver.attachNewIntent(intent);
            connectedReceiver.register(RestaurantsMap.INSTANCE);
        } else {
            connectedReceiver.attachNewIntent(intent);
        }
    }

    private static synchronized void clearConnectedReceiver() {
        if (connectedReceiver != null) {
            connectedReceiver.unregister(RestaurantsMap.INSTANCE);
            connectedReceiver = null;
        }
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final ConnectivityManager manager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            handleWhenConnected(intent);
            return;
        }
        clearConnectedReceiver();

        final int action = intent.getIntExtra(ACTION_KEY, 0);
        if (action == VENUES_SEARCH_BY_REGION) {
            venuesSearchByRegion(intent);
        } else if (action == VENUE_DETAILS) {
            venueDetails(intent);
        }
    }

    private void venuesSearchByRegion(final Intent intent) {
        try {
            final LatLngBounds bounds = intent.getParcelableExtra(BOUNDS_KEY);
            final Content<VenuesResponse> content = RestaurantsMap.INSTANCE
                    .getFoursquareApi().venuesSearchByRegion(bounds);
            storeVenues(content.getResponse().getVenues());
            sendBroadcast(new Intent(VENUES_UPDATED_ACTION));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void venueDetails(final Intent intent) {
        try {
            final String venueId = intent.getStringExtra(VENUE_ID_KEY);
            final Content<VenueDetailsResponse> content = RestaurantsMap.INSTANCE
                    .getFoursquareApi().venueDetails(venueId);
            storeVenueDetails(content.getResponse().getVenue());
            sendBroadcast(new Intent(VENUE_DETAILS_UPDATED_ACTION));
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

    private void storeVenueDetails(final VenueDetails details) {
        final ContentResolver resolver = this.getContentResolver();
        resolver.update(VENUES_URI,
                venueDetailsToContentValues(details),
                "id=?", new String[] { details.getId() });
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

    private void fillContentValues(final ContentValues values, final Venue venue) {
        values.put(ID_FIELD, venue.getId());
        values.put(NAME_FIELD, venue.getName());
        values.put(LOCATION_LAT_FIELD, venue.getLocation().getLat());
        values.put(LOCATION_LNG_FIELD, venue.getLocation().getLng());
        values.put(LOCATION_CC_FIELD, venue.getLocation().getCountryCode());
        values.put(LOCATION_COUNTRY_FIELD, venue.getLocation().getCountry());
    }

    private ContentValues venueToContentValues(final Venue venue) {
        final ContentValues values = new ContentValues();
        fillContentValues(values, venue);
        return values;
    }

    private ContentValues venueDetailsToContentValues(final VenueDetails details) {
        final ContentValues values = new ContentValues();
        fillContentValues(values, details);
        values.put(CANONICAL_URL_FIELD, details.getCanonicalUrl());
        values.put(VERIFIED_FIELD, details.isVerified());
        values.put(STATS_CHECKINS_COUNT_FIELD, details.getStats().getCheckinsCount());
        values.put(STATS_USERS_COUNT_FIELD, details.getStats().getUsersCount());
        values.put(STATS_TIP_COUNT_FIELD, details.getStats().getTipCount());
        values.put(STATS_VISITS_COUNT_FIELD, details.getStats().getVisitsCount());
        values.put(PRICE_TIER_FIELD, details.getPrice().getTier());
        values.put(PRICE_MESSAGE_FIELD, details.getPrice().getMessage());
        values.put(PRICE_CURRENCY_FIELD, details.getPrice().getCurrency());
        values.put(LIKES_COUNT_FIELD, details.getLikes().getCount());
        values.put(LIKES_SUMMARY_FIELD, details.getLikes().getSummary());
        values.put(DISLIKE_FIELD, details.isDislike());
        values.put(SHORT_URL_FIELD, details.getShortUrl());
        values.put(TIME_ZONE_FIELD, details.getTimeZone());
        values.put(BEST_PHOTO_ID_FIELD, details.getBestPhoto().getId());
        return values;
    }
}
