package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DeactivatedViewPager extends ViewPager {

    public DeactivatedViewPager (Context context) {
        super(context);
    }

    public DeactivatedViewPager (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (this.enabled) {
//            return super.onTouchEvent(event);
//        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        if (this.enabled) {
//            return super.onInterceptTouchEvent(event);
//        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
//        this.enabled = enabled;
    }

    @Override
    public void scrollTo(int x, int y) {
//        if(enabled) {
//            super.scrollTo(x, y);
//        }
    }
}