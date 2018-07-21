package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts;


import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public interface ISetupOrder {
    interface View extends LifeCycle.View {

        void onShowLoadTablesByOrderIDProgress();

        void onHideLoadTablesByOrderIDProgress();

        void onExit();

        boolean onUpdateMenu(boolean availableShow, boolean isCreateOrder, @OrderFlag int statusFlag);

        void onOpenSelectFoodsView();

        void onOpenInfoView();

        <DATA> void openConfirmDialog(@StringRes int titleResId, @StringRes int msgResId, GetCallback<DATA> callback);
    }
}
