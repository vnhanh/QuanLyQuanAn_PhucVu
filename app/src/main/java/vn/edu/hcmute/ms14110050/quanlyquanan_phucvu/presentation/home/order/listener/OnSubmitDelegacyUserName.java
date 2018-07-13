package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.listener;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.ListOrdersViewModel;

public class OnSubmitDelegacyUserName implements GetCallback<String> {
    private WeakReference<ListOrdersViewModel> weakViewModel;

    public OnSubmitDelegacyUserName(ListOrdersViewModel viewModel) {
        weakViewModel = new WeakReference<>(viewModel);
    }

    @Override
    public void onFinish(String username) {
        ListOrdersViewModel viewModel = weakViewModel.get();
        if (viewModel != null) {
            viewModel.onSubmitDelegacyUserName(username);
        }
    }

    public void destroy() {
        weakViewModel = null;
    }
}
