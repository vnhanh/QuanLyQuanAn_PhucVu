package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;

public class OrderCheckable {
    private Order order;
    private boolean isCheck;

    public OrderCheckable(Order order, boolean isCheck) {
        this.order = order;
        this.isCheck = isCheck;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
