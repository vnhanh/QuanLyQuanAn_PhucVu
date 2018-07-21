package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

/**
 * Created by Vo Ngoc Hanh on 6/30/2018.
 */

public class FullOrderResponse extends OrderResponse {
    @SerializedName("tables")
    @Expose
    protected ArrayList<Table> tables;
    @SerializedName("foods")
    @Expose
    protected ArrayList<Food> foods;

    public FullOrderResponse() {
    }

    public FullOrderResponse(boolean success, String message) {
        super(success, message);
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public void setTables(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public ArrayList<Food> getFoods() {
        if (foods == null) {
            foods = new ArrayList<>();
        }
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }
}
