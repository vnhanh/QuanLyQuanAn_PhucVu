package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.UUID;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.RegionTableSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.date_time.DateTimeRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestManger;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.table.RegionTableRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ITableDataListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public class SetupOrderViewModel extends BaseNetworkViewModel<ISetupOrder.View> implements IOrderVM {
    public final ObservableBoolean isCreateOrder = new ObservableBoolean(true);
    public final ObservableField<Integer> toolbarTitle = new ObservableField<Integer>();
    public final ObservableBoolean isCreatedDateFailed = new ObservableBoolean(false);
    public final ObservableField<String> createdDate = new ObservableField<String>();

    private TableSocketListener tableListener;

    private DateTimeRequestManager dateTimeRM;
    private RegionTableRequestManager tableRequestManager;
    private FoodRequestManger foodRequestManager;

    private int processMode;
    private Order order;
    // view lắng nghe thay đổi dữ liệu của các bàn
    private ITableDataListener tableDataListener;
    private User waiter;

    /*
    * Property
    * */

    public int getProcessMode() {
        return processMode;
    }

    public void setProcessMode(int processMode) {
        this.processMode = processMode;
        isCreateOrder.set(processMode == OrderMode.ADD);
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

    public void setTableDataListener(ITableDataListener tableDataListener) {
        this.tableDataListener = tableDataListener;
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
        order = new Order();
        order.setId(UUID.randomUUID().toString().replace("-"," "));
        order.setWaiterUsername(waiter.getUsername());
        order.setWaiterFullname(waiter.getFullname());
    }

    /*
    * End Property
    * */

    public SetupOrderViewModel() {
        if (getProcessMode() == OrderMode.ADD) {
            dateTimeRM = new DateTimeRequestManager();
        }
        tableListener = new TableSocketListener();
    }

    @Override
    public void onViewAttach(@NonNull ISetupOrder.View viewCallback) {
        super.onViewAttach(viewCallback);

        tableRequestManager = new RegionTableRequestManager();
        foodRequestManager = new FoodRequestManger();

        initView();

        if (!isCreateOrder.get()) {
            loadOrder();
        }else{
            // socket listen table và food (thiếu food)
            tableListener.listenSockets(tableDataListener, order.getId());
        }
    }

    // load thông tin order theo orderID
    // sau khi load xong thì listen socket table và food
    // load danh sách table và food theo orderID
    private void loadOrder() {
        //TODO
    }

    private void initView() {
        // toolbar title
        if (isCreateOrder.get()) {
            toolbarTitle.set(R.string.title_toolbar_create_order);
        }else{
            toolbarTitle.set(R.string.title_toolbar_edit_order);
        }

        // ngày lập order
        if (isCreateOrder.get()) {
            isCreatedDateFailed.set(false);
            createdDate.set(getString(R.string.loading_local_date_time));

            dateTimeRM.getLocalTime(new GetCallback<String>() {
                @Override
                public void onFinish(String result) {
                    if (StringUtils.isEmpty(result)) {
                        isCreatedDateFailed.set(true);
                        createdDate.set(getResources().getString(R.string.get_date_time_error));
                    }else{
                        isCreatedDateFailed.set(false);
                        String dateTime = getResources().getString(R.string.content_created_date, result);
                        createdDate.set(dateTime);
                    }
                }
            });
        }
    }

    // được gọi sau khi load thành công order
    private void loadListSelectedTable() {
        if (isViewAttached()) {
            getView().onShowLoadTablesByOrderIDProgress();
        }
        if (StringUtils.isEmpty(token)) {
            token = SSharedReference.getToken(getView().getContext());
        }
        tableRequestManager.loadListSelectedTable(token, order.getId(), new GetCallback<ArrayList<Table>>() {
            @Override
            public void onFinish(ArrayList<Table> tables) {
                onGetTablesByOrderID(tables);
            }
        });
    }

    private void onGetTablesByOrderID(ArrayList<Table> tables) {
        if (isViewAttached()) {
            tableDataListener.onGetTablesByOrderID(tables);
            getView().onHideLoadTablesByOrderIDProgress();
        }
    }

    /*
    * Data Binding
    * */
    // Bấm nút onBack toolbar
    public void onClickBackButton() {

    }

    // Bấm nút TẠO hoặc SỬA order
    public void onClickProcessData() {

    }

    /*
    * End Data Binding
    * */

    /*
    * IOrderVM
    * */
    private String token;

    @Override
    public void onRequestAddTableToOrder(final Table table, final GetCallback<TableResponse> callback) {
        getToken();
        tableRequestManager.addTableToOrder(token, table.getId(), order.getId(), new GetCallback<TableResponse>() {
            @Override
            public void onFinish(TableResponse response) {
                // thêm table vào order
                if (response.getSuccess()) {
                    // nếu thành công thì thêm bàn vào recyclerview
                    order.getTables().add(table.getId());
                    tableDataListener.onAddTable(response.getTable());
                }

                callback.onFinish(response);
            }
        });
    }

    @Override
    public void onRequestRemoveTableFromOrder(final String tableID, final GetCallback<TableResponse> callback) {
        getToken();
        tableRequestManager.removeTableFromOrder(token, tableID, order.getId(), new GetCallback<TableResponse>() {
            @Override
            public void onFinish(TableResponse response) {
                // remove table ra khỏi order
                if (response.getSuccess()) {
                    order.getTables().remove(tableID);
                    tableDataListener.onDeleteTable(response.getTable().getId());
                }
                callback.onFinish(response);
            }
        });
    }

    @Override
    public RegionTableRequestManager getRegionTableRequestManager() {
        return tableRequestManager;
    }

    @Override
    public void onRequestSelectFood(Food food, int newCount) {

    }

    @Override
    public RegionTableSocketService getRegionTableSocketService() {
        return tableListener.getSocketService();
    }

    //TODO
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

    /*
    * End IOrderVM
    * */
}
