package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.view_food;

import android.support.annotation.StringRes;
import android.view.View;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;

/**
 * Created by Vo Ngoc Hanh on 6/27/2018.
 */

public interface IViewFood extends LifeCycle.View {
    void onBackPrevActivity();

    // Nhập số lượng món cần đặt
    void openInputOrderCountDialog(int oldCount, InputCallback callback);

    void openConfirmDialog(@StringRes int messageResId, final GetCallback<Void> callback);

    View getViewUI();
}
