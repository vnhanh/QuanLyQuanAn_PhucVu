package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class TablesResponse extends ResponseValue{
    @SerializedName("tables")
    @Expose
    private ArrayList<Table> tables;

    public ArrayList<Table> getTables() {
        return tables;
    }

    public void setTables(ArrayList<Table> tables) {
        this.tables = tables;
    }
}
