package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.helper;


import android.util.Log;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.RemoveDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.UpdateStatusDetailOrderSocketData;


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
        service.onEventUpdateStatusDetailOrder(new GetCallback<UpdateStatusDetailOrderSocketData>() {
            @Override
            public void onFinish(UpdateStatusDetailOrderSocketData data) {
                if (constributor != null) {
                    constributor.onStatusDetailOrderUpdated(data);
                }
            }
        });
        service.onEventUpdateStatusOrder(new GetCallback<UpdateStatusOrderResponse>() {
            @Override
            public void onFinish(UpdateStatusOrderResponse data) {
                if (data != null) {
                    Log.d("LOG", ListOrdersSocketListener.class.getSimpleName()
                            + ":onEventUpdateStatusOrder():old_status:" + data.getOldStatus()
                            + ":new status:" + data.getOrder().getStatusFlag());
                    if (constributor != null) {
                        constributor.onUpdateStatus(data);
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
        service.onEventUpdateDetailOrder(new GetCallback<UpdateDetailOrderSocketData>() {
            @Override
            public void onFinish(UpdateDetailOrderSocketData data) {
                if (constributor != null) {
                    constributor.onDetailOrderUpdated(data);
                }
            }
        });
        service.onEventRemoveDetailOrder(new GetCallback<RemoveDetailOrderSocketData>() {
            @Override
            public void onFinish(RemoveDetailOrderSocketData data) {
                if (constributor != null) {
                    constributor.onDetailOrderRemoved(data);
                }
            }
        });
    }

    public void stopListening() {
        service.stopAllEvents();
    }
}
