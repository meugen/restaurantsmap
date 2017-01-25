package ua.meugen.android.levelup.restaurantsmap.model;

import com.google.gson.annotations.SerializedName;

public final class Location {

    private double lat;
    private double lng;
    @SerializedName("cc")
    private String countryCode;
    private String country;

    public double getLat() {
        return lat;
    }

    public void setLat(final double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(final double lng) {
        this.lng = lng;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
