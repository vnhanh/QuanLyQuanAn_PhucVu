package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

public class UpdateStatusOrderResponse extends ResponseValue {
    @SerializedName("old_status")
    @Expose
    protected int oldStatus;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
