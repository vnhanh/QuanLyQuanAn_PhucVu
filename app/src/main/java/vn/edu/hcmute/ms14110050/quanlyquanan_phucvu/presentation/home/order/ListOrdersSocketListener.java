package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IListAdapterListener;

public class ListOrdersSocketListener {
    private OrderSocketService service;
    private IListAdapterListener<Order> ordersListener;

    public void setOrdersListener(IListAdapterListener<Order> ordersListener) {
        this.ordersListener = ordersListener;
    }

    public ListOrdersSocketListener() {
        service = new OrderSocketService();
    }

    public void destroy() {
        service.destroy();
    }

    public void startListening() {
        service.onEventUpdateStatusOrder(new GetCallback<Order>() {
            @Override
            public void onFinish(Order order) {
                if (order != null) {
                    switch (order.getStatusFlag()) {
                        case OrderFlag.CREATING:
                            return;

                        case OrderFlag.PENDING:
                            onAddOrder(order);
                            return;

                        case OrderFlag.RUNNING:
                            onUpdateOrder(order);
                            return;

                        case OrderFlag.PAYING:
                            onDeleteOrder(order);
                            return;

                        case OrderFlag.COMPLETE:
                            return;
                    }
                }
            }
        });
        service.onEventRemoveOrder(new GetCallback<String>() {
            @Override
            public void onFinish(String orderID) {
                if (orderID != null) {
                    ordersListener.onRemoveItem(orderID);
                }
            }
        });
    }

    // Xóa order trong danh sách hiển thị
    private void onDeleteOrder(Order order) {
        if (order.getStatusFlag() == OrderFlag.PAYING) {
            ordersListener.onRemoveItem(order.getId());
        }
    }


    // Update order
    private void onUpdateOrder(Order order) {
        if (order.getStatusFlag() == OrderFlag.RUNNING) {
            ordersListener.onUpdateItem(order);
        }
    }

    // Add order vào recyclerview
    private void onAddOrder(Order order) {
        if (order.getStatusFlag() == OrderFlag.PENDING) {
            ordersListener.onAddItem(order);
        }
    }

}
