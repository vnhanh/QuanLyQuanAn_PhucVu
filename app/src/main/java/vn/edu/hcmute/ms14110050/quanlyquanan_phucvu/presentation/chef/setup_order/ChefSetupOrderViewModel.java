package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.WeakHashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IRecyclerAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.FullOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderParams;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.socket_listener.OrderSocketListener;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_ERROR;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_SUCCESS;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_WARNING;


/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public class ChefSetupOrderViewModel extends BaseNetworkViewModel<ISetupOrder.View> implements IOrderVM {
    public final ObservableField<Integer> toolbarTitle = new ObservableField<Integer>();

    private OrderSocketListener orderListener;

    private OrderRequestManager orderRM;

    private Order order;

    public final ObservableField<String> creater = new ObservableField<String>();
    public final ObservableBoolean isInValidCreatedTime = new ObservableBoolean(false);
    public final ObservableField<String> createdDate = new ObservableField<String>();
    public final ObservableField<String> numberCustomer = new ObservableField<String>();
    public final ObservableField<String> orderStatus = new ObservableField<String>();
    public final ObservableField<String> totalCost = new ObservableField<String>();
    public final ObservableField<String> descriptionOrder = new ObservableField<String>();

    // listener lắng nghe thay đổi dữ liệu của bàn và món
    private IRecyclerAdapter<Table> tableDataListener;
    private IRecyclerAdapter<Food> foodDataListener;

    private boolean FLAG_BACK_TO_PREV_ACTIVITY = false;
    private String error = "";

    // cờ xác nhận trạng thái đang chờ xác nhận nấu của order
    private boolean PENDING_STATUS = false;

    private Handler handler = new Handler();

    /*
    * Property
    * */

    public String getOrderID() {
        return order != null ? order.getId() : "";
    }

    public void setOrderID(String orderID) {
        order = new Order();
        order.setId(orderID);
    }

    public void setTableDataListener(IRecyclerAdapter<Table> tableDataListener) {
        this.tableDataListener = tableDataListener;
    }

    public void setFoodDataListener(IRecyclerAdapter<Food> foodDataListener) {
        this.foodDataListener = foodDataListener;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    /*
    * End Property
    * */

    public ChefSetupOrderViewModel() {

    }

    private void createOrderSocketListener() {
        if (orderListener == null) {
            Log.d("LOG", getClass().getSimpleName() + ":createOrderSocketListener()");
            orderListener = new OrderSocketListener();
            orderListener.setCenterViewModel(this);
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
            getView().onBackPressed();
        }

        createOrderSocketListener();
        orderListener.startListening();

        createOrderRequestManager();

        toolbarTitle.set(R.string.title_confirm_order);

        loadOrder();
    }

    private void showInformationOrder() {
        if (isViewAttached() && order != null) {
            String _creater = getString(R.string.display_creater_order, order.getWaiterFullname());
            creater.set(_creater);

            showCreatedTime();

            showNumberCustomer();
            showStatusOrder();
            showFinalCost();
            showDescriptionOrder();

            PENDING_STATUS = order.getStatusFlag() == OrderFlag.PENDING;

            if (isViewAttached()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean updateMenuResult = getView().onUpdateMenu(order.getStatusFlag());
                        if (!updateMenuResult) {
                            handler.postDelayed(this, 500);
                        }
                    }
                }, 500);
            }
        }
    }

    private void showCreatedTime() {
        String dateTimeStr = "";

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        SimpleDateFormat inputFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        try {
            Date date = inputFormat.parse(order.getCreatedTime());

            dateTimeStr = outputFormat.format(date);
            isInValidCreatedTime.set(false);
        } catch (ParseException e) {
            e.printStackTrace();
            dateTimeStr = " " + getString(R.string.not_determine);
            isInValidCreatedTime.set(true);
        }

        String dateTime = getResources().getString(R.string.content_created_date, dateTimeStr);
        createdDate.set(dateTime);
    }

    private void showNumberCustomer() {
        numberCustomer.set(getString(R.string.display_number_customer, order.getCustomerNumber()));
    }

    private void showStatusOrder() {
        orderStatus.set(getString(R.string.display_status_order, getString(order.getStatusValue())));
    }

    private void showFinalCost() {
        totalCost.set(getString(R.string.display_total_cost_order, order.getFinalCost()));
    }

    private void showDescriptionOrder() {
        descriptionOrder.set(getString(R.string.display_description_order, order.getDescription()));
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        orderListener.stopListening();
    }

    @Override
    public void onDestroy() {
        orderListener.destroy();
        orderListener = null;
        orderRM = null;
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
                if (orderResponse.isSuccess()) {
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

    /*
    * DATA BINDING
    * */

    /*
    * End DATA BINDING
    * */

    /*
    * IOrderVM
    * */

    private String token;

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
    public FoodSocketService getFoodSocketService() {
        return null;
    }

    @Override
    public String getToken() {
        if (token == null) {
            token = SSharedReference.getToken(getContext());
        }
        return token;
    }

    @Override
    public void onOrderUpdatedStatus(Order order) {
        this.order = order;
        showInformationOrder();
        if (isViewAttached()) {
            getView().onAnimationShowStatus();
        }

        int flag = order.getStatusFlag();

        switch (flag) {
            case OrderFlag.COOKING:
                showMessage(R.string.order_updated_cooking, COLOR_WARNING);
                return;

            case OrderFlag.PREPARE:
                showMessage(R.string.order_updated_preapare, COLOR_WARNING);
                return;

            case OrderFlag.EATING:
                showMessage(R.string.order_updated_eating, COLOR_WARNING);

                if (isViewAttached()) {
                    getView().onToast(R.string.msg_exit_setup_order);
                    getView().onBackPressed();
                }
                else{
                    FLAG_BACK_TO_PREV_ACTIVITY = true;
                    error = getString(R.string.msg_exit_setup_order);
                }
                return;

            default:
                if (isViewAttached()) {
                    getView().onToast(R.string.msg_exit_setup_order);
                    getView().onBackPressed();
                }
                else{
                    FLAG_BACK_TO_PREV_ACTIVITY = true;
                    error = getString(R.string.msg_exit_setup_order);
                }
        }
    }

    // Khi socket lắng nghe dữ liệu order hoặc lấy được dữ liệu từ request manager
    @Override
    public void onDetailOrderUpdated(UpdateDetailOrderSocketData data) {
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
                    if (foodDataListener != null) {
                        RecyclerView.Adapter adapter = foodDataListener.getAdapter();
                        adapter.notifyItemChanged(i);
                    }
                    break;
                }
            }
        }
    }

    private void onUpdateFinalCost() {
        showFinalCost();
        if (isViewAttached()) {
            getView().onAnimationFinalCost();
        }
    }

    // Khi socket lắng nghe dữ liệu order hoặc lấy được dữ liệu từ request manager
    @Override
    public void onDetailOrderRemoved(UpdateDetailOrderSocketData data) {
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
                if (foodDataListener != null) {
                    RecyclerView.Adapter adapter = foodDataListener.getAdapter();
                    foodDataListener.getList().remove(i);
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

        if (isViewAttached()) {
            getView().onToast(R.string.order_removed);
            getView().onBackPressed();
        }
    }

    @Override
    public Context getContext() {
        return isViewAttached() ? getView().getContext() : null;
    }

    /*
    * End IOrderVM
    * */

    private void showLoadingOrder() {
        showProgress(R.string.loading_order);
    }


    // Khi người dùng bấm vào menu XÁC NHẬN
    public void onClickConfirmMenu() {
        if (order != null && (order.getStatusFlag()== OrderFlag.PENDING || order.getStatusFlag()== OrderFlag.COOKING)) {
            createOrderRequestManager();

            WeakHashMap<String, Object> map =
                    OrderParams.createUpdateStatus(order.getId(), order.getStatusFlag() + 1);

            // order đang chờ xác nhận nấu
            if (PENDING_STATUS) {
                showProgress(R.string.message_confirm_start_cooking);
            }else{
                showProgress(R.string.message_confirm_finish_cook);
            }

            orderRM.updateStatusOrder(token, map, new GetCallback<OrderResponse>() {
                @Override
                public void onFinish(OrderResponse response) {
                    if (response.isSuccess()) {
                        showMessage(R.string.message_update_status_order_success, COLOR_SUCCESS);
                    }else{
                        showMessage(R.string.message_update_status_order_failed, COLOR_ERROR);
                    }
                    hideProgress();
                }
            });
        }
    }
}
