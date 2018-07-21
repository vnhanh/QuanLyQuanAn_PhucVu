package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;

public class UpdateDetailOrderSocketData {
    @SerializedName("order_id")
    @Expose
    private String orderID;
    @SerializedName("final_cost")
    @Expose
    private long finalCost;
    @SerializedName("detail_order")
    @Expose
    private DetailOrder detailOrder;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public long getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(long finalCost) {
        this.finalCost = finalCost;
    }

    public DetailOrder getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(DetailOrder detailOrder) {
        this.detailOrder = detailOrder;
    }
}
