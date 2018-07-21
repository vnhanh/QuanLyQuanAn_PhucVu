package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.food;

import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestApi;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface IFoodVM {

    boolean isCreatedOrder();

    int getDetailOrderIndexByFood(String foodID);

    DetailOrder getDetailOrderByFood(String foodID);

    FoodRequestApi getFoodRequestManager();

    FoodSocketService getFoodSocketService();

    String getOrderID();

    String getToken();

    Context getContext();
}
