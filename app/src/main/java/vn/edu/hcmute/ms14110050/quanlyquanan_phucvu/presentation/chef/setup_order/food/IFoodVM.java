package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.food;

import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;


/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface IFoodVM {

    int getDetailOrderIndexByFood(String foodID);

    DetailOrder getDetailOrderByFood(String foodID);

    FoodSocketService getFoodSocketService();

    String getOrderID();

    String getToken();

    Context getContext();
}
