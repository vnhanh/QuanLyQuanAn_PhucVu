package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts;


import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;


/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public interface ISetupOrder {
    interface View extends LifeCycle.View {

        void onAnimationShowStatus();

        void onAnimationFinalCost();

        void onBackPressed();

        // hiển thị tên menu xác nhận tương ứng với trạng thái order
        boolean onUpdateMenu(int statusFlag);

        void onToast(@StringRes int messageIdRes);
    }
}
