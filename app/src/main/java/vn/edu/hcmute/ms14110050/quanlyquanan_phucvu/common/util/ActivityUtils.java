package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class ActivityUtils {
    public static void replaceFragment(FragmentManager manager, Fragment fragment, @IdRes int layoutRes) {
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment _temp = manager.findFragmentByTag("fragment");
        if (_temp != null) {
            Log.d("LOG", ActivityUtils.class.getSimpleName() + ":replaceFragment():find prev fragment");
            transaction.remove(_temp);
        }
        transaction.replace(layoutRes, fragment, "fragment");
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

}
