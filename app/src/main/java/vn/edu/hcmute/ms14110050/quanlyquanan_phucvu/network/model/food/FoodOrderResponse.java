package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public class FoodOrderResponse extends ResponseValue {
    @SerializedName("order_id")
    @Expose
    private String orderID;
    @SerializedName("new_count")
    @Expose
    private int newCount;
    @SerializedName("food")
    @Expose
    private Food food;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
