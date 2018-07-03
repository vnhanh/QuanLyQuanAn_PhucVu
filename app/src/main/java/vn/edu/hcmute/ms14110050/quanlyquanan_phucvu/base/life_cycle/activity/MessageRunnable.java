package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MessageRunnable implements Runnable{
    private WeakReference<Context> weakContext;
    private WeakReference<View> weakView;
    private String message;
    private int colorTextIsRes;

    public MessageRunnable(Context context, View view, String message, int colorTextIsRes) {
        weakContext = new WeakReference<>(context);
        weakView = new WeakReference<>(view);
        this.message = message;
        this.colorTextIsRes = colorTextIsRes;
    }

    @Override
    public void run() {
        Context context = weakContext.get();
        View view = weakView.get();
        if (context != null && view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Toast.LENGTH_SHORT);
            View _view = snackbar.getView();
            TextView textView = _view.findViewById(android.support.design.R.id.snackbar_text);
            int textColor = ContextCompat.getColor(context, colorTextIsRes);
            textView.setTextColor(textColor);
            snackbar.show();
        }
    }
}
