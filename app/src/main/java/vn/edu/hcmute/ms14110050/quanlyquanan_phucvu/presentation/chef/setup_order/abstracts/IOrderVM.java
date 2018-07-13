package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts;

import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.food.IFoodVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.table.ITableVM;


/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */


public interface IOrderVM extends ITableVM, IFoodVM {
    Order getOrder();

    Context getContext();

    void onOrderUpdatedStatus(Order order);

    void onDetailOrderUpdated(UpdateDetailOrderSocketData data);

    void onDetailOrderRemoved(UpdateDetailOrderSocketData data);

    void onOrderRemoved();
}
