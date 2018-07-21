package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class OrderResponse extends ResponseValue {
    @SerializedName("order")
    @Expose
    protected Order order;

    public OrderResponse() {
    }

    public OrderResponse(boolean success, String message) {
        super(success, message);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
