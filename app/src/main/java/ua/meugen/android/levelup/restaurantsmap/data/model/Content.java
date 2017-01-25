package ua.meugen.android.levelup.restaurantsmap.data.model;

public final class Content<T> {

    private Meta meta;
    private T response;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(final Meta meta) {
        this.meta = meta;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(final T response) {
        this.response = response;
    }
}
