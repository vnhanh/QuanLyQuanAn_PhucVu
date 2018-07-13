package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order;


import android.util.Log;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateStatusOrderResponse;


public class ListOrdersSocketListener {
    private OrderSocketService service;
    private ChefOrdersConstributor constributor;

    public void setConstributor(ChefOrdersConstributor constributor) {
        this.constributor = constributor;
    }

    public ListOrdersSocketListener() {
        service = new OrderSocketService();
    }

    public void destroy() {
        constributor = null;
    }

    public void startListening() {
        service.onEventUpdateStatusOrder(new GetCallback<UpdateStatusOrderResponse>() {
            @Override
            public void onFinish(UpdateStatusOrderResponse data) {
                if (data != null) {
                    Log.d("LOG", ListOrdersSocketListener.class.getSimpleName()
                            + ":onEventUpdateStatusOrder():old_status:" + data.getOldStatus()
                            + ":new status:" + data.getOrder().getStatusFlag());
                    if (constributor != null) {
                        constributor.onUpdateStatus(data.getOldStatus(), data.getOrder());
                    }
                }
            }
        });
        service.onEventRemoveOrder(new GetCallback<String>() {
            @Override
            public void onFinish(String orderID) {
                if (orderID != null) {
                    constributor.onRemoveItem(orderID);
                }
            }
        });
    }

    public void stopListening() {
        service.stopAllEvents();
    }
}
