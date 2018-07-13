package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.listener;

import android.support.annotation.NonNull;
import android.util.Log;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.Callback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.OrderSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.DelegacyResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.SuggestDelegacy;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.ListOrdersViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.OrdersConstributor;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.socket_listener.OrderSocketListener;

public class ListOrdersSocketListener {
    private OrderSocketService service;
    private OrdersConstributor constributor;
    private ListOrdersViewModel viewModel;

    public void setViewModel(ListOrdersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setConstributor(@NonNull OrdersConstributor constributor) {
        this.constributor = constributor;
    }

    public ListOrdersSocketListener() {
        service = new OrderSocketService();
    }

    public void destroy() {
        viewModel = null;
        constributor = null;
    }

    public void startListening() {
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
        service.removeAllEvents();
    }

}
