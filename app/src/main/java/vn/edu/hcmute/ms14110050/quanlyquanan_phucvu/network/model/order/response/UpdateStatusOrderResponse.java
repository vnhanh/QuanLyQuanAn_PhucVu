package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;

public class UpdateStatusOrderResponse extends ResponseValue {
    @SerializedName("old_status")
    @Expose
    protected int oldStatus;
    @SerializedName("detail_order_id")
    @Expose
    protected String detailOrderID;
    @SerializedName("old_detail_order_status")
    @Expose
    protected int oldDetailOrderStatus;
    @SerializedName("new_detail_order_status")
    @Expose
    protected int newDetailOrderStatus;
    @SerializedName("order")
    @Expose
    protected Order order;

    public UpdateStatusOrderResponse(boolean success, String message) {
        super(success, message);
    }

    public int getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(int oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getDetailOrderID() {
        return detailOrderID;
    }

    public void setDetailOrderID(String detailOrderID) {
        this.detailOrderID = detailOrderID;
    }

    public int getOldDetailOrderStatus() {
        return oldDetailOrderStatus;
    }

    public void setOldDetailOrderStatus(int oldDetailOrderStatus) {
        this.oldDetailOrderStatus = oldDetailOrderStatus;
    }

    public int getNewDetailOrderStatus() {
        return newDetailOrderStatus;
    }

    public void setNewDetailOrderStatus(int newDetailOrderStatus) {
        this.newDetailOrderStatus = newDetailOrderStatus;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
