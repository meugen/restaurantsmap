package ua.meugen.android.levelup.restaurantsmap.providers;

import android.net.Uri;

public interface FoursquareContent {

    String SCHEME = "content";
    String AUTHORITY = "ua.meugen.android.levelup.restaurantsmap";

    String VENUES_TABLE = "venues";

    String ID_FIELD = "id";
    String NAME_FIELD = "name";
    String LOCATION_LAT_FIELD = "location_lat";
    String LOCATION_LNG_FIELD = "location_lng";
    String LOCATION_CC_FIELD = "location_cc";
    String LOCATION_COUNTRY_FIELD = "location_country";

    Uri IDS_URI = new Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("ids")
            .build();

    Uri VENUES_URI = new Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("venues")
            .build();

    Uri VENUE_ID_URI = new Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("venue")
            .build();
}
