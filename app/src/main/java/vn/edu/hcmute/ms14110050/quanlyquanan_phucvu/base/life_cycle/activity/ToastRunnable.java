package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ToastRunnable implements Runnable {
    private WeakReference<Activity> weakActivity;
    private String message;

    public ToastRunnable(Activity activity, @StringRes int messageResId) {
        weakActivity = new WeakReference<>(activity);
        this.message = activity.getString(messageResId);
    }

    public ToastRunnable(Activity activity, String message) {
        weakActivity = new WeakReference<>(activity);
        this.message = message;
    }

    @Override
    public void run() {
        Activity activity = weakActivity.get();
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }
}
