package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;

public class UpdateStatusDetailOrderSocketData {
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("detail_order")
    @Expose
    private DetailOrder detailOrder;
    @SerializedName("old_status_detail_order")
    @Expose
    private int oldDetailOrderStatus;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DetailOrder getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(DetailOrder detailOrder) {
        this.detailOrder = detailOrder;
    }

    public int getOldDetailOrderStatus() {
        return oldDetailOrderStatus;
    }

    public void setOldDetailOrderStatus(int oldDetailOrderStatus) {
        this.oldDetailOrderStatus = oldDetailOrderStatus;
    }
}
