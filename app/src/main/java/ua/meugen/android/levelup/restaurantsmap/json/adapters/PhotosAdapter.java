package ua.meugen.android.levelup.restaurantsmap.json.adapters;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ua.meugen.android.levelup.restaurantsmap.data.model.Photo;

/**
 * Created by meugen on 27.01.17.
 */

public final class PhotosAdapter implements JsonDeserializer<List<Photo>> {

    public static void register(final GsonBuilder builder) {
        final TypeToken<List<Photo>> token = new TypeToken<List<Photo>>() {};
        builder.registerTypeAdapter(token.getType(), new PhotosAdapter());
    }

    @Override
    public List<Photo> deserialize(final JsonElement json, final Type typeOfT,
                              final JsonDeserializationContext context) throws JsonParseException {
        final List<Photo> result = new ArrayList<>();

        final JsonArray groups = json.getAsJsonObject().getAsJsonArray("groups");
        for (JsonElement group : groups) {
            final JsonArray items = group.getAsJsonObject().getAsJsonArray("items");
            for (JsonElement item : items) {
                final Photo photo = context.deserialize(item, Photo.class);
                result.add(photo);
            }
        }
        return result;
    }
}
