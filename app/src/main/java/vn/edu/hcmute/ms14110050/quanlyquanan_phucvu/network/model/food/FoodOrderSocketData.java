package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vo Ngoc Hanh on 6/26/2018.
 */

public class FoodOrderSocketData {
    @SerializedName("order_id")
    @Expose
    private String orderID;

    @SerializedName("food")
    @Expose
    private Food food;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
