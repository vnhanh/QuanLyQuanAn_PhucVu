package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view.MyProgressDialog;

public class ProgressRunnable implements Runnable{
    private WeakReference<Context> weakContext;
    private WeakReference<AlertDialog> weakDialog;
    private String message;

    public ProgressRunnable(Context context, String message) {
        weakContext = new WeakReference<>(context);
        this.message = message;
    }

    public void hide() {
        AlertDialog dialog = weakDialog.get();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void run() {
        Context context = weakContext.get();
        if (context != null) {
            weakDialog = new WeakReference<>(MyProgressDialog.create(context, message));

            weakDialog.get().show();
        }
    }
}