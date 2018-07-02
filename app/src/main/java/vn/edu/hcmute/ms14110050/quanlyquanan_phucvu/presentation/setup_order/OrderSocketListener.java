package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFood;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_ADD_CATEGORY_FOOD;

/**
 * Created by Vo Ngoc Hanh on 6/30/2018.
 */

public class OrderSocketListener {
    private OrderSocketService service;
    private IOrderVM centerVM;

    public void startListening() {
        service.onEventUpdateStatusOrder(new GetCallback<Order>() {
            @Override
            public void onFinish(Order _order) {
                if (centerVM != null && centerVM.getOrderID() != null) {
                    String orderID = centerVM.getOrderID();
                    if (orderID.equals(_order)) {
                        centerVM.onUpdateStatusOrder(_order);
                    }
                }
            }
        });
        service.onEventUpdateDetailOrder(new GetCallback<UpdateDetailOrderSocketData>() {
            @Override
            public void onFinish(UpdateDetailOrderSocketData data) {
                if (centerVM != null && centerVM.getOrderID() != null && centerVM.getOrderID().equals(data.getOrderID())) {
                    centerVM.onUpdateDetailOrder(data);
                }
            }
        });
        service.onEventDeleteDetailOrder(new GetCallback<UpdateDetailOrderSocketData>() {
            @Override
            public void onFinish(UpdateDetailOrderSocketData data) {
                if (centerVM != null && centerVM.getOrderID() != null && centerVM.getOrderID().equals(data.getOrderID())) {
                    centerVM.onDeleteDetailOrder(data);
                }
            }
        });
    }

    public void destroy() {
        centerVM = null;
    }
}
