package ua.meugen.android.levelup.restaurantsmap.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import ua.meugen.android.levelup.restaurantsmap.data.Content;
import ua.meugen.android.levelup.restaurantsmap.data.responses.Venues;

public interface FoursquareApi {

    @GET("v2/venues/search")
    Call<Content<Venues>> venuesSearch(@QueryMap Map<String, String> params);
}
