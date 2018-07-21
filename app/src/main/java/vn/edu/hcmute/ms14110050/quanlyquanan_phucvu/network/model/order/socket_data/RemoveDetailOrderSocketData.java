package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

public class RemoveDetailOrderSocketData {
    @SerializedName("order_id")
    @Expose
    private String orderID;
    @SerializedName("detail_order_id")
    @Expose
    private String detailOrderID;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDetailOrderID() {
        return detailOrderID;
    }

    public void setDetailOrderID(String detailOrderID) {
        this.detailOrderID = detailOrderID;
    }
}
