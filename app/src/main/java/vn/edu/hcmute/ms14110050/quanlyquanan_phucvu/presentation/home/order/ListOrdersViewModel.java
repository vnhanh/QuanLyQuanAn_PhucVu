package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;

import android.support.annotation.NonNull;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrdersResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IListAdapterListener;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class ListOrdersViewModel extends BaseNetworkViewModel<ListOrdersContract.View> implements GetCallback<User> {
    private OrderRequestManager orderRM;
    private String token;
    private User user;
    private IListAdapterListener<Order> orderAdapter;
    private ListOrdersSocketListener socketListener;

    /*
    * Property
    * */

    public User getUser() {
        return user;
    }

    public void setOrderAdapter(IListAdapterListener<Order> orderAdapter) {
        this.orderAdapter = orderAdapter;
        socketListener = new ListOrdersSocketListener();
        socketListener.setOrdersListener(orderAdapter);
    }

    /*
    * End Property
    * */

    @Override
    public void onViewAttach(@NonNull ListOrdersContract.View viewCallback) {
        super.onViewAttach(viewCallback);
        socketListener.startListening();
        loadOrders();
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        socketListener.destroy();
    }

    @Override
    public void onDestroy() {
        orderAdapter = null;
        orderRM = null;
        super.onDestroy();
    }

    private void loadOrders() {
        createOrderRequestManager();
        getToken();

        orderRM.getOrdersForWaiter(token, new GetCallback<OrdersResponse>() {
            @Override
            public void onFinish(OrdersResponse response) {
                if (response.getSuccess()) {
                    if (orderAdapter != null) {
                        orderAdapter.onGetList(response.getOrders());
                    }
                }
            }
        });
    }

    private void createOrderRequestManager() {
        if (orderRM == null) {
            orderRM = new OrderRequestManager();
        }
    }

    private void getToken() {
        if (token == null && isViewAttached()) {
            token = SSharedReference.getToken(getContext());
        }
    }

    /*
    * ONCLICK VIEW
    * */
    public void onClickAddOrderButton() {
        if (isViewAttached()) {
            getView().openAddOrderScreen();
        }
    }

    /*
    * IMPLEMENT GetCallback<User>
    * */
    @Override
    public void onFinish(User user) {
        this.user = user;
    }

}
