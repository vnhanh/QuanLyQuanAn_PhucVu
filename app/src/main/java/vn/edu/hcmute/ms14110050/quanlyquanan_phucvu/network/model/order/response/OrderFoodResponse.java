package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;

public class OrderFoodResponse extends ResponseValue {
    @SerializedName("food")
    @Expose
    protected Food food;
    @SerializedName("order")
    @Expose
    protected Order order;

    public OrderFoodResponse(boolean success, String message) {
        super(success, message);
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
