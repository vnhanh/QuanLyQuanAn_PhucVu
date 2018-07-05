package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;

import android.support.annotation.NonNull;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrdersResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IRecyclerAdapter;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class ListOrdersViewModel extends BaseNetworkViewModel<ListOrdersContract.View> implements GetCallback<User> {
    private OrderRequestManager orderRM;
    private String token;
    private User user;
    private ListOrdersSocketListener socketListener;
    private OrdersConstributor orderConstributor;


    /**
     * Constructor
     */
    public ListOrdersViewModel() {
        createOrderConstributor();
        socketListener = new ListOrdersSocketListener();
        socketListener.setConstributor(orderConstributor);
    }

    private void createOrderConstributor() {
        if (orderConstributor == null) {
            orderConstributor = new OrdersConstributor();
        }
    }


    /*
    * Property
    * */

    public User getUser() {
        return user;
    }

    public void setOrderAdapter(IRecyclerAdapter<OrderCheckable> orderAdapter) {
        orderConstributor.registerAdapter(orderAdapter);
    }

    public OrdersConstributor getOrderConstributor() {
        return orderConstributor;
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
        socketListener.stopListening();
    }

    @Override
    public void onDestroy() {
        socketListener.destroy();
        orderRM = null;
        orderConstributor.destroy();
        super.onDestroy();
    }

    private void loadOrders() {
        showProgress(R.string.loading_order);

        createOrderRequestManager();
        getToken();

        orderRM.getOrdersForWaiter(token, new GetCallback<OrdersResponse>() {
            @Override
            public void onFinish(OrdersResponse response) {
                if (response.getSuccess()) {
                    if (orderConstributor != null) {
                        orderConstributor.onGetList(response.getOrders());
                    }
                }
                hideProgress();
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
        createOrderConstributor();
        orderConstributor.setUsername(user != null ? user.getUsername() : "");
    }
}
