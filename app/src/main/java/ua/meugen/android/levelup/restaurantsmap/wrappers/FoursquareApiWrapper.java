package ua.meugen.android.levelup.restaurantsmap.wrappers;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ua.meugen.android.levelup.restaurantsmap.api.FoursquareApi;
import ua.meugen.android.levelup.restaurantsmap.data.Content;
import ua.meugen.android.levelup.restaurantsmap.data.responses.VenueDetailsResponse;
import ua.meugen.android.levelup.restaurantsmap.data.responses.VenuesResponse;

public final class FoursquareApiWrapper {

    private static final String VERSION = "20170101";
    private static final String MODE = "foursquare";
    private static final String CLIENT_ID = "SCEQLTPFILFYYIT3TC015UIDTYJNWXWKFLBXLRGD0NSELLRJ";
    private static final String CLIENT_SECRET = "OPMYDTTPJTYZYJDWTQF5XFYZWW4445A245ZWQZQLYS4NRQZV";

    private static final String FOOD_CATEGORY_ID = "4d4b7105d754a06374d81259";

    private final FoursquareApi api;

    public FoursquareApiWrapper(final FoursquareApi api) {
        this.api = api;
    }

    private Map<String, String> buildBasicParams() {
        final Map<String, String> params = new HashMap<>();
        params.put("v", VERSION);
        params.put("m", MODE);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        return params;
    }

    private String latLngToValue(final LatLng latLng) {
        return "" + latLng.latitude + "," + latLng.longitude;
    }

    public Content<VenuesResponse> venuesSearchByRegion(final LatLngBounds bounds) throws IOException {
        final Map<String, String> params = buildBasicParams();
        params.put("intent", "browse");
        params.put("sw", latLngToValue(bounds.southwest));
        params.put("ne", latLngToValue(bounds.northeast));
        params.put("categoryId", FOOD_CATEGORY_ID);
        return this.api.venuesSearch(params).execute().body();
    }

    public Content<VenueDetailsResponse> venueDetails(final String venueId) throws IOException {
        return this.api.venueDetails(venueId, buildBasicParams()).execute().body();
    }
}
