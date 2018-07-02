package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food;

import android.content.Context;

import java.util.Map;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestManger;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface IFoodVM {

    boolean isCreatedOrder();

    int getDetailOrderIndexByFood(String foodID);

    DetailOrder getDetailOrderByFood(String foodID);

    FoodRequestManger getFoodRequestManager();

    FoodSocketService getFoodSocketService();

    String getOrderID();

    String getToken();

    Context getContext();
}
