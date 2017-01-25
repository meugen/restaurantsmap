package ua.meugen.android.levelup.restaurantsmap.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoursquareDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "foursquare";

    private static final Pattern PATTERN = Pattern.compile("\\s*([^;]);");

    private final Context context;

    public FoursquareDbHelper(final Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase database) {
        onUpgrade(database, 0, VERSION);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase database,
            final int oldVersion, final int newVersion) {
        final AssetManager assets = this.context.getAssets();
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            upgrade(database, version, assets);
        }
    }

    private void upgrade(final SQLiteDatabase database, final int version,
                         final AssetManager assets) {
        final StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(assets
                        .open("db/" + NAME + "/" + version + ".sql")));

                final char[] buf = new char[1024];
                while (true) {
                    final int count = reader.read(buf);
                    if (count < 0) {
                        break;
                    }
                    builder.append(buf, 0, count);
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Matcher matcher = PATTERN.matcher(builder);
        while (matcher.find()) {
            database.execSQL(matcher.group(1));
        }
    }
}
