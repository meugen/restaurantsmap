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
    String CANONICAL_URL_FIELD = "canonical_url";
    String VERIFIED_FIELD = "verified";
    String STATS_CHECKINS_COUNT_FIELD = "stats_checkins_count";
    String STATS_USERS_COUNT_FIELD = "stats_users_count";
    String STATS_TIP_COUNT_FIELD = "stats_tip_count";
    String STATS_VISITS_COUNT_FIELD = "stats_visits_count";
    String PRICE_TIER_FIELD = "price_tier";
    String PRICE_MESSAGE_FIELD = "price_message";
    String PRICE_CURRENCY_FIELD = "price_currency";
    String LIKES_COUNT_FIELD = "likes_count";
    String LIKES_SUMMARY_FIELD = "likes_summary";
    String DISLIKE_FIELD = "dislike";
    String SHORT_URL_FIELD = "short_url";
    String TIME_ZONE_FIELD = "time_zone";
    String BEST_PHOTO_ID_FIELD = "best_photo_id";

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
