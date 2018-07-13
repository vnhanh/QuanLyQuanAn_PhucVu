package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;

import android.support.annotation.NonNull;

import java.util.WeakHashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.Callback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.IWebData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrdersResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.ResOrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.DelegacyResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.SuggestDelegacy;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderParams;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.abstracts.ListOrdersContract;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.listener.ListOrdersSocketListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.listener.OnSubmitDelegacyUserName;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IRecyclerAdapter;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class ListOrdersViewModel extends BaseNetworkViewModel<ListOrdersContract.View>
        implements GetCallback<User>, Callback<IWebData> {

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
        socketListener.setViewModel(this);
        createOrderRequestManager();
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
        callback = new OnSubmitDelegacyUserName(this);
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        socketListener.stopListening();
        callback.destroy();
        callback = null;
    }

    @Override
    public void onDestroy() {
        socketListener.destroy();
        orderRM = null;
        orderConstributor.destroy();
        super.onDestroy();
    }

    private void loadOrders() {
        showProgress(R.string.loading_orders);

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

    private OnSubmitDelegacyUserName callback;

    public OnSubmitDelegacyUserName getOnSubmitDelegacyUserNameCallback() {
        return callback;
    }

    public void onSubmitDelegacyUserName(String username) {
        showProgress(R.string.msg_giving_handover);
        createOrderRequestManager();
        getToken();

        WeakHashMap<String, Object> map = OrderParams.createSuggestDelegacy(user.getUsername(), username);

        orderRM.suggestDelegacy(token, map, new GetCallback<ResponseValue>() {
            @Override
            public void onFinish(ResponseValue response) {
                hideProgress();

                if (!response.isSuccess()) {
                    showMessage(response.getMessage(), Constant.COLOR_ERROR);
                }
                else{
                    showMessage(response.getMessage(), Constant.COLOR_SUCCESS);
                }
            }
        });
    }

    /*
    * Lắng nghe dữ liệu socket khi bàn giao hóa đơn
    * */

    private SuggestDelegacy suggest;

    // Khi nhận đề nghị bàn giao từ phục vụ khác
    public void onGetRequestDelegacySuggest(SuggestDelegacy suggest) {
        if (suggest != null) {
            if (user != null && user.getUsername() != null) {
                String username = user.getUsername();
                String delegacyUserName = suggest.getDelegacyUserName();

                if (username.equals(delegacyUserName)) {
                    this.suggest = suggest;

                    if (isViewAttached()) {
                        getView().onShowConfirmSuggestDelegacy(suggest);
                    }
                }
            }
        }
    }

    // Khi nhận phản hồi đề nghị bàn giao đã gửi lúc trước
    public void onGetResponseDelegacyWaiter(DelegacyResponse response) {
        if (response != null) {
            String handoverUserName = response.getHandoverUserName();
            if (handoverUserName != null && user != null) {
                String username = user.getUsername();

                if (handoverUserName.equals(username)) {
                    if (isViewAttached()) {
                        getView().onShowNotiResponseDelegacy(response);
                    }
                }
            }
        }
    }

    @Override
    public void onGet(IWebData data, int flag) {
        switch (flag) {
            case ResOrderFlag.RESPONSE_HANDOVER:
                if (data instanceof DelegacyResponse) {
                    onGetResponseDelegacyWaiter((DelegacyResponse) data);
                }
                break;

            case ResOrderFlag.RESQUEST_HANDOVER:
                if (data instanceof SuggestDelegacy) {
                    onGetRequestDelegacySuggest((SuggestDelegacy) data);
                }
                break;
        }
    }

    // Người dùng bấm OK đồng ý bàn giao
    public void onAcceptHandOver() {
        showProgress(R.string.msg_feedbacking_agree_handover);

        createOrderRequestManager();
        getToken();

        WeakHashMap<String, Object> map =
                OrderParams.createAgreeDelegacy(user.getUsername(), suggest.getHandoverUserName());

        orderRM.agreeBecomeDelegacy(token, map, new GetCallback<ResponseValue>() {
            @Override
            public void onFinish(ResponseValue response) {
                hideProgress();

                if (response.isSuccess()) {
                    showMessage(response.getMessage(), Constant.COLOR_SUCCESS);

                    // tải lại danh sách hóa đơn
                    loadOrders();
                }
                else{
                    String msg = getString(R.string.suggest_reaccept_handover, response.getMessage());
                    showMessage(msg, Constant.COLOR_ERROR);

                    if (isViewAttached()) {
                        getView().onShowConfirmSuggestDelegacy(suggest);
                    }
                }
            }
        });
    }

    // Người dùng bấm CANCEL không đồng ý bàn giao
    public void onDisagreeHandOver() {
        showProgress(R.string.msg_feedbacking_disagree_handover);

        createOrderRequestManager();
        getToken();

        WeakHashMap<String, Object> map =
                OrderParams.createDiagreeDelegacy(user.getUsername(), suggest.getHandoverUserName());

        orderRM.disagreeBecomeDelegacy(token, map, new GetCallback<ResponseValue>() {
            @Override
            public void onFinish(ResponseValue response) {
                hideProgress();

                if (response.isSuccess()) {
                    showMessage(response.getMessage(), Constant.COLOR_SUCCESS);
                }else{
                    String msg = getString(R.string.suggest_reaccept_handover, response.getMessage());
                    showMessage(msg, Constant.COLOR_ERROR);

                    if (isViewAttached()) {
                        getView().onShowConfirmSuggestDelegacy(suggest);
                    }
                }
            }
        });
    }

    /*
    * End
    * */
}
