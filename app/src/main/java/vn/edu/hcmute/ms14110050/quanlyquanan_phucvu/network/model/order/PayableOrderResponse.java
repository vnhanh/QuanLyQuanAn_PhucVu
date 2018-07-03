package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

public class PayableOrderResponse extends ResponseValue {
    @SerializedName("tables")
    @Expose
    private ArrayList<String> tables;
    @SerializedName("order")
    @Expose
    private Order order;

    public PayableOrderResponse(boolean success, String message) {
        super(success, message);
    }

    public ArrayList<String> getTables() {
        return tables;
    }

    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
