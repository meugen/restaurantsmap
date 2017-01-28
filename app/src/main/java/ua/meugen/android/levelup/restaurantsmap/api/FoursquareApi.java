package ua.meugen.android.levelup.restaurantsmap.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import ua.meugen.android.levelup.restaurantsmap.data.Content;
import ua.meugen.android.levelup.restaurantsmap.data.responses.VenueDetailsResponse;
import ua.meugen.android.levelup.restaurantsmap.data.responses.VenuesResponse;

public interface FoursquareApi {

    @GET("v2/venues/search")
    Call<Content<VenuesResponse>> venuesSearch(
            @QueryMap Map<String, String> params);

    @GET("v2/venues/{venueId}")
    Call<Content<VenueDetailsResponse>> venueDetails(
            @Path("venueId") String venueId,
            @QueryMap Map<String, String> params);
}
