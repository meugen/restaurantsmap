package ua.meugen.android.levelup.restaurantsmap.servers;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;

import ua.meugen.android.levelup.restaurantsmap.RestaurantsMap;
import ua.meugen.android.levelup.restaurantsmap.data.Content;
import ua.meugen.android.levelup.restaurantsmap.data.responses.Venues;

public class FetchContentService extends IntentService {

    private static final String TAG = FetchContentService.class.getName();

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
            Log.i(TAG, "Content: " + content);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
