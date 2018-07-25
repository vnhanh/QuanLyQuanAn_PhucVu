package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.WeakHashMap;

import io.reactivex.disposables.CompositeDisposable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputProcessorCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable.RxDisposableSubscriber;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable.SendObject;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.OnChangeSocketStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.SocketManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.RegionTableSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.FullOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.PayableOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateDetailOrderStatusResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderParams;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestApi;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestApi;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.table.TableRequestApi;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IRecyclerAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.contract.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.order_info.IOrderInfoView;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.socket_listener.FoodSocketListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.socket_listener.OrderSocketListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.socket_listener.TableSocketListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.view_food.InputCallbackImpl;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_ERROR;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_SUCCESS;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_WARNING;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.OrderConstant.METHOD_PAY_ORDER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.OrderConstant.METHOD_REMOVE_ORDER;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public class WaiterSetupOrderViewModel extends BaseNetworkViewModel<ISetupOrder.View>
        implements IOrderVM, OnChangeSocketStateListener, InputProcessorCallback {

    public final ObservableBoolean isCreateOrder = new ObservableBoolean(true);
    public final ObservableField<Integer> toolbarTitle = new ObservableField<Integer>();

    private OrderSocketListener orderListener;
    private TableSocketListener tableListener;
    private FoodSocketListener foodListener;

    private OrderRequestApi orderRM;
    private TableRequestApi tableRM;
    private FoodRequestApi foodRM;

    private int processMode;
    private Order order;

    private WaiterSetupOrderDisplay displayer;

    // listener lắng nghe thay đổi dữ liệu của bàn và món
    private IRecyclerAdapter<Table> tableDataListener;
    private IRecyclerAdapter<DetailOrder> detailOrderAdapter;

    private User waiter;

    private boolean FLAG_BACK_TO_PREV_ACTIVITY = false;
    private String error = "";
    private IOrderInfoView orderInfoView;

    // cờ đánh dấu đang ở trong màn hình thông tin hóa đơn
    private boolean isInfoView = true;

    public final ObservableBoolean isShowTables = new ObservableBoolean(true);

    /*
    * Property
    * */

    public void setOrderInfoView(IOrderInfoView orderInfoView) {
        this.orderInfoView = orderInfoView;
    }

    public void setDisplayer(WaiterSetupOrderDisplay displayer) {
        this.displayer = displayer;
        displayer.onStart(getView(), this, isCreateOrder);
    }

    public int getProcessMode() {
        return processMode;
    }

    public void setProcessMode(int processMode) {
        this.processMode = processMode;

        isCreateOrder.set(processMode == OrderMode.CREATE);
        FLAG_CREATED_ORDER_ON_SERVER = processMode == OrderMode.VIEW;

        toolbarTitle.set(isCreateOrder.get() ? R.string.action_create_order : R.string.action_serve_order);
    }

    public String getOrderID() {
        return order != null ? order.getId() : "";
    }

    public void setOrderID(String orderID) {
        if (!isCreateOrder.get()) {
            order = new Order();
            order.setId(orderID);
        }
    }

    public void setTableDataListener(IRecyclerAdapter<Table> tableDataListener) {
        this.tableDataListener = tableDataListener;
    }

    public void setDetailOrderAdapter(IRecyclerAdapter<DetailOrder> detailOrderAdapter) {
        this.detailOrderAdapter = detailOrderAdapter;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    public User getWaiter() {
        return waiter;
    }

    public void setWaiter(User waiter) {
        this.waiter = waiter;

        if (isCreateOrder.get()) {
            createOrder();
        }
    }

    private void createOrder() {
        if (waiter != null) {
            order = new Order();
            order.setWaiterUsername(waiter.getUsername());
            order.setWaiterFullname(waiter.getFullname());
            order.setStatusFlag(OrderFlag.CREATING);
            order.setCustomerNumber(1);
        }
    }

    /*
    * End Property
    * */

    public WaiterSetupOrderViewModel() {
        SocketManager.getInstance().addSocketStateListener(this);
    }

    private void createOrderSocketListener() {
        if (orderListener == null) {
            orderListener = new OrderSocketListener();
            orderListener.setCenterViewModel(this);
        }
    }

    private void createTableRequestManager() {
        if (tableRM == null) {
            tableRM = new TableRequestApi();
        }
    }

    private void createFoodRequestManager() {
        if (foodRM == null) {
            foodRM = new FoodRequestApi();
        }
    }

    private void createTableSocketListener() {
        if (tableListener == null) {
            tableListener = new TableSocketListener();
        }
    }

    private void createFoodListener() {
        if (foodListener == null) {
            foodListener = new FoodSocketListener();
        }
    }

    private void createOrderRequestManager() {
        if (orderRM == null) {
            orderRM = new OrderRequestApi();
        }
    }

    @Override
    public void onViewAttach(@NonNull ISetupOrder.View viewCallback) {
        super.onViewAttach(viewCallback);
        Log.d("LOG", getClass().getSimpleName() + ":onViewAttach()");
        if (FLAG_BACK_TO_PREV_ACTIVITY) {
//            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            showToast(error);

            getView().onExit();
        }

        createOrderSocketListener();
        orderListener.startListening();

        createTableSocketListener();
        createFoodListener();

        createOrderRequestManager();
        createTableRequestManager();
        createFoodRequestManager();

        if (isCreateOrder.get()) {
            // socket listen table và food
            tableListener.listenSockets(tableDataListener, this);
        }
        if (displayer != null) {
            displayer.onStart(getView(), this, isCreateOrder);
        }

        if (FLAG_ON_START_WAITING) {
            FLAG_ON_START_WAITING = false;
            onStart();
        }
    }

    private boolean FLAG_ON_START_WAITING = false;

    @Override
    public void onStart() {
        Log.d("LOG", getClass().getSimpleName()+":onStart()");
        if (isViewAttached()) {
            if (FLAG_CREATED_ORDER_ON_SERVER) {
                loadOrder();
            }else{
                createNewOrder();
            }
        }else{
            FLAG_ON_START_WAITING = true;
        }
    }

    // cờ đánh dấu đã thực hiện thao tác tạo order trên server
    private boolean FLAG_CREATED_ORDER_ON_SERVER = false;

    private void createNewOrder() {
        if (!FLAG_CREATED_ORDER_ON_SERVER && isCreateOrder.get() && order != null && orderRM != null) {

            showCreatingNewOrder();
            FLAG_CREATED_ORDER_ON_SERVER = true;

            getToken();
            order.setStatusFlag(OrderFlag.CREATING);
            WeakHashMap<String, Object> map = OrderParams.createRequestCreateOrder(order);

            orderRM.createOrder(token, map, new GetCallback<OrderResponse>() {
                @Override
                public void onFinish(OrderResponse response) {

                    hideProgress();

                    if (response.isSuccess()){
                        FLAG_CREATED_ORDER_ON_SERVER = true;
//                        Log.d("LOG", SetupOrderViewModel.class.getSimpleName() + ":createOrder():success");
                        order = response.getOrder();
                        if (displayer != null) {
                            displayer.showInfo();
                        }
                    }
                    else{
                        FLAG_CREATED_ORDER_ON_SERVER = false;
                        if (isViewAttached()) {
                            Toast.makeText(getContext(), getString(R.string.error_cant_create_new_order) + ". " + response.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            getView().onExit();
                        }else{
                            FLAG_BACK_TO_PREV_ACTIVITY = true;
                            error = getString(R.string.error_cant_create_new_order);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        Log.d("LOG", "viewmodel detach");
        orderInfoView = null;

        displayer.onStop();
        foodListener.stopListening();
        tableListener.stopListening();
        orderListener.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LOG", "viewmodel destroy");
        displayer = null;

        if (disposables != null) {
            disposables.clear();
            disposables = null;
        }

        if (foodListener != null) {
            foodListener.destroy();
            foodListener = null;
        }

        if (tableDataListener != null) {
            tableListener.destroy();
            tableListener = null;
        }

        if (orderListener != null) {
            orderListener.destroy();
            orderListener = null;
        }

        numberCustomerListener = null;

        tableDataListener = null;
        detailOrderAdapter = null;

        orderRM = null;
        tableRM = null;
        foodRM = null;

        SocketManager.getInstance().removeSocketStateListener(this);
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    // load thông tin order theo orderID
    // sau khi load xong thì listen socket table và food
    // load danh sách table và food theo orderID
    private void loadOrder() {
        Log.d("LOG", getClass().getSimpleName()+":loadOrder():" + order);
        if (order == null || order.getId() == null) {
            return;
        }

        showLoadingOrder();
        getToken();
        orderRM.getOrder(token, order.getId(), new GetCallback<FullOrderResponse>() {
            @Override
            public void onFinish(FullOrderResponse orderResponse) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (orderResponse.isSuccess()) {
                            order = orderResponse.getOrder();
                            int status = order.getStatusFlag();
                            isShowTables.set(status < OrderFlag.PAYING);

                            ArrayList<Table> tables = orderResponse.getTables();
                            tableDataListener.clearAll();
                            tableDataListener.onGetList(tables);

                            ArrayList<DetailOrder> details = order.getDetailOrders();
                            detailOrderAdapter.clearAll();
                            detailOrderAdapter.onGetList(details);

                            if (displayer != null) {
                                displayer.showInfo();
                            }
//                    showInformationOrder();
                        }else{
                            if (isViewAttached()) {
                                Toast.makeText(getContext(), R.string.get_order_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgress();
                    }
                });
            }
        });
    }

    /*
    * DATA BINDING
    * */

    // Bấm nút onBack toolbar
    public void onClickBackButton() {
        if (isCreateOrder.get()) {

            if (!isInfoView) {
                getView().onOpenInfoView();
            }
            else{
                // chưa chọn bất cứ bàn và món nào
                if(order == null){
                    onExit();
                }
                // chưa chọn bất cứ item nào
                else if ((order.getTables() == null || order.getTables().size() == 0)
                        && (order.getDetailOrders() == null || order.getDetailOrders().size() == 0)) {

                    if (isViewAttached()) {
                        orderInfoView.openConfirmDialog(R.string.ask_want_to_exit, new GetCallback<Void>() {
                            @Override
                            public void onFinish(Void aVoid) {
                                showProgress(R.string.removing_order);
                                removeOrderAndRestoreData();
                            }
                        });
                    }
                }else{
                    // hiển thị hộp thoại xác nhận thoát
                    // bấm OK ==> run onRemoveOrder()
                    if (isViewAttached()) {
                        OrderGetCallBackImpl removeCallback = new OrderGetCallBackImpl(this);
                        removeCallback.setTag(METHOD_REMOVE_ORDER);
                        orderInfoView.openConfirmDialog(R.string.message_confirm_remove_order_in_creating, removeCallback);
                    }
                }
            }
        }
    }

    private void onExit() {
        if(isViewAttached()){
            getView().onExit();
        }
        else{
            FLAG_BACK_TO_PREV_ACTIVITY = true;
            error = "";
        }
    }

    // Khi người dùng bấm nút OK trong hộp thoại confirm restore data trước khi thoát
    public void onRemoveOrder() {
        showProgress(R.string.restoring_tables_and_foods_in_order);
        // thực hiện request tới server để remove order và restore data
        removeOrderAndRestoreData();
    }

    // Bấm menu TẠO HÓA ĐƠN
    public void onClickCreateOrder() {
        if (isCreateOrder.get()) {
            if (order == null) {
                showMessage(R.string.order_null, Constant.COLOR_ERROR);
                return;
            }

            if (order.getDetailOrders() == null || order.getDetailOrders().size() == 0
                    || order.getTables() == null || order.getTables().size() == 0) {
                Toast.makeText(getContext(), R.string.must_select_tables_and_foods_for_order, Toast.LENGTH_SHORT).show();
                return;
            }

            if(StrUtil.isEmpty(order.getId())){
                showMessage(R.string.order_id_null, Constant.COLOR_ERROR);
                return;
            }

            createOrderRequestManager();
            getToken();

            disposables.add(orderRM.makeOrder(token, order.getId(), new GetCallback<UpdateStatusOrderResponse>() {
                @Override
                public void onFinish(UpdateStatusOrderResponse response) {
                    if (response.isSuccess()) {
                        showMessage(R.string.message_update_status_order_success, COLOR_SUCCESS);
                        onExit();
                    }else{
                        showMessage(R.string.message_update_status_order_failed, COLOR_ERROR);
                    }
                }
            }));
        }
    }

    // Thực hiện request update trạng thái order
    private void requestUpdateStatusOrder(WeakHashMap<String, Object> map, boolean isSetPayable) {
        showProgress(R.string.message_creating);

        createOrderRequestManager();
        getToken();

        if (isSetPayable) {
            orderRM.setPayableOrder(token, map, new GetCallback<PayableOrderResponse>() {
                @Override
                public void onFinish(PayableOrderResponse responseValue) {
                    hideProgress();
                    if (responseValue.isSuccess()) {
                        showMessage(R.string.message_update_status_order_success, COLOR_SUCCESS);
                    }else{
                        StringBuilder builder = new StringBuilder();

                        builder.append(getString(R.string.message_update_status_order_failed));
                        builder.append("\n");
                        builder.append(getString(R.string.list_failed_tables));

                        ArrayList<String> tables = responseValue.getTables();
                        if (tables != null && tables.size() > 0) {
                            int size = tables.size();
                            int i = 0;
                            for ( ; i < size; i++) {
                                if (i < size - 2) {
                                    builder.append(tables.get(i) + ", ");
                                }else{
                                    builder.append(tables.get(i));
                                }
                            }
                        }
                        showMessage(builder.toString(), COLOR_ERROR);
                    }
                }
            });
        }else{
            orderRM.updateStatusOrder(token, map, new GetCallback<OrderResponse>() {
                @Override
                public void onFinish(OrderResponse responseValue) {
                    hideProgress();
                    if (responseValue.isSuccess()) {
                        showMessage(R.string.message_update_status_order_success, COLOR_SUCCESS);
                        if (isCreateOrder.get()) {
                            if (isViewAttached()) {
                                getView().onExit();
                            }else{
                                FLAG_BACK_TO_PREV_ACTIVITY = true;
                            }
                        }
                    }else{
                        showMessage(R.string.message_update_status_order_failed, COLOR_ERROR);
                    }
                }
            });
        }
    }

    // Xóa order hiện thời và khôi phục lại các bàn, món đã chọn
    private void removeOrderAndRestoreData() {
        WeakHashMap<String, Object> _map = new WeakHashMap<>();
        _map.put("id", order.getId());

        disposables.add(orderRM.restoreOrder(token, _map, new GetCallback<ResponseValue>() {
            @Override
            public void onFinish(ResponseValue responseValue) {
                if (responseValue.isSuccess()) {
                    if (isViewAttached()) {
                        Toast.makeText(getContext(), R.string.remove_order_success, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (isViewAttached()) {
                        Toast.makeText(getContext(), R.string.remove_order_failed, Toast.LENGTH_SHORT).show();
                    }
                }
                hideProgress();
                if (isViewAttached()) {
                    getView().onExit();
                }
            }
        }));
    }

    private CompositeDisposable disposables = new CompositeDisposable();

    public void onClickSelectFoods() {
        isInfoView = false;
        getView().onOpenSelectFoodsView();
    }

    private InputCallback numberCustomerListener;

    // Khi người dùng bấm vào nút EDIT nằm bên phải dòng số lượng khách hàng
    public void onClickNumberCustomerView() {
        if (isViewAttached()) {
            if (numberCustomerListener == null) {
                numberCustomerListener = new InputCallbackImpl(this);
            }
            currentNumberCustomerInput = order.getCustomerNumber();
            orderInfoView.openInputNumberCustomerView(currentNumberCustomerInput, numberCustomerListener);
        }
    }

    private int currentNumberCustomerInput = 0;

    // Khi người EditText input số lượng khách thay đổi nội dung
    @Override
    public void onChangeInputValue(TextInputLayout til, EditText edt, String input) {
        String error = "";
        String newText = "";
        int count = 0;
        boolean isChanged = false;

        try {
            count = input.equals("") ? 0 : Integer.parseInt(input);

            if (count < 0) {
                error = getString(R.string.number_customer_must_not_negative);
                newText = "0";
                isChanged = true;
            }else if(count > 0 && input.charAt(0) == '0'){
                newText = String.valueOf(count);
                isChanged = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = getString(R.string.error_wrong_format_input);
            newText = "0";
            isChanged = true;
        }

        if (error.equals("")) {
            currentNumberCustomerInput = count;
        }else{
            if (til != null) {
                til.setError(error);
            }else{
                edt.setError(error);
            }
        }
        if (isChanged) {
            edt.setText(newText);
            edt.setSelection(edt.getText().toString().length());
        }
    }

    // Khi người dùng bấm vào nút OK trong hộp thoại nhập số lượng khách
    @Override
    public void onSubmitInputProcessor() {
        if (currentNumberCustomerInput == order.getCustomerNumber()) {
            return;
        }

        showProgress(R.string.message_processing);
        createOrderRequestManager();
        getToken();

        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("order_id", order.getId());
        map.put("number_customer", currentNumberCustomerInput);

        disposables.add(orderRM.updateNumberCustomer(token, map, new GetCallback<OrderResponse>() {
            @Override
            public void onFinish(OrderResponse responseValue) {
                if (responseValue.isSuccess()) {
                    onUpdateNumberCustomer(responseValue.getOrder());
                }else{
                    if (isViewAttached()) {
                        Toast.makeText(getContext(), getString(R.string.update_number_customer_failed), Toast.LENGTH_SHORT).show();
                    }
                }
                hideProgress();
            }
        }));

        order.setCustomerNumber(currentNumberCustomerInput);
        if (displayer != null) {
            displayer.showNumberCustomer(order);
        }
    }

    // Nhận dữ liệu trả về từ server khi update số lượng khách trong hóa đơn
    private void onUpdateNumberCustomer(Order _order) {
        this.order = _order;
        if (displayer != null) {
            displayer.showNumberCustomer(order);
        }

        if (isViewAttached()) {
            orderInfoView.onAnimationNumberCustomer();
        }
    }

    private InputCallback descriptionInputListener;
    private String currentDescription = "";

    // Khi người dùng bấm vào nút EDIT nằm bên phải dòng chú thích
    public void onClickEditDescription() {
        if (isViewAttached()) {
            if (descriptionInputListener == null) {
                descriptionInputListener = new InputCallbackImpl(new InputProcessorCallback() {
                    @Override
                    public void onChangeInputValue(TextInputLayout til, EditText edt, String input) {
                        currentDescription = input;
                    }

                    @Override
                    public void onSubmitInputProcessor() {
                        if (currentDescription == null) {
                            Toast.makeText(getContext(), getString(R.string.description_content_is_null), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (currentDescription.equals(order.getDescription())) {
                            Toast.makeText(getContext(), getString(R.string.description_content_not_change), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (order == null) {
                            Toast.makeText(getContext(), getString(R.string.not_verify_order), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        order.setDescription(currentDescription);
                        if (displayer != null) {
                            displayer.showDescriptionOrder(order);
                        }

                        createOrderRequestManager();
                        getToken();

                        WeakHashMap<String, Object> map = new WeakHashMap<>();
                        map.put("order_id", order.getId());
                        map.put("description", currentDescription);

                        disposables.add(orderRM.updateDescription(token, map, new GetCallback<ResponseValue>() {
                            @Override
                            public void onFinish(ResponseValue response) {
                                if (response.isSuccess()) {
                                    order.setDescription(currentDescription);
                                    onUpdateDescriptionOrder();
                                }else{
                                    if (isViewAttached()) {
                                        Toast.makeText(getContext(),
                                                getString(R.string.update_description_order_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }));
                    }
                });
            }
            currentDescription = order.getDescription();
            orderInfoView.openDescriptionDialog(order.getDescription(), descriptionInputListener);
        }
    }

    private void onUpdateDescriptionOrder() {
        if (displayer != null) {
            displayer.showDescriptionOrder(order);
        }
        if (isViewAttached()) {
            orderInfoView.onAnimationDescriptionOrder();
        }
    }

    /*
    * End DATA BINDING
    * */

    /*
    * IOrderVM
    * */

    @Override
    public boolean isCreatedOrder() {
        return isCreateOrder.get();
    }

    private String token;

    @Override
    public void onRequestAddTableToOrder(final Table table, final GetCallback<TableResponse> callback) {
        createTableRequestManager();
        getToken();

        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("orderID", getOrderID());
        map.put("tableID", table.getId());

        createOrderRequestManager();

        disposables.add(orderRM.orderTable(token, getOrderID(), table.getId(), new GetCallback<TableResponse>() {
            @Override
            public void onFinish(TableResponse tableResponse) {
                if (tableResponse.isSuccess()) {
//                    Log.d("LOG", SetupOrderViewModel.class.getSimpleName()
//                            + ":onRequestAddTableToOrder():tableResponse:success");

                    // nếu thành công thì thêm bàn vào recyclerview
                    order.getTables().add(table.getId());
                    tableDataListener.onAddItem(tableResponse.getTable());
                }
                else{
                    Log.d("LOG", WaiterSetupOrderViewModel.class.getSimpleName()
                            + ":onRequestAddTableToOrder():tableResponse:failed:"+tableResponse.getMessage());
                }
                callback.onFinish(tableResponse);
            }
        }));
    }

    @Override
    public void onRequestRemoveTableFromOrder(final String tableID, final GetCallback<TableResponse> callback) {
        createOrderRequestManager();
        getToken();

        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("tableID", tableID);
        map.put("orderID", getOrderID());

        disposables.add(orderRM.removeTableFromOrder(token, map, new GetCallback<TableResponse>() {
            @Override
            public void onFinish(TableResponse response) {
                if (response.isSuccess()) {
                    String tableID = response.getTable().getId();
                    order.getTables().remove(tableID);
                    tableDataListener.onRemoveItem(tableID);
                }
                else{
                    Log.d("LOG", WaiterSetupOrderViewModel.class.getSimpleName()
                            + ":onRequestRemoveTableFromOrder():failed:" + response.getMessage());
                }
                callback.onFinish(response);
            }
        }));
    }

    @Override
    public TableRequestApi getRegionTableRequestManager() {
        createTableRequestManager();
        return tableRM;
    }

    @Override
    public FoodRequestApi getFoodRequestManager() {
        createFoodRequestManager();
        return foodRM;
    }

    @Override
    public DetailOrder getDetailOrderByFood(String foodID) {
//        Log.d("LOG", getClass().getSimpleName() + ":getDetailOrderByFood():foodID:" + foodID);
        if(order == null)
            return null;
        ArrayList<DetailOrder> details = order.getDetailOrders();
        for (DetailOrder detail : details) {
            if (detail.getFoodId() != null && detail.getFoodId().equals(foodID)) {
//                Log.d("LOG", getClass().getSimpleName() + ":getDetailOrderByFood():find detail");
                return detail;
            }
        }
//        Log.d("LOG", getClass().getSimpleName() + ":getDetailOrderByFood():find no detail");
        return null;
    }

    @Override
    public int getDetailOrderIndexByFood(String foodID) {
        if(order == null)
            return -1;
        ArrayList<DetailOrder> details = order.getDetailOrders();
        int size = details.size();
        for (int i = 0; i < size; i++) {
            DetailOrder detail = details.get(i);
            if (detail.getFoodId() != null && detail.getFoodId().equals(foodID)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public RegionTableSocketService getRegionTableSocketService() {
        createTableSocketListener();
        return tableListener.getSocketService();
    }

    @Override
    public FoodSocketService getFoodSocketService() {
        createFoodListener();
        return foodListener.getSocketService();
    }

    @Override
    public String getToken() {
        if (token == null) {
            token = SSharedReference.getToken(getContext());
        }
        return token;
    }

    @Override
    public void onOrderUpdated(Order order) {

    }

    @Override
    public void onOrderUpdatedStatus(UpdateStatusOrderResponse data) {
        this.order = data.getOrder();
        if (displayer != null) {
            displayer.showInfo();
        }
        ArrayList<DetailOrder> details = order.getDetailOrders();
        detailOrderAdapter.clearAll();
        detailOrderAdapter.onGetList(details);

        if (isViewAttached()) {
            orderInfoView.onAnimationShowStatus();
        }

        int flag = order.getStatusFlag();

        switch (flag) {
            case OrderFlag.PAYING:
                showMessage(R.string.order_updated_paying, COLOR_WARNING);
                return;

            case OrderFlag.COMPLETE:

                showMessage(R.string.order_updated_completed, COLOR_WARNING);

                if (isViewAttached()) {
                    getView().onToast(R.string.msg_exit_setup_order);
                    getView().onExit();
                }
                else{
                    FLAG_BACK_TO_PREV_ACTIVITY = true;
                    error = getString(R.string.msg_exit_setup_order);
                }
                return;
        }
    }

    @Override
    public void onUpdateDetailOrder(UpdateDetailOrderSocketData data) {
        if (data != null) {
            order.setFinalCost(data.getFinalCost());
            onUpdateFinalCost();

            if (order.getDetailOrders() == null) {
                return;
            }

            DetailOrder _detail = data.getDetailOrder();
            if (_detail == null || _detail.getFoodId() == null) {
                return;
            }
            ArrayList<DetailOrder> details = order.getDetailOrders();
            int size = details.size();

            for (int i = 0; i < size; i++) {
                if (_detail.getFoodId().equals(details.get(i).getFoodId())) {
                    details.set(i, _detail);
                    if (detailOrderAdapter != null) {
                        RecyclerView.Adapter adapter = detailOrderAdapter.getAdapter();
                        adapter.notifyItemChanged(i);
                    }
                    break;
                }
            }
        }
    }

    private void onUpdateFinalCost() {
        if (displayer != null) {
            displayer.showFinalCost(order);
        }
        if (isViewAttached()) {
            orderInfoView.onAnimationFinalCost();
        }
    }

    @Override
    public void onDeleteDetailOrder(UpdateDetailOrderSocketData data) {
        if (data == null) {
            return;
        }

        order.setFinalCost(data.getFinalCost());
        onUpdateFinalCost();

        if (order.getDetailOrders() == null) {
            return;
        }

        DetailOrder _detail = data.getDetailOrder();
        if (_detail == null || _detail.getFoodId() == null) {
            return;
        }
        ArrayList<DetailOrder> details = order.getDetailOrders();
        int size = details.size();

        for (int i = 0; i < size; i++) {
            if (_detail.getFoodId().equals(details.get(i).getFoodId())) {
                details.remove(i);
                if (detailOrderAdapter != null) {
                    RecyclerView.Adapter adapter = detailOrderAdapter.getAdapter();
                    detailOrderAdapter.getList().remove(i);
                    adapter.notifyItemRemoved(i);
                }
                break;
            }
        }
    }

    // Order đã được remove
    // lắng nghe từ socket
    @Override
    public void onOrderRemoved() {
        FLAG_BACK_TO_PREV_ACTIVITY = true;
        error = getString(R.string.order_removed);

        showMessage(R.string.order_removed, COLOR_ERROR);
        if (isViewAttached()) {
            getView().onExit();
        }
    }

    @Override
    public Context getContext() {
        return isViewAttached() ? getView().getContext() : null;
    }

    /*
    * End IOrderVM
    * */

    private void showCreatingNewOrder() {
        showProgress(R.string.creating_new_order);
    }

    private void showLoadingOrder() {
        showProgress(R.string.loading_order);
    }

    // Người dùng bấm vào menu item phục vụ
    public void onClickMenuServeOrder() {
        if (order == null) {
            showMessage(R.string.not_found_order, Constant.COLOR_ERROR);
            return;
        }

        int statusOrder = order.getStatusFlag();

        if (statusOrder == OrderFlag.CREATING) {
            showMessage(R.string.msg_cant_serve_order, Constant.COLOR_ERROR);
        }
        // order đã được tạo, chờ bếp xác nhận
        // có thể hủy
        else if (statusOrder == OrderFlag.PENDING) {
            // mở hộp thoại xác nhận hủy order với nội dung xác định

            OrderGetCallBackImpl removeCallback = new OrderGetCallBackImpl(this);
            removeCallback.setTag(METHOD_REMOVE_ORDER);
            orderInfoView.openConfirmDialog(R.string.message_remove_order_in_pending, removeCallback);
        }
        // order đã bắt đầu nấu hoặc đã nấu xong
        // có thể thanh toán
        else if(statusOrder == OrderFlag.PREPARE){

            orderInfoView.openConfirmDialog(R.string.message_confirm_prepare_order, new GetCallback<Void>() {
                @Override
                public void onFinish(Void aVoid) {
                    onConfirmPrepareOrder();
                }
            });
        }
    }

    // Người dùng bấm vào menu item thanh toán
    public void onClickMenuPayOrder() {
        if (order == null) {
            showMessage(R.string.not_found_order, Constant.COLOR_ERROR);
            return;
        }

        int status = order.getStatusFlag();

        if (status >= OrderFlag.COOKING && status <= OrderFlag.EATING) {
            boolean available = true;
//            ArrayList<DetailOrder> details = order.getDetailOrders();
//            for (DetailOrder detail : details) {
//                int _status = detail.getStatusFlag();
//                if (_status <= OrderFlag.PENDING) {
//                    available = false;
//                    break;
//                }
//            }
            if (!available) {
                showMessage(R.string.has_food_not_cooked_yet, Constant.COLOR_ERROR);
            }else{
                onClickPayOrder();
            }
        }
        else{
            showMessage(R.string.msg_cant_pay_order, Constant.COLOR_ERROR);
        }
    }

    // Chọn thanh toán hóa đơn
    private void onClickPayOrder() {
        OrderGetCallBackImpl payCallback = new OrderGetCallBackImpl(this);
        payCallback.setTag(METHOD_PAY_ORDER);
        orderInfoView.openConfirmDialog(R.string.message_confirm_pay_order, payCallback);
    }

    // Xác nhận đã dọn đồ ăn ra cho khách
    public void onConfirmPrepareOrder() {
        WeakHashMap<String, Object> map =
                OrderParams.createUpdateStatus(order.getId(), OrderFlag.EATING);

        requestUpdateStatusOrder(map, false);
    }

    // Xác nhận muốn thanh toán
    public void onConfirmPayOrder() {
        WeakHashMap<String, Object> map =
                OrderParams.createUpdateStatus(order.getId(), OrderFlag.PAYING);

        requestUpdateStatusOrder(map, true);
    }

    @Override
    public void onRequestRemoveDetailOrder(String detailOrderID) {
        if (orderInfoView != null) {
            orderInfoView.openConfirmDialog(R.string.title_confirm_order,
                    R.string.ask_confirm_remove_detail_order, new GetCallback<Void>() {
                        @Override
                        public void onFinish(Void aVoid) {
                            onRemoveDetailOrder(detailOrderID);
                        }
                    });
        }
    }

    private void onRemoveDetailOrder(String detailOrderID) {
        showProgress(R.string.removing_detail_order);
        getToken();
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("order_id", order.getId());
        map.put("detail_order_id", detailOrderID);

        orderRM.removeStatusDetailOrder(token, map, new GetCallback<OrderResponse>() {
            @Override
            public void onFinish(OrderResponse response) {
                if (response.isSuccess()) {
                    order = response.getOrder();
                    if (displayer != null) {
                        displayer.showFinalCost(order);
                    }
                    ArrayList<DetailOrder> details = order.getDetailOrders();
                    detailOrderAdapter.clearAll();
                    detailOrderAdapter.onGetList(details);
                }else{
                    showMessage(R.string.message_process_failed, Constant.COLOR_ERROR);
                }
                hideProgress();
            }
        });
    }

    @Override
    public void onRequestUpdateDetailOrderStatus(String detailOrderID, int status) {
        if (orderInfoView != null) {
            orderInfoView.openConfirmDialog(R.string.title_confirm_order,
                    R.string.ask_confirm_preapre_detail_order, new GetCallback<Void>() {
                        @Override
                        public void onFinish(Void aVoid) {
                            updateDetailOrderStatus(detailOrderID, status);
                        }
                    });
        }
    }

    private void updateDetailOrderStatus(String detailOrderID, int status){
        showProgress(R.string.confirming);

        getToken();
        WeakHashMap<String, Object> map =
                OrderParams.updateDetailOrderStatus(order.getId(), detailOrderID, status);
        setupForNewRequest();

        RxDisposableSubscriber subscriber = new RxDisposableSubscriber(this, Index.UPDATE_DETAIL_ORDER_STATUS);
        disposable = asyncProcessor.subscribeWith(subscriber);

        orderRM.updateDetailOrder(token, map).subscribe(asyncProcessor);
    }

    private void onGetUpdateDetailOrderStatusResponse(UpdateDetailOrderStatusResponse response) {
        hideProgress();
        if (response.isSuccess()) {
            order = response.getOrder();
            String detailOrderID = response.getDetailOrderID();
            ArrayList<DetailOrder> details = order.getDetailOrders();
            for (DetailOrder detail : details) {
                String _id = detail.getId();
                if (detailOrderID != null && detailOrderID.equals(_id)) {
                    detailOrderAdapter.onUpdateItem(detail);
                    break;
                }
            }
        }
        else{
            showMessage(response.getMessage(), Constant.COLOR_ERROR);
        }
    }

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

                case Index.REPROCESS_ORDER:
                    ResponseValue responseValue = (ResponseValue) object.getValue();
                    onReprocessOrderFinish(responseValue);
                    break;
            }
        }
    }

    /*
    * OnChangeSocketStateListener
    * */

    @Override
    public void onSocketConnect() {

    }

    @Override
    public void onSocketDisconnect() {
        if (isViewAttached()) {
            showMessage(R.string.server_disconnect, Constant.COLOR_WARNING);
            getView().onExit();
        }
        else{
            FLAG_BACK_TO_PREV_ACTIVITY = false;
            error = getString(R.string.server_disconnect);
        }
    }

    // Người dùng bấm nút XÁC NHẬN khi muốn xác nhận dữ liệu mới của hóa đơn
    public void onCLickReProcessOrder() {
        getView().openConfirmDialog(R.string.title_confirm,
                R.string.ask_confirm_reprocess_order,
                new GetCallback<Void>() {
                    @Override
                    public void onFinish(Void aVoid) {
                        reProcessOrder();
                    }
        });
    }

    private void reProcessOrder() {
        showProgress(R.string.confirming);

        getToken();
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("order_id", order.getId());

        setupForNewRequest();

        RxDisposableSubscriber subscriber = new RxDisposableSubscriber(this, Index.REPROCESS_ORDER);
        disposable = asyncProcessor.subscribeWith(subscriber);

        orderRM.reProcessOrder(token, map).subscribe(asyncProcessor);
    }

    private void onReprocessOrderFinish(ResponseValue response) {
        if (response.isSuccess()) {
            if (isViewAttached()) {
                getView().onExit();
            }
        }else{
            showMessage(response.getMessage(), Constant.COLOR_ERROR);
        }
    }

    /*
    * End OnChangeSocketStateListener
    * */

    private @interface Index{
        int UPDATE_DETAIL_ORDER_STATUS = 0;
        int REPROCESS_ORDER = 1;
    }
}
