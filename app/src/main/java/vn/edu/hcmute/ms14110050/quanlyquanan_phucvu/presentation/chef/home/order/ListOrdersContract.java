package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order;


import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public interface ListOrdersContract {
    interface View extends LifeCycle.View{
        void openConfirmDialog(@StringRes int titleResId, @StringRes int messageResId, GetCallback<Void> callback);

    }
}
