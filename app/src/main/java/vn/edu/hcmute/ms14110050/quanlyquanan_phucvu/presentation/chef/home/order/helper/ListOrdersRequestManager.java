package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.helper;

import java.util.WeakHashMap;

import io.reactivex.Flowable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.network.BaseRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrdersResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateDetailOrderStatusResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestApi;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestApi;

public class ListOrdersRequestManager extends BaseRequestManager {
    private OrderRequestApi orderApi;
    private FoodRequestApi foodApi;

    public ListOrdersRequestManager() {
        orderApi = new OrderRequestApi();
        foodApi = new FoodRequestApi();
    }

    public void loadOrders(GetCallback<OrdersResponse> callback) {
        getToken();
        orderApi.getOrdersForWaiter(token, callback);
    }

    public Flowable<UpdateDetailOrderStatusResponse> updateDetailOrderStatus(WeakHashMap<String,Object> map) {
        getToken();
        return orderApi.updateDetailOrder(token, map);
    }
}
