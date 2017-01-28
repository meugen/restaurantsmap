package ua.meugen.android.levelup.restaurantsmap.data.model;

import java.util.List;

/**
 * Created by meugen on 27.01.17.
 */

public class VenueDetails extends Venue {

    private String canonicalUrl;
    private boolean verified;
    private Stats stats;
    private Price price;
    private Likes likes;
    private boolean dislike;
    private List<Photo> photos;
    private String shortUrl;
    private String timeZone;
    private Photo bestPhoto;

    public Photo getBestPhoto() {
        return bestPhoto;
    }

    public void setBestPhoto(final Photo bestPhoto) {
        this.bestPhoto = bestPhoto;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(final String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public boolean isDislike() {
        return dislike;
    }

    public void setDislike(final boolean dislike) {
        this.dislike = dislike;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(final Likes likes) {
        this.likes = likes;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(final Price price) {
        this.price = price;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(final String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(final Stats stats) {
        this.stats = stats;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(final boolean verified) {
        this.verified = verified;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(final List<Photo> photos) {
        this.photos = photos;
    }
}
