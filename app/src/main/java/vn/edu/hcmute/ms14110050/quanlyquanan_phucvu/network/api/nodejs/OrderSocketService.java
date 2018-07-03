package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs;

import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateDetailOrderSocketData;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_DELETE_DETAIL_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_REMOVE_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_DETAIL_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_STATUS_ORDER;

/**
 * Created by Vo Ngoc Hanh on 6/30/2018.
 */

public class OrderSocketService extends BaseSocketService {
    public void onEventUpdateStatusOrder(final GetCallback<Order> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_STATUS_ORDER, callback, "order", new TypeToken<Order>(){}.getType());
    }

    public void onEventUpdateDetailOrder(final GetCallback<UpdateDetailOrderSocketData> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_DETAIL_ORDER, callback, null, new TypeToken<UpdateDetailOrderSocketData>(){}.getType());
    }

    public void onEventDeleteDetailOrder(final GetCallback<UpdateDetailOrderSocketData> callback) {
        listenEvent(SOCKET_EVENT_DELETE_DETAIL_ORDER, callback, null, new TypeToken<UpdateDetailOrderSocketData>(){}.getType());
    }

    public void onEventRemoveOrder(final GetCallback<String> callback) {
        listenEvent(SOCKET_EVENT_REMOVE_ORDER, callback, "order_id", null);
    }
}
