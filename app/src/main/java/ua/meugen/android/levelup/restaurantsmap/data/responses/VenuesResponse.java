package ua.meugen.android.levelup.restaurantsmap.data.responses;

import java.util.List;

import ua.meugen.android.levelup.restaurantsmap.data.model.Venue;

public final class VenuesResponse {

    private List<Venue> venues;

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(final List<Venue> venues) {
        this.venues = venues;
    }
}
