package ua.meugen.android.levelup.restaurantsmap.data.responses;

import ua.meugen.android.levelup.restaurantsmap.data.model.VenueDetails;

/**
 * Created by meugen on 27.01.17.
 */

public final class VenueDetailsResponse {

    private VenueDetails venue;

    public VenueDetails getVenue() {
        return venue;
    }

    public void setVenue(final VenueDetails venue) {
        this.venue = venue;
    }
}
