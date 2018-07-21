package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

public class UpdateDetailOrderStatusResponse extends OrderResponse {
    @SerializedName("detail_order_id")
    @Expose
    private String detailOrderID;
    @SerializedName("new_detail_order_status")
    @Expose
    private int newDetailOrderStatus;
    @SerializedName("old_detail_order_status")
    @Expose
    private int oldDetailOrderStatus;

    public UpdateDetailOrderStatusResponse(boolean success, String message) {
        super(success, message);
    }

    public String getDetailOrderID() {
        return detailOrderID;
    }

    public void setDetailOrderID(String detailOrderID) {
        this.detailOrderID = detailOrderID;
    }

    public int getNewDetailOrderStatus() {
        return newDetailOrderStatus;
    }

    public void setNewDetailOrderStatus(int newDetailOrderStatus) {
        this.newDetailOrderStatus = newDetailOrderStatus;
    }

    public int getOldDetailOrderStatus() {
        return oldDetailOrderStatus;
    }

    public void setOldDetailOrderStatus(int oldDetailOrderStatus) {
        this.oldDetailOrderStatus = oldDetailOrderStatus;
    }
}
