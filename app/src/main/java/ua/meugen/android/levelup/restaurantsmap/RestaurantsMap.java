package ua.meugen.android.levelup.restaurantsmap;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.meugen.android.levelup.restaurantsmap.api.FoursquareApi;
import ua.meugen.android.levelup.restaurantsmap.json.adapters.PhotosAdapter;
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

                final GsonBuilder builder = new GsonBuilder();
                PhotosAdapter.register(builder);
                final Converter.Factory factory = GsonConverterFactory
                        .create(builder.create());
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.foursquare.com/")
                        .client(client)
                        .addConverterFactory(factory)
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
