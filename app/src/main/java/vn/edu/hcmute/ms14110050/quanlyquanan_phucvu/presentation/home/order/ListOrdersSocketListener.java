package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;

import android.support.annotation.NonNull;
import android.util.Log;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.socket_listener.OrderSocketListener;

public class ListOrdersSocketListener {
    private OrderSocketService service;
    private OrdersConstributor constributor;

    public void setConstributor(@NonNull OrdersConstributor constributor) {
        this.constributor = constributor;
    }

    public ListOrdersSocketListener() {
        service = new OrderSocketService();
    }

    public void destroy() {
        constributor = null;
    }

    public void startListening() {
        service.onEventUpdateStatusOrder(new GetCallback<UpdateStatusOrderResponse>() {
            @Override
            public void onFinish(UpdateStatusOrderResponse data) {
//                Log.d("LOG", OrderSocketListener.class.getSimpleName() + ":onEventUpdateStatusOrder():data");
                if (data != null) {
                    if (constributor != null) {
                        constributor.onUpdateStatus(data.getOldStatus(), data.getOrder());
                    }
                }
            }
        });
        service.onEventRemoveOrder(new GetCallback<String>() {
            @Override
            public void onFinish(String orderID) {
                if (orderID != null) {
                    constributor.onRemoveItem(orderID);
                }
            }
        });
    }

    public void stopListening() {
        service.removeAllEvents();
    }
/*

    // Xóa order trong danh sách hiển thị
    private void onDeleteOrder(Order order) {
        if (order.getStatusFlag() == OrderFlag.PAYING) {
            ordersListener.onRemoveItem(order.getId());
        }
    }


    // Update order
    private void onUpdateOrder(Order order) {
        if (order.getStatusFlag() == OrderFlag.EATING) {
            ordersListener.onUpdateItem(order);
        }
    }

    // Add order vào recyclerview
    private void onAddOrder(Order order) {
        if (order.getStatusFlag() == OrderFlag.PENDING) {
            ordersListener.onAddItem(order, false);
        }
    }
*/

}
