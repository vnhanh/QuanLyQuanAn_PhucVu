package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class OrderResponse extends ResponseValue {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
