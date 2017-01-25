package ua.meugen.android.levelup.restaurantsmap;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.meugen.android.levelup.restaurantsmap.api.FoursquareApi;
import ua.meugen.android.levelup.restaurantsmap.wrappers.FoursquareApiWrapper;


public final class RestaurantsMap extends ContextWrapper {

    public static final RestaurantsMap INSTANCE = new RestaurantsMap();

    private static final Object SYNC = new Object();

    private FoursquareApiWrapper wrapper;

    public RestaurantsMap() {
        super(null);
    }

    public FoursquareApiWrapper getFoursquareApi() {
        synchronized (SYNC) {
            if (this.wrapper == null) {
                final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                final OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                final Gson gson = new GsonBuilder()
                        .create();
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.foursquare.com/")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                final FoursquareApi api = retrofit.create(FoursquareApi.class);
                this.wrapper = new FoursquareApiWrapper(api);
            }
            return this.wrapper;
        }
    }

    public final static class App extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            INSTANCE.attachBaseContext(this);
        }
    }
}
