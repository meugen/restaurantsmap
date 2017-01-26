package ua.meugen.android.levelup.restaurantsmap.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;


public final class StartServiceOnConnected extends BroadcastReceiver {

    private Intent intent;

    @Override
    public synchronized void onReceive(final Context context, final Intent intent) {
        if (this.intent != null) {
            context.startService(this.intent);
        }
    }

    public void register(final Context context) {
        context.registerReceiver(this, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void unregister(final Context context) {
        context.unregisterReceiver(this);
    }

    public synchronized void attachNewIntent(final Intent intent) {
        this.intent = intent;
    }
}
