package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Observable;
import java.util.WeakHashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable.RxDisposableSubscriber;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable.SendObject;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrdersResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateDetailOrderStatusResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderParams;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.helper.ChefOrdersConstributor;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.helper.ListOrdersRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.helper.ListOrdersSocketListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.IDetailOrderAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder.IDetailOrderProcessor;


/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class ChefListOrdersViewModel extends BaseNetworkViewModel<ListOrdersContract.View>
        implements GetCallback<User>, IDetailOrderProcessor {

    private ListOrdersRequestManager resManager;

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
        resManager = new ListOrdersRequestManager();
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

    public void setOrderAdapter(IDetailOrderAdapter orderAdapter) {
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
        resManager.onStart(getContext());
        loadOrders();
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        socketListener.stopListening();
        resManager.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socketListener.destroy();
        socketListener = null;
        orderConstributor.destroy();
        resManager = null;
    }

    private void loadOrders() {
        showProgress(R.string.loading_orders_waiting);
        resManager.loadOrders(new GetCallback<OrdersResponse>() {
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

    /*
    * IDetailOrderProcessor interface
    * */

    @Override
    public void requestUpdateStatusDetailOrder(DetailOrder detailOrder, int newStatus) {
        if (detailOrder == null) {
            Log.d("LOG", getClass().getSimpleName()
                    + ":requestUpdateStatusDetailOrder():detail order is null");
            return;
        }
        int oldStatus = detailOrder.getStatusFlag();
        if (oldStatus == newStatus) {
            Log.d("LOG", getClass().getSimpleName()
                    + ":requestUpdateStatusDetailOrder():not change status");
            showMessage(R.string.status_order_not_changed, Constant.COLOR_ERROR);
            return;
        }
        if (isViewAttached()) {
            int titleResId;
            if (oldStatus == OrderFlag.PENDING) {
                titleResId = R.string.title_confirm_start_cooking;
            }else if (oldStatus == OrderFlag.COOKING){
                titleResId = R.string.title_confirm_cook_finish;
            }else{
                return;
            }
            getView().openConfirmDialog(titleResId, R.string.ask_confirm_order, new GetCallback<Void>() {
                @Override
                public void onFinish(Void aVoid) {
                    updateStatusDetailOrder(detailOrder, newStatus);
                }
            });
        }
    }

    public void updateStatusDetailOrder(DetailOrder detailOrder, int newStatus) {
        if (resManager != null) {
            showProgress(R.string.confirming);
            WeakHashMap<String, Object> map =
                    OrderParams.updateDetailOrderStatus(detailOrder.getOrderID(), detailOrder.getId(), newStatus);
            setupForNewRequest();
            RxDisposableSubscriber subscriber = new RxDisposableSubscriber(this, Index.UPDATE_DETAIL_ORDER_STATUS);
            disposable = asyncProcessor.subscribeWith(subscriber);
            resManager.updateDetailOrderStatus(map).subscribe(asyncProcessor);
        }
    }

    private void onGetUpdateDetailOrderStatusResponse(UpdateDetailOrderStatusResponse response) {
        hideProgress();
        if (response.isSuccess()) {
            Order order = response.getOrder();
            String detailOrderID = response.getDetailOrderID();
            int oldDetailOrderStatus = response.getOldDetailOrderStatus();
            int newDetailOrderStatus = response.getNewDetailOrderStatus();
            orderConstributor.onUpdateDetailOrderStatus(order, detailOrderID, oldDetailOrderStatus, newDetailOrderStatus);
        }
        else{
            showMessage(response.getMessage(), Constant.COLOR_ERROR);
        }
    }

    /*
    * End
    * */

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);

        if (arg instanceof SendObject) {
            SendObject object = (SendObject) arg;
            int tag = object.getTag();

            switch (tag) {
                case Index.UPDATE_DETAIL_ORDER_STATUS:
                    UpdateDetailOrderStatusResponse orderResponse = (UpdateDetailOrderStatusResponse) object.getValue();
                    onGetUpdateDetailOrderStatusResponse(orderResponse);
                    break;
            }
        }
    }

    private @interface Index{
        int UPDATE_DETAIL_ORDER_STATUS = 0;
    }
}
