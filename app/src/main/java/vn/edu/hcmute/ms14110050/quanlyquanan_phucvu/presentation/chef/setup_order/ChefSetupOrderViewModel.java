package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.WeakHashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable.RxDisposableSubscriber;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable.SendObject;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IRecyclerAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.FullOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateDetailOrderStatusResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderParams;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.ChefListOrdersViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder.IDetailOrderProcessor;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.helper.SetupOrderRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.socket_listener.OrderSocketListener;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_WARNING;


/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public class ChefSetupOrderViewModel extends BaseNetworkViewModel<ISetupOrder.View>
        implements IOrderVM, IDetailOrderProcessor {

    public final ObservableField<Integer> toolbarTitle = new ObservableField<Integer>();

    private SetupOrderRequestManager resManager;
    private OrderSocketListener orderListener;

    private Order order;

    public final ObservableField<String> creater = new ObservableField<String>();
    public final ObservableBoolean isInValidCreatedTime = new ObservableBoolean(false);
    public final ObservableField<String> createdDate = new ObservableField<String>();
    public final ObservableField<String> numberCustomer = new ObservableField<String>();
    public final ObservableField<String> orderStatus = new ObservableField<String>();
    public final ObservableField<String> totalCost = new ObservableField<String>();
    public final ObservableBoolean isShowDescription = new ObservableBoolean();
    public final ObservableField<String> descriptionOrder = new ObservableField<String>();

    // listener lắng nghe thay đổi dữ liệu của bàn và món
    private IRecyclerAdapter<Table> tableDataListener;
    private IRecyclerAdapter<DetailOrder> detailOrderAdapter;

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

    public void setDetailOrderAdapter(IRecyclerAdapter<DetailOrder> detailOrderAdapter) {
        this.detailOrderAdapter = detailOrderAdapter;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    /*
    * End Property
    * */

    public ChefSetupOrderViewModel() {
        resManager = new SetupOrderRequestManager();
    }

    private void createOrderSocketListener() {
        if (orderListener == null) {
            orderListener = new OrderSocketListener();
            orderListener.setCenterViewModel(this);
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

        resManager.onStart(getContext());

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
        String des = order.getDescription();
        isShowDescription.set(!StrUtil.isEmpty(des));

        if (isShowDescription.get()) {
            descriptionOrder.set(getString(R.string.display_description_order, order.getDescription()));
        }
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();

        orderListener.stopListening();
        resManager.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        orderListener.destroy();
        orderListener = null;
        resManager = null;
        tableDataListener = null;
        detailOrderAdapter = null;
    }

    // load thông tin order theo orderID
    // sau khi load xong thì listen socket table và food
    // load danh sách table và food theo orderID
    private void loadOrder() {
        if (order == null || order.getId() == null) {
            return;
        }

        showProgress(R.string.loading_order);

        setupForNewRequest();
        RxDisposableSubscriber subscriber = new RxDisposableSubscriber(this, Index.LOAD_ORDER);
        disposable = asyncProcessor.subscribeWith(subscriber);
        resManager.loadOrder(order.getId()).subscribe(asyncProcessor);
    }

    private void onGetFullOrderResponse(FullOrderResponse response){
        if (response.isSuccess()) {
            order = response.getOrder();
            ArrayList<Table> tables = response.getTables();
            tableDataListener.onGetList(tables);
            ArrayList<DetailOrder> details = order.getDetailOrders();
            detailOrderAdapter.onGetList(details);
            showInformationOrder();
        }else{
            if (isViewAttached()) {
                Toast.makeText(getContext(), R.string.get_order_failed, Toast.LENGTH_SHORT).show();
            }
        }
        hideProgress();
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

    // Khi người dùng bấm vào menu XÁC NHẬN
    public void onClickConfirmMenu() {
        if (order != null && (order.getStatusFlag()== OrderFlag.PENDING || order.getStatusFlag()== OrderFlag.COOKING)) {

            WeakHashMap<String, Object> map =
                    OrderParams.createUpdateStatus(order.getId(), order.getStatusFlag() + 1);

            // order đang chờ xác nhận nấu
            if (PENDING_STATUS) {
                showProgress(R.string.message_confirm_start_cooking);
            }else{
                showProgress(R.string.message_confirm_finish_cook);
            }

            setupForNewRequest();

            RxDisposableSubscriber subscriber = new RxDisposableSubscriber(this, Index.COOK_ORDER);
            disposable = asyncProcessor.subscribeWith(subscriber);
            resManager.processCooking(map).subscribe(asyncProcessor);
        }
    }

    private void onGetOrderResponse(OrderResponse response) {
        if (response.isSuccess()) {
            order = response.getOrder();
            showStatusOrder();
            if (detailOrderAdapter != null) {
                ArrayList<DetailOrder> detailOrders = order.getDetailOrders();
                detailOrderAdapter.clearAll();
                detailOrderAdapter.onGetList(detailOrders);
            }
            showMessage(R.string.message_process_success, Constant.COLOR_SUCCESS);
        }
        else{
            String message = response.getMessage();
            if (StrUtil.isEmpty(message)) {
                message = getString(R.string.message_process_failed);
            }
            showMessage(message, Constant.COLOR_ERROR);
        }
        hideProgress();
    }

    /*
    * DisposableObserver interface
    * */

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);

        if (arg instanceof SendObject) {
            SendObject object = (SendObject) arg;
            int tag = object.getTag();
            switch (tag) {
                case Index.LOAD_ORDER:
                    FullOrderResponse response = (FullOrderResponse) object.getValue();
                    onGetFullOrderResponse(response);
                    break;

                case Index.COOK_ORDER:
                    OrderResponse orderResponse = (OrderResponse) object.getValue();
                    onGetOrderResponse(orderResponse);
                    break;

                case Index.UPDATE_DETAIL_ORDER_STATUS:
                    UpdateDetailOrderStatusResponse updateDetailResponse = (UpdateDetailOrderStatusResponse) object.getValue();
                    onGetUpdateDetailOrderStatusResponse(updateDetailResponse);
                    break;
            }
        }
    }

    /*
    * End
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

    private void updateStatusDetailOrder(DetailOrder detailOrder, int newStatus) {
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
            order = response.getOrder();
            showInformationOrder();

            String detailOrderID = response.getDetailOrderID();
            if (detailOrderID == null) {
                Log.d("LOG", getClass().getSimpleName()
                        + ":on get update detail order status response:detailOrderID is null");
                return;
            }
            ArrayList<DetailOrder> details = order.getDetailOrders();
            for (DetailOrder detail : details) {
                String _id = detail.getId();
                if (detailOrderID.equals(_id)) {
                    detailOrderAdapter.onUpdateItem(detail);
                    break;
                }
            }
        }
        else{
            showMessage(response.getMessage(), Constant.COLOR_ERROR);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface Index{
        int LOAD_ORDER = 0;
        int COOK_ORDER = 1;
        int UPDATE_DETAIL_ORDER_STATUS = 2;
    }
}
