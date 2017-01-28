package ua.meugen.android.levelup.restaurantsmap.data.model;

/**
 * Created by meugen on 27.01.17.
 */

public class Stats {

    private int checkinsCount;
    private int usersCount;
    private int tipCount;
    private int visitsCount;

    public int getCheckinsCount() {
        return checkinsCount;
    }

    public void setCheckinsCount(final int checkinsCount) {
        this.checkinsCount = checkinsCount;
    }

    public int getTipCount() {
        return tipCount;
    }

    public void setTipCount(final int tipCount) {
        this.tipCount = tipCount;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(final int usersCount) {
        this.usersCount = usersCount;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public void setVisitsCount(final int visitsCount) {
        this.visitsCount = visitsCount;
    }
}
