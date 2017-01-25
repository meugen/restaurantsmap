package ua.meugen.android.levelup.restaurantsmap.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import ua.meugen.android.levelup.restaurantsmap.helpers.FoursquareDbHelper;

public class FoursquareProvider extends ContentProvider {

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
        return null;
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        return 0;
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        return null;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection,
                      final String[] selectionArgs) {
        return 0;
    }
}
