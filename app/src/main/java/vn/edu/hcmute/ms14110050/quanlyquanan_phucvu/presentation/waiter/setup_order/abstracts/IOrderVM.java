package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts;

import android.app.Activity;
import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.food.IFoodVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table.ITableVM;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */


public interface IOrderVM extends ITableVM, IFoodVM{
    Order getOrder();

    Context getContext();

    void onOrderUpdatedStatus(Order order);

    void onUpdateDetailOrder(UpdateDetailOrderSocketData data);

    void onDeleteDetailOrder(UpdateDetailOrderSocketData data);

    void onOrderRemoved();
}
