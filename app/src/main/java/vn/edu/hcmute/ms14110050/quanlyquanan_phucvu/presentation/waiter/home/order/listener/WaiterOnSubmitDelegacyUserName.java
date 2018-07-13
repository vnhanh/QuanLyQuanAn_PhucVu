package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.listener;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.WaiterListOrdersViewModel;

public class WaiterOnSubmitDelegacyUserName implements GetCallback<String> {
    private WeakReference<WaiterListOrdersViewModel> weakViewModel;

    public WaiterOnSubmitDelegacyUserName(WaiterListOrdersViewModel viewModel) {
        weakViewModel = new WeakReference<>(viewModel);
    }

    @Override
    public void onFinish(String username) {
        WaiterListOrdersViewModel viewModel = weakViewModel.get();
        if (viewModel != null) {
            viewModel.onSubmitDelegacyUserName(username);
        }
    }

    public void destroy() {
        weakViewModel = null;
    }
}
