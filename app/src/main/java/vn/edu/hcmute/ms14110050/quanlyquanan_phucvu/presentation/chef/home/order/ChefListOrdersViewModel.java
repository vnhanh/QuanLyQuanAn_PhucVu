package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order;

import android.support.annotation.NonNull;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IRecyclerAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrdersResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestManager;


/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class ChefListOrdersViewModel extends BaseNetworkViewModel<ListOrdersContract.View> implements GetCallback<User> {
    private OrderRequestManager orderRM;
    private String token;
    private User user;
    private ListOrdersSocketListener socketListener;
    private ChefOrdersConstributor orderConstributor;

    /**
     * Constructor
     */
    public ChefListOrdersViewModel() {
        createOrderConstributor();
        socketListener = new ListOrdersSocketListener();
        socketListener.setConstributor(orderConstributor);
    }

    private void createOrderConstributor() {
        if (orderConstributor == null) {
            orderConstributor = new ChefOrdersConstributor();
        }
    }

    /*
    * Property
    * */

    public User getUser() {
        return user;
    }

    public void setOrderAdapter(IRecyclerAdapter<Order> orderAdapter) {
        orderConstributor.registerAdapter(orderAdapter);
    }

    public ChefOrdersConstributor getOrderConstributor() {
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
        super.onDestroy();
        socketListener.destroy();
        socketListener = null;
        orderRM = null;
        orderConstributor.destroy();
    }

    private void loadOrders() {
        showProgress(R.string.loading_orders_waiting);
        createOrderRequestManager();
        getToken();

        orderRM.getOrdersForWaiter(token, new GetCallback<OrdersResponse>() {
            @Override
            public void onFinish(OrdersResponse response) {
                if (response.isSuccess()) {
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

    /*
    * IMPLEMENT GetCallback<User>
    * */
    @Override
    public void onFinish(User user) {
        this.user = user;
    }
}
