package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts;


import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public interface ISetupOrder {
    interface View extends LifeCycle.View {

        void onShowLoadTablesByOrderIDProgress();

        void onHideLoadTablesByOrderIDProgress();

        void onBackPrevActivity();

        void showProgress(@StringRes int messageIdRes);

        void hideProgress();

        void openInputNumberCustomerView(int customerNumber, InputCallback numberCustomerListener);

        void onAnimationShowStatus();

        void onAnimationNumberCustomer();
    }
}
