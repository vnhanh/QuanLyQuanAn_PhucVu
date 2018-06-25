package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestManger;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface IFoodVM {
    FoodRequestManger getFoodRequestManager();

    FoodSocketService getFoodSocketService();
}
