package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs;

import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.Callback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.IWebData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.ResOrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.RemoveDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.UpdateStatusDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.DelegacyResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.SuggestDelegacy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_CREATE_DETAIL_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_REMOVE_DETAIL_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_REMOVE_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_REQUEST_DELEGACY_WAITER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_RESPONSE_DELEGACY_WAITER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_DETAIL_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_STATUS_DETAIL_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_STATUS_ORDER;

/**
 * Created by Vo Ngoc Hanh on 6/30/2018.
 */

public class OrderSocketService extends BaseSocketService {
    public void onEventUpdateStatusOrder(final GetCallback<UpdateStatusOrderResponse> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_STATUS_ORDER, callback, new TypeToken<UpdateStatusOrderResponse>(){}.getType());
    }

    public void onEventUpdateOrder(final GetCallback<Order> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_ORDER, callback, "order", new TypeToken<Order>(){}.getType());
    }

    public void onEventUpdateDetailOrder(final GetCallback<UpdateDetailOrderSocketData> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_DETAIL_ORDER, callback, new TypeToken<UpdateDetailOrderSocketData>(){}.getType());
    }

    public void onEventCreateDetailOrder(final GetCallback<UpdateDetailOrderSocketData> callback) {
        listenEvent(SOCKET_EVENT_CREATE_DETAIL_ORDER, callback, new TypeToken<UpdateDetailOrderSocketData>(){}.getType());
    }

    public void onEventRemoveDetailOrder(final GetCallback<UpdateDetailOrderSocketData> callback) {
        listenEvent(SOCKET_EVENT_REMOVE_DETAIL_ORDER, callback, new TypeToken<UpdateDetailOrderSocketData>(){}.getType());
    }

    public void onEventUpdateStatusDetailOrder(final GetCallback<UpdateStatusDetailOrderSocketData> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_STATUS_DETAIL_ORDER, callback, new TypeToken<UpdateStatusDetailOrderSocketData>(){}.getType());
    }

    public void onEventRemoveOrder(final GetCallback<String> callback) {
        listenEvent(SOCKET_EVENT_REMOVE_ORDER, callback, "order_id", null);
    }

    public void onEventRequestDelegacyWaiter(GetCallback<SuggestDelegacy> callback) {
        listenEvent(SOCKET_EVENT_REQUEST_DELEGACY_WAITER, callback, new TypeToken<SuggestDelegacy>(){}.getType());
    }

    public void onEventResponseDelegacyWaiter(Callback<IWebData> callback) {
        SocketListenerImpl<DelegacyResponse> listener =

                new SocketListenerImpl<DelegacyResponse>(
                        ResOrderFlag.RESPONSE_HANDOVER, callback,
                        new TypeToken<DelegacyResponse>(){}.getType());

        listenEvent(SOCKET_EVENT_RESPONSE_DELEGACY_WAITER, listener);
    }
}
