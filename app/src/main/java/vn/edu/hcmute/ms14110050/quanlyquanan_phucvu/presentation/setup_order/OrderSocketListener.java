package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;

/**
 * Created by Vo Ngoc Hanh on 6/30/2018.
 */

public class OrderSocketListener {
    private OrderSocketService service;
    private IOrderVM centerVM;

    public OrderSocketListener() {
        service = new OrderSocketService();
    }

    public void setCenterViewModel(IOrderVM centerVM) {
        this.centerVM = centerVM;
    }

    public void startListening() {
        service.onEventUpdateStatusOrder(new GetCallback<Order>() {
            @Override
            public void onFinish(Order _order) {
                if (centerVM != null && centerVM.getOrderID() != null) {
                    String orderID = centerVM.getOrderID();
                    if (orderID.equals(_order.getId())) {
                        centerVM.onOrderUpdatedStatus(_order);
                    }
                }
            }
        });

        service.onEventRemoveOrder(new GetCallback<String>() {
            @Override
            public void onFinish(String orderID) {
                if (orderID != null && orderID.equals(centerVM.getOrderID())) {
                    centerVM.onOrderRemoved();
                }
            }
        });
    }

    public void destroy() {
        service.destroy();
        centerVM = null;
    }
}
