package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Vo Ngoc Hanh on 6/14/2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private ArrayList<OnChangeNetworkStateListener> listeners = new ArrayList<>();
    // cờ ghi nhớ trạng thái kết nối internet hiện tại
    private boolean networkState = false;

    public NetworkChangeReceiver() {

    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        networkState = isConnect(context);
        notifyListeners();
    }

    private void notifyListeners() {
        if (listeners == null) {
            return;
        }
        for (OnChangeNetworkStateListener listener : listeners) {
            notifyListener(listener);
        }
    }

    private void notifyListener(OnChangeNetworkStateListener listener) {
        if (networkState) {
            listener.onConnect();
        }else{
            listener.onDisconnect();
        }
    }

    private boolean isConnect(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public void register(@NonNull OnChangeNetworkStateListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }

        notifyListener(listener);
    }

    public void unregister(@NonNull OnChangeNetworkStateListener listener) {
        if (listeners == null) {
            return;
        }
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
}