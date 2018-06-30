package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.WeakHashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputProcessorCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.RegionTableSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.FullOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.OrderRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestManger;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.table.RegionTableRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IListViewAdapterListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food.InputCountCallback;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public class SetupOrderViewModel extends BaseNetworkViewModel<ISetupOrder.View> implements IOrderVM, InputProcessorCallback {
    public final ObservableBoolean isCreateOrder = new ObservableBoolean(true);
    public final ObservableField<Integer> toolbarTitle = new ObservableField<Integer>();

    public final ObservableBoolean isMainView = new ObservableBoolean(true);

    private OrderSocketListener orderListener;
    private TableSocketListener tableListener;
    private FoodSocketListener foodListener;

    private OrderRequestManager orderRM;
    private RegionTableRequestManager tableRM;
    private FoodRequestManger foodRM;

    private int processMode;
    private Order order;

    public final ObservableField<String> creater = new ObservableField<String>();
    public final ObservableBoolean isInValidCreatedTime = new ObservableBoolean(false);
    public final ObservableField<String> createdDate = new ObservableField<String>();
    public final ObservableField<String> numberCustomer = new ObservableField<String>();
    public final ObservableField<String> orderStatus = new ObservableField<String>();
    public final ObservableField<String> totalCost = new ObservableField<String>();
    public final ObservableField<String> descriptionOrder = new ObservableField<String>();


    // listener lắng nghe thay đổi dữ liệu của bàn và món
    private IListViewAdapterListener<Table> tableDataListener;
    private IListViewAdapterListener<Food> foodDataListener;

    private User waiter;

    private boolean FLAG_BACK_TO_PREV_ACTIVITY = false;
    private String error = "";

    private boolean FLAG_CREATING_NEW_ORDER = false;

    /*
    * Property
    * */

    public int getProcessMode() {
        return processMode;
    }

    public void setProcessMode(int processMode) {
        Log.d("LOG", getClass().getSimpleName() + ":setProcessMode()");
        this.processMode = processMode;
        isCreateOrder.set(processMode == OrderMode.ADD);
        FLAG_CREATED_ORDER_ON_SERVER = processMode == OrderMode.VIEW;
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

    public void setTableDataListener(IListViewAdapterListener<Table> tableDataListener) {
        this.tableDataListener = tableDataListener;
    }

    public void setFoodDataListener(IListViewAdapterListener<Food> foodDataListener) {
        this.foodDataListener = foodDataListener;
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
            order.setId(UUID.randomUUID().toString().replace("-"," "));
            order.setWaiterUsername(waiter.getUsername());
            order.setWaiterFullname(waiter.getFullname());
            order.setStatusFlag(OrderFlag.CREATING);
            // TODO: làm layout nhập số lượng khách
            order.setCustomerNumber(1);
        }
    }

    /*
    * End Property
    * */

    public SetupOrderViewModel() {

    }

    private void createOrderSocketListener() {
        if (orderListener == null) {
            orderListener = new OrderSocketListener();
        }
    }

    private void createTableRequestManager() {
        if (tableRM == null) {
            tableRM = new RegionTableRequestManager();
        }
    }

    private void createFoodRequestManager() {
        if (foodRM == null) {
            foodRM = new FoodRequestManger();
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
            orderRM = new OrderRequestManager();
        }
    }

    @Override
    public void onViewAttach(@NonNull ISetupOrder.View viewCallback) {
        super.onViewAttach(viewCallback);
        if (FLAG_BACK_TO_PREV_ACTIVITY) {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            getView().onBackPrevActivity();
        }

        if (FLAG_CREATING_NEW_ORDER) {
            showCreatingNewOrder();
        }

        createOrderSocketListener();
        createTableSocketListener();
        createFoodListener();
        createOrderRequestManager();
        createTableRequestManager();
        createFoodRequestManager();

        if (isCreateOrder.get()) {
            // socket listen table và food
            tableListener.listenSockets(tableDataListener, this);
            foodListener.listenSockets(foodDataListener, this);
        }

        initView();

        Log.d("LOG", getClass().getSimpleName() + ":onViewAttached():FLAG_CREATED_ORDER_ON_SERVER:" + FLAG_CREATED_ORDER_ON_SERVER);
        if (FLAG_CREATED_ORDER_ON_SERVER) {
            loadOrder();
        }else{
            createNewOrder();
        }
    }

    // cờ đánh dấu đã thực hiện thao tác tạo order trên server
    private boolean FLAG_CREATED_ORDER_ON_SERVER = false;

    private void createNewOrder() {
        if (!FLAG_CREATED_ORDER_ON_SERVER && isCreateOrder.get() && order != null && orderRM != null) {
            Log.d("LOG", getClass().getSimpleName()+":createNewOrder()");

            showCreatingNewOrder();
            FLAG_CREATED_ORDER_ON_SERVER = true;

            getToken();
            WeakHashMap<String, Object> map = new WeakHashMap<>();
            map.put("id", order.getId());
            map.put("waiter_username", order.getWaiterUsername());
            map.put("waiter_fullname", order.getWaiterFullname());
            map.put("flag_status", order.getStatusFlag());
            map.put("flag_set_table", order.getStatusFlag());
            map.put("number_customer", order.getCustomerNumber());

            orderRM.createOrder(token, map, new GetCallback<OrderResponse>() {
                @Override
                public void onFinish(OrderResponse response) {
                    FLAG_CREATING_NEW_ORDER = false;
                    hideProgress();
                    if (response.getSuccess()) {
                        FLAG_CREATED_ORDER_ON_SERVER = true;
                        Log.d("LOG", SetupOrderViewModel.class.getSimpleName() + ":createOrder():success");
                        order = response.getOrder();
                        showInformationOrder();
                    }else{
                        FLAG_CREATED_ORDER_ON_SERVER = false;
                        if (isViewAttached()) {
                            Toast.makeText(getContext(), getString(R.string.error_cant_create_new_order) + ". " + response.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            getView().onBackPrevActivity();
                        }else{
                            FLAG_BACK_TO_PREV_ACTIVITY = true;
                            error = getString(R.string.error_cant_create_new_order);
                        }
                    }
                }
            });
        }
    }

    private void showInformationOrder() {
        if (isViewAttached() && order != null) {
            String _creater = getString(R.string.display_creater_order, waiter.getFullname());
            creater.set(_creater);

            String dateTimeStr = "";
            if (!StringUtils.isEmpty(order.getCreatedTime())) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                try {
                    dateTimeStr = sdf.format(inputFormat.parse(order.getCreatedTime()));
                    isInValidCreatedTime.set(false);
                } catch (ParseException e) {
                    e.printStackTrace();
                    dateTimeStr = " " + getString(R.string.not_determine);
                    isInValidCreatedTime.set(true);
                }
            }else{
                dateTimeStr = " " + getString(R.string.not_determine);
                isInValidCreatedTime.set(true);
            }
            String dateTime = getResources().getString(R.string.content_created_date, dateTimeStr);
            createdDate.set(dateTime);

            numberCustomer.set(getString(R.string.display_number_customer, order.getCustomerNumber()));
            showStatusOrder();
            totalCost.set(getString(R.string.display_total_cost_order, order.getFinalCost()));
            descriptionOrder.set(getString(R.string.display_description_order, order.getDescription()));
        }
    }

    @Override
    public void onViewDetached() {
        orderRM = null;
        tableRM = null;
        foodRM = null;
        orderListener.destroy();
        orderListener = null;
        tableListener.destroy();
        tableListener = null;
        foodListener.destroy();
        foodListener = null;
        numberCustomerListener = null;

        super.onViewDetached();
    }

    @Override
    public void onDestroy() {
        tableDataListener = null;
        foodDataListener = null;
        super.onDestroy();
    }

    // load thông tin order theo orderID
    // sau khi load xong thì listen socket table và food
    // load danh sách table và food theo orderID
    private void loadOrder() {
        if (order == null || order.getId() == null) {
            return;
        }
        showLoadingOrder();
        getToken();
        orderRM.getOrder(token, order.getId(), new GetCallback<FullOrderResponse>() {
            @Override
            public void onFinish(FullOrderResponse orderResponse) {
                if (orderResponse.getSuccess()) {
                    order = orderResponse.getOrder();
                    ArrayList<Table> tables = orderResponse.getTables();
                    tableDataListener.onGetList(tables);
                    ArrayList<Food> foods = orderResponse.getFoods();
                    foodDataListener.onGetList(foods);
                    showInformationOrder();
                }else{
                    if (isViewAttached()) {
                        Toast.makeText(getContext(), R.string.get_order_failed, Toast.LENGTH_SHORT).show();
                    }
                }
                hideProgress();
            }
        });
    }

    private void initView() {
        // toolbar title
        if (isCreateOrder.get()) {
            toolbarTitle.set(R.string.title_toolbar_create_order);
        }
    }

    // được gọi sau khi load thành công order
    private void loadOrderedTables() {
        if (isViewAttached()) {
            getView().onShowLoadTablesByOrderIDProgress();
        }
        if (StringUtils.isEmpty(token)) {
            token = SSharedReference.getToken(getView().getContext());
        }
        tableRM.loadListSelectedTable(token, order.getId(), new GetCallback<ArrayList<Table>>() {
            @Override
            public void onFinish(ArrayList<Table> tables) {
                onGetTablesByOrderID(tables);
            }
        });
    }

    private void onGetTablesByOrderID(ArrayList<Table> tables) {
        if (isViewAttached()) {
            tableDataListener.onGetList(tables);
            getView().onHideLoadTablesByOrderIDProgress();
        }
    }

    /*
    * DATA BINDING
    * */
    // Bấm nút onBack toolbar
    public void onClickBackButton() {
        if (!isMainView.get()) {
            isMainView.set(true);
        }
    }

    // Bấm nút TẠO hoặc SỬA order
    public void onClickProcessData() {

    }

    public void onClickSelectFoods() {
        isMainView.set(false);
    }

    private InputCallback numberCustomerListener;

    public void onClickNumberCustomerView() {
        if (isViewAttached()) {
            if (numberCustomerListener == null) {
                numberCustomerListener = new InputCountCallback(this);
            }
            currentNumberCustomerInput = order.getCustomerNumber();
            getView().openInputNumberCustomerView(currentNumberCustomerInput, numberCustomerListener);
        }
    }

    private int currentNumberCustomerInput = 0;

    @Override
    public void onChangeInputValue(TextInputLayout til, EditText edt, String input) {
        String error = "";
        String newText = "";
        int count = 0;

        try {
            count = input.equals("") ? 0 : Integer.parseInt(input);

            if (count < 0) {
                error = getString(R.string.number_customer_must_not_negative);
                newText = "0";
            }else if(count > 0){
                newText = String.valueOf(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = getString(R.string.error_wrong_format_input);
            newText = "0";
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
        edt.setText(newText);
    }

    @Override
    public void onSubmitInputProcessor() {
        if (currentNumberCustomerInput == order.getCustomerNumber()) {
            return;
        }
        showProgres(R.string.message_processing);
        createOrderRequestManager();
        getToken();

        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("orderID", order.getId());
        map.put("numberCustomer", currentNumberCustomerInput);

        orderRM.updateNumberCustomer(token, map, new GetCallback<ResponseValue>() {
            @Override
            public void onFinish(ResponseValue responseValue) {

            }
        });
    }

    private void showProgres(@StringRes int messageResId) {
        if (isViewAttached()) {
            getView().showProgress(messageResId);
        }
    }

    /*
    * End DATA BINDING
    * */

    /*
    * IOrderVM
    * */
    private String token;

    @Override
    public void onRequestAddTableToOrder(final Table table, final GetCallback<TableResponse> callback) {
        getToken();
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("orderID", getOrderID());
        map.put("tableID", table.getId());

        createOrderRequestManager();

        orderRM.orderTable(token, map, new GetCallback<TableResponse>() {
            @Override
            public void onFinish(TableResponse tableResponse) {
                if (tableResponse.getSuccess()) {
//                    Log.d("LOG", SetupOrderViewModel.class.getSimpleName()
//                            + ":onRequestAddTableToOrder():tableResponse:success");

                    // nếu thành công thì thêm bàn vào recyclerview
                    order.getTables().add(table.getId());
                    tableDataListener.onAddItem(tableResponse.getTable());
                }else{
                    Log.d("LOG", SetupOrderViewModel.class.getSimpleName()
                            + ":onRequestAddTableToOrder():tableResponse:failed:"+tableResponse.getMessage());
                }
                callback.onFinish(tableResponse);
            }
        });
    }

    @Override
    public void onRequestRemoveTableFromOrder(final String tableID, final GetCallback<TableResponse> callback) {
        createOrderRequestManager();
        getToken();
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("tableID", tableID);
        map.put("orderID", getOrderID());
        orderRM.removeTableFromOrder(token, map, new GetCallback<TableResponse>() {
            @Override
            public void onFinish(TableResponse response) {
                if (response.getSuccess()) {
                    String tableID = response.getTable().getId();
                    order.getTables().remove(tableID);
                    tableDataListener.onRemoveItem(tableID);
                }else{
                    Log.d("LOG", SetupOrderViewModel.class.getSimpleName()
                            + ":onRequestRemoveTableFromOrder():failed:" + response.getMessage());
                }
                callback.onFinish(response);
            }
        });
    }

    @Override
    public RegionTableRequestManager getRegionTableRequestManager() {
        createTableRequestManager();
        return tableRM;
    }

    @Override
    public FoodRequestManger getFoodRequestManager() {
        createFoodRequestManager();
        return foodRM;
    }

    @Override
    public DetailOrder getDetailOrderByFood(String foodID) {
        Log.d("LOG", getClass().getSimpleName() + ":getDetailOrderByFood():foodID:" + foodID);
        if(order == null)
            return null;
        ArrayList<DetailOrder> details = order.getDetailOrders();
        for (DetailOrder detail : details) {
            if (detail.getFoodId() != null && detail.getFoodId().equals(foodID)) {
                Log.d("LOG", getClass().getSimpleName() + ":getDetailOrderByFood():find detail");
                return detail;
            }
        }
        Log.d("LOG", getClass().getSimpleName() + ":getDetailOrderByFood():find no detail");
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
    public void onUpdateStatusOrder(Order order) {
        this.order.setStatusFlag(order.getStatusFlag());
        showStatusOrder();
    }

    private void showStatusOrder() {
        orderStatus.set(getString(R.string.display_status_order, getString(order.getStatusValue())));
        if (isViewAttached()) {
            getView().onAnimationShowStatus();
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
        showProgres(R.string.creating_new_order);
    }

    private void showLoadingOrder() {
        showProgres(R.string.loading_order);
    }

    private void hideProgress() {
        if (isViewAttached()) {
            getView().hideProgress();
        }
    }
}
