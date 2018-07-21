package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts;

import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.food.IFoodVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table.ITableVM;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */


public interface IOrderVM extends ITableVM, IFoodVM{
    Order getOrder();

    Context getContext();

    void onStart();

    void onOrderUpdatedStatus(UpdateStatusOrderResponse data);

    void onOrderUpdated(Order order);

    void onUpdateDetailOrder(UpdateDetailOrderSocketData data);

    void onDeleteDetailOrder(UpdateDetailOrderSocketData data);

    void onOrderRemoved();

    void onRequestRemoveDetailOrder(String detailOrderID);

    void onRequestUpdateDetailOrderStatus(String detailOrderID, int status);
}
