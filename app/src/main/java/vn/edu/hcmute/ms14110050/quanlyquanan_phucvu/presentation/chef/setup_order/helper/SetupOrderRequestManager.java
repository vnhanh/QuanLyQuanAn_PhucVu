package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.helper;

import java.util.WeakHashMap;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.network.BaseRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.FullOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateDetailOrderStatusResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestApi;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.ChefSetupOrderViewModel;

public class SetupOrderRequestManager extends BaseRequestManager{
    private OrderRequestApi orderApi;

    public SetupOrderRequestManager() {
        orderApi = new OrderRequestApi();
    }

    public Flowable<FullOrderResponse> loadOrder(String orderID) {
        getToken();
        return orderApi.loadOrder(token, orderID);
    }

    public Flowable<OrderResponse> processCooking(WeakHashMap<String, Object> map) {
        getToken();
        return orderApi.updateStatusOrder(token, map);
    }

    public Flowable<UpdateDetailOrderStatusResponse> updateDetailOrderStatus(WeakHashMap<String,Object> map) {
        getToken();
        return orderApi.updateDetailOrder(token, map);
    }
}
