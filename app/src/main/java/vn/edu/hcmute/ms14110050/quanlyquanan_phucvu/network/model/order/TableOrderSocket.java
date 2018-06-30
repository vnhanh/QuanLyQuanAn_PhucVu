package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

/**
 * Created by Vo Ngoc Hanh on 6/30/2018.
 */

public class TableOrderSocket {
    @SerializedName("order_id")
    @Expose
    private String orderID;
    @SerializedName("table")
    @Expose
    private Table table;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
