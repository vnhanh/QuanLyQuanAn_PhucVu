package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;


/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */


public interface IOrderVM extends ITableVM, IFoodVM {
    Order getOrder();

    void onOrderUpdatedStatus(Order order);

    void onOrderRemoved();
}
