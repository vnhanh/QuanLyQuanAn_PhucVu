package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ToastRunnable implements Runnable {
    private WeakReference<Activity> weakActivity;
    private int resId;

    public ToastRunnable(Activity activity, @StringRes int messageResId) {
        weakActivity = new WeakReference<>(activity);
        this.resId = messageResId;
    }

    @Override
    public void run() {
        Activity activity = weakActivity.get();
        if (activity != null) {
            Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
        }
    }
}
