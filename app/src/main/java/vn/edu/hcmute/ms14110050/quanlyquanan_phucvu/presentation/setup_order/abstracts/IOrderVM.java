package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.ITableVM;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

// TODO : còn phải extends IFoodVM
public interface IOrderVM extends ITableVM{

    void onRequestSelectFood(Food food, int newCount);

    FoodSocketService getFoodSocketService();

    Order getOrder();
}
