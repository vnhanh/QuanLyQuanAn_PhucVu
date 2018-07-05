package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.animator;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class AlphaAnimator implements ValueAnimator.AnimatorUpdateListener{
    private ValueAnimator animator;
    private ArrayList<View> views = new ArrayList<>();
    private HashMap<String, Integer> map = new HashMap<>();

    public AlphaAnimator() {
        animator = ValueAnimator.ofFloat(0.6f, 1f);
        animator.addUpdateListener(this);
        animator.setDuration(600);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
    }

    public void addView(View view) {
        views.add(view);
    }

    public void start() {
        animator.start();
    }

    public void stop() {
        animator.cancel();
    }

    public void reset() {
        map.clear();
        views.clear();
    }

    public void destroy() {
        views.clear();
        map.clear();
        map = null;
        views = null;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int size = views.size();

        for (int i = 0; i < size; i++) {
            View view = views.get(i);
            if (view != null) {
                view.setAlpha((Float) animation.getAnimatedValue());
            }
        }
    }
}
