package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.order_info;

import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

public interface IOrderInfoView extends LifeCycle.View {

    void openInputNumberCustomerView(int customerNumber, InputCallback numberCustomerListener);

    void onAnimationShowStatus();

    void onAnimationNumberCustomer();

    void onAnimationFinalCost();

    void openDescriptionDialog(String description, InputCallback inputCallback);

    void onAnimationDescriptionOrder();

    void openConfirmDialog(@StringRes int messageResId, GetCallback<Void> callback);

    <DATA> void openConfirmDialog(@StringRes int titleResId, @StringRes int msgResId, GetCallback<DATA> callback);
}
