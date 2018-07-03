package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.METHOD_REMOVE_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.METHOD_PAY_ORDER;

public class OrderGetCallBackImpl implements GetCallback<Void> {
    private WeakReference<SetupOrderViewModel> weakVM;
    private String tag;

    public OrderGetCallBackImpl(SetupOrderViewModel viewmodel) {
        this.weakVM = new WeakReference<>(viewmodel);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void onFinish(Void aVoid) {
        SetupOrderViewModel viewmodel = weakVM.get();
        if (viewmodel != null) {
            switch (tag) {
                case METHOD_REMOVE_ORDER:
                    viewmodel.onRemoveOrder();
                    break;

                case METHOD_PAY_ORDER:
                    viewmodel.onConfirmPayOrder();
            }
        }
    }
}
