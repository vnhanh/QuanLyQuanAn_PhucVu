package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class DetailOrder {
    @SerializedName("food_id")
    @Expose
    String foodId;
    @SerializedName("food_name")
    @Expose
    String foodName;
    @SerializedName("price_unit")
    @Expose
    long unitPrice;
    @SerializedName("discount")
    @Expose
    long discount;
    @SerializedName("count")
    @Expose
    int count;

    public String getFoodId() {
        if (foodId == null) {
            foodId = "";
        }
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
