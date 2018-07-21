package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PayableOrderResponse extends UpdateStatusOrderResponse {

    @SerializedName("tables")
    @Expose
    protected ArrayList<String> tables;

    public PayableOrderResponse(boolean success, String message) {
        super(success, message);
    }

    public ArrayList<String> getTables() {
        return tables;
    }

    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }
}
