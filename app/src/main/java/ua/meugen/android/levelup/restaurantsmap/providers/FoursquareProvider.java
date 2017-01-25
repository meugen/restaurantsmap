package ua.meugen.android.levelup.restaurantsmap.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import ua.meugen.android.levelup.restaurantsmap.helpers.FoursquareDbHelper;

public final class FoursquareProvider extends ContentProvider implements FoursquareContent {

    private static final UriMatcher MATCHER;

    private static final int IDS = 1;
    private static final int VENUES = 2;
    private static final int VENUE_ID = 3;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(AUTHORITY, "ids", IDS);
        MATCHER.addURI(AUTHORITY, "venues", VENUES);
        MATCHER.addURI(AUTHORITY, "venue/#", VENUE_ID);
    }

    private FoursquareDbHelper helper;

    @Override
    public boolean onCreate() {
        this.helper = new FoursquareDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(final Uri uri) {
        return null;
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String selection,
                        final String[] selectionArgs, final String sortOrder) {
        final int which = MATCHER.match(uri);
        if (which == IDS) {
            return queryIds();
        } else if (which == VENUES) {
            return queryVenues(projection, selection, selectionArgs, sortOrder);
        }
        return null;
    }

    private Cursor queryIds() {
        final SQLiteDatabase database = this.helper.getReadableDatabase();
        return database.rawQuery("SELECT DISTINCT id FROM venues", new String[0]);
    }

    private Cursor queryVenues(final String[] projection, final String selection,
                               final String[] selectionArgs, final String sortOrder) {
        final SQLiteDatabase database = this.helper.getReadableDatabase();
        return database.query(VENUES_TABLE, projection, selection, selectionArgs, null,
                null, sortOrder);
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        final SQLiteDatabase database = this.helper
                .getWritableDatabase();
        database.insert(VENUES_TABLE, null, values);
        return VENUE_ID_URI.buildUpon().appendPath(values
                .getAsString(ID_FIELD)).build();
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        final SQLiteDatabase database = this.helper.getWritableDatabase();
        return database.update(VENUES_TABLE, values, selection, selectionArgs);
    }
}
