package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.socket_listener;

import android.util.Log;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateStatusOrderResponse;
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
        service.onEventUpdateStatusOrder(new GetCallback<UpdateStatusOrderResponse>() {
            @Override
            public void onFinish(UpdateStatusOrderResponse data) {
                Log.d("LOG", OrderSocketListener.class.getSimpleName() + ":onEventUpdateStatusOrder():data");
                if (centerVM != null && centerVM.getOrderID() != null) {
                    String orderID = centerVM.getOrderID();
                    Order _order = data.getOrder();
                    if (_order == null) {
                        return;
                    }
                    String _orderID = _order.getId();
                    if (orderID.equals(_orderID)) {
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

    public void stopListening() {
        service.removeAllEvents();
    }

    public void destroy() {
        service = null;
        centerVM = null;
    }
}
