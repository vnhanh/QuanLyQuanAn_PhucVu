package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

/**
 * Created by Vo Ngoc Hanh on 6/25/2018.
 */

public class TableResponse extends ResponseValue {
    @SerializedName("table")
    @Expose
    Table table;

    public TableResponse(boolean success, String message) {
        super(success, message);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }
}
