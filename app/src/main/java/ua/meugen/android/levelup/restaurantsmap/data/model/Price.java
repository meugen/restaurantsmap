package ua.meugen.android.levelup.restaurantsmap.data.model;

/**
 * Created by meugen on 27.01.17.
 */

public class Price {

    private int tier;
    private String message;
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(final int tier) {
        this.tier = tier;
    }
}
