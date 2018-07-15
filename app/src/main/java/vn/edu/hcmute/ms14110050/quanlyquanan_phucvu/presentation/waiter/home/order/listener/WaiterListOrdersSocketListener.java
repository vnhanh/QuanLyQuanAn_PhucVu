package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.listener;

import android.support.annotation.NonNull;
import android.util.Log;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.SuggestDelegacy;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.WaiterOrdersConstributor;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.WaiterListOrdersViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.socket_listener.OrderSocketListener;

public class WaiterListOrdersSocketListener {
    private OrderSocketService service;
    private WaiterOrdersConstributor constributor;
    private WaiterListOrdersViewModel viewModel;

    public void setViewModel(WaiterListOrdersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setConstributor(@NonNull WaiterOrdersConstributor constributor) {
        this.constributor = constributor;
    }

    public WaiterListOrdersSocketListener() {
        service = new OrderSocketService();
    }

    public void destroy() {
        viewModel = null;
        constributor = null;
    }

    public void startListening() {
        service.onEventUpdateOrder(new GetCallback<Order>() {
            @Override
            public void onFinish(Order order) {
                if (order != null) {
                    if (constributor != null) {
                        constributor.onUpdateOrder(order);
                    }
                }
            }
        });
        service.onEventUpdateStatusOrder(new GetCallback<UpdateStatusOrderResponse>() {
            @Override
            public void onFinish(UpdateStatusOrderResponse data) {
//                Log.d("LOG", OrderSocketListener.class.getSimpleName() + ":onEventUpdateStatusOrder():data");
                if (data != null) {
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

        service.onEventRequestDelegacyWaiter(new GetCallback<SuggestDelegacy>() {
            @Override
            public void onFinish(SuggestDelegacy suggestDelegacy) {
                Log.d("LOG", OrderSocketListener.class.getSimpleName()
                        + ":onEventRequestDelegacyWaiter():handover username:" + suggestDelegacy.getDelegacyUserName());

                if (viewModel != null) {
                    viewModel.onGetRequestDelegacySuggest(suggestDelegacy);
                }
            }
        });

        service.onEventResponseDelegacyWaiter(viewModel);
    }

    public void stopListening() {
        service.stopAllEvents();
    }

}
