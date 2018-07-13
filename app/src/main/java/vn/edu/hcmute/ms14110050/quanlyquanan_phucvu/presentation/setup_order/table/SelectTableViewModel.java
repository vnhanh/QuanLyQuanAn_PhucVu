package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.OnSpinnerStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.RegionTableSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.Region;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.table.TableRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ISpinnerDataListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IRecyclerAdapter;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public class SelectTableViewModel
        extends BaseNetworkViewModel<ITableView>
        implements OnSpinnerStateListener.DataProcessor, ITableVM {

    private TableRequestManager requestManager;
    private RegionTableSocketService socketService;

    private String selectedRegionID;

    protected String token;

    private ISpinnerDataListener<Region> regionDataListener;
    private IRecyclerAdapter<Table> tableDataListener;

    private IOrderVM centerVM;

    public final ObservableBoolean isLoadingTables = new ObservableBoolean(false);

    /*
    * Properties
    * */

    public void setCenterViewModel(IOrderVM centerViewModel) {
        this.centerVM = centerViewModel;
        requestManager = centerVM.getRegionTableRequestManager();
        socketService = centerVM.getRegionTableSocketService();
    }

    @Override
    public String getOrderID() {
        return centerVM.getOrderID();
    }

    public void setRegionDataListener(ISpinnerDataListener<Region> regionDataListener) {
        this.regionDataListener = regionDataListener;
    }

    public void setTableDataListener(IRecyclerAdapter tableDataListener) {
        this.tableDataListener = tableDataListener;
    }

    private void showLoadingTables() {
        isLoadingTables.set(true);
    }

    private void hideLoadingTables() {
        isLoadingTables.set(false);
    }

    /*
    * End Properties
    * */

    /*
    * BaseNetworkViewModel
    * */

    @Override
    public void onViewAttach(@NonNull ITableView viewCallback) {
        super.onViewAttach(viewCallback);
        Log.d("LOG", getClass().getSimpleName() + ":onViewAttach()");

        onSetupServerConnection();
        loadRegions();
    }

    @Override
    public void onViewDetached() {
        requestManager = null;
        socketService = null;
        token = null;
        super.onViewDetached();
    }

    private void onSetupServerConnection() {
        listenRegionValue();
        listenTableValue();
    }

    /*
    * End BaseNetworkViewModel
    * */

    private void loadRegions() {
        if (isViewAttached()) {
            getView().onShowLoadRegionsProgress();
        }
        if (StringUtils.isEmpty(token)) {
            token = SSharedReference.getToken(getView().getContext());
        }
        requestManager.loadRegions(token, new GetCallback<ArrayList<Region>>() {
            @Override
            public void onFinish(ArrayList<Region> _regions) {
                onGetRegions(_regions);
            }
        });
    }

    private void onGetRegions(ArrayList<Region> _regions) {
        if (isViewAttached()) {
            regionDataListener.onGetList(_regions);
            getView().onHideLoadRegionsProgress();
        }
    }

    private void listenRegionValue() {
        socketService.onEventRegionAdded(new GetCallback<Region>() {
            @Override
            public void onFinish(Region region) {
                onAddRegion(region);
            }
        });

        socketService.onEventRegionUpdate(new GetCallback<Region>() {
            @Override
            public void onFinish(Region region) {
                onUpdateRegion(region);
            }
        });

        socketService.onEventRegionDelete(new GetCallback<String>() {
            @Override
            public void onFinish(String regionID) {
                onDeleteRegion(regionID);
            }
        });
    }

    private boolean isInvalidRegion(Region region) {
        return region == null || StringUtils.isEmpty(region.getId());
    }

    private void onAddRegion(Region region) {
        if (isInvalidRegion(region)) {
            return;
        }
        regionDataListener.onAddItem(region);
    }

    private void onUpdateRegion(Region region) {
        if (isInvalidRegion(region)) {
            return;
        }
        regionDataListener.onUpdateItem(region);
    }

    private void onDeleteRegion(String regionID) {
        if (StringUtils.isEmpty(regionID)) {
            return;
        }
        regionDataListener.onDeleteItem(regionID);
    }

    private void listenTableValue() {
        socketService.listenAddTableEvent(new GetCallback<Table>() {
            @Override
            public void onFinish(Table table) {
                onAddTable(table);
            }
        });
        socketService.listeneAddTableToOrder(new GetCallback<Table>() {
            @Override
            public void onFinish(Table table) {
                if (table != null && table.getOrderID().equals(getOrderID())) {
                    Log.d("LOG", SelectTableViewModel.class.getSimpleName()
                            + ":onAddTableToOrder:table id:" + table.getId()
                            + ":is current orderID");
                    return;
                }
                Log.d("LOG", SelectTableViewModel.class.getSimpleName()
                        + ":onAddTableToOrder:table id:" + table.getId()
                        + ":order id:"+table.getOrderID());
                onAddTableToOrder(table);
            }
        });
        socketService.listenRemoveOrderTable(new GetCallback<Table>() {
            @Override
            public void onFinish(Table table) {
                if (table != null && table.getOrderID().equals(getOrderID())) {
                    Log.d("LOG", SelectTableViewModel.class.getSimpleName()
                            + ":onRemoveOrderTable:table id:" + table.getId()
                            + ":is current orderID");
                    return;
                }
                onRemoveOrderTable(table);
            }
        });
        socketService.listenUpdateActivedTableEvent(new GetCallback<Table>() {
            @Override
            public void onFinish(Table table) {
                onUpdateActivedTable(table);
            }
        });
        socketService.listenDeleteTable(new GetCallback<String>() {
            @Override
            public void onFinish(String tableID) {
                onRemoveTable(tableID);
            }
        });
    }

    /**
     * Khi có table được add vào CSDL
     * Chỉ cho phép add table này vào UI nếu khác NULL, các giá trị thuộc tính hợp lệ
     * và thuộc khu vực đang chọn
     */
    private void onAddTable(Table table) {
        if (isViewAttached() && table != null && selectedRegionID != null) {
            if (selectedRegionID.equals(table.getRegionID())){
                tableDataListener.onAddItem(table);
            }
        }
    }

    /**
     * Khi có table được update trên CSDL
     * chỉ khi add table vào order (chính user đang dùng hoặc user khác) mới kích hoạt event này
     * Chỉ update UI khi các dữ liệu cần thiết khác NULL, khác rỗng và table này thuộc khu vực đang chọn
     * @param table
     */
    private void onAddTableToOrder(Table table) {
        if (isViewAttached() && table != null) {
            if (selectedRegionID.equals(table.getRegionID())){
                if (getOrderID().equals(table.getOrderID())) {
                    tableDataListener.onUpdateItem(table);
                }else{
                    tableDataListener.onRemoveItem(table.getId());
                }
            }
        }
    }

    /**
     * Khi có table được remove ra khỏi order nào đó
     * Chỉ update UI khi dữ liệu hợp lệ và table này nằm trong khu vực đang chọn
     * @param table
     */
    private void onRemoveOrderTable(Table table) {
        if (isViewAttached() && table != null) {
            if (selectedRegionID.equals(table.getRegionID())){
                // update nếu tìm thấy item
                // add nếu không tìm thấy
                tableDataListener.onUpdateOrAddItem(table);
            }
        }
    }

    /**
     * Khi có table được update trường actived trên CSDL
     * Chỉ update UI khi dữ liệu hợp lệ và table này nằm trong khu vực đang chọn
     */
    private void onUpdateActivedTable(Table table) {
        if (isViewAttached() && table != null) {
            if (selectedRegionID.equals(table.getRegionID())){
                if (table.isActived()) {
                    tableDataListener.onAddItem(table);
                }else{
                    tableDataListener.onRemoveItem(table.getId());
                }
            }
        }
    }

    // remove table ra khỏi recyclerview nếu nó tồn tại
    private void onRemoveTable(String tableID) {
        if (isViewAttached() && !StringUtils.isEmpty(tableID)) {
            tableDataListener.onRemoveItem(tableID);
        }
    }

    /*
    * OnSpinnerStateListener.DataProcessor
    * */
    // Khi item của region spinner được bấm
    @Override
    public void onSelectSpinnerItemId(String regionID) {
        selectedRegionID = regionID;
        if (StringUtils.isEmpty(regionID)) {
            tableDataListener.onGetList(new ArrayList<Table>());
            return;
        }

        showLoadingTables();

        getToken();
        requestManager.loadTablesByRegionID(token, regionID, new GetCallback<ArrayList<Table>>() {
            @Override
            public void onFinish(ArrayList<Table> tables) {
                onGetTables(tables);
            }
        });
    }

    private void onGetTables(ArrayList<Table> _tables) {
        if (_tables != null) {
            ArrayList<Table> validTables = copyEmptyTables(_tables);
            tableDataListener.onGetList(validTables);
            hideLoadingTables();
        }
    }

    private ArrayList<Table> copyEmptyTables(ArrayList<Table> _tables) {
        ArrayList<Table> valids = new ArrayList<>();
        for (Table _table : _tables) {
            if (_table.isActived() && (StringUtils.isEmpty(_table.getOrderID()) || getOrderID().equals(_table.getOrderID()))) {
                valids.add(_table);
            }
        }
        return valids;
    }

    /*
    * ITableVM
    * */

    @Override
    public boolean isCreatedOrder() {
        if (centerVM == null) {
            Log.d("LOG", getClass().getSimpleName() + ":isCreatedOrder():centerVM is null");
        }
        return centerVM != null && centerVM.isCreatedOrder();
    }

    @Override
    public void onRequestAddTableToOrder(final Table table, final GetCallback<TableResponse> callback) {
        centerVM.onRequestAddTableToOrder(table, new GetCallback<TableResponse>() {
            @Override
            public void onFinish(TableResponse response) {
                if (response.isSuccess()) {
                    Log.d("LOG", SelectTableViewModel.class.getSimpleName()
                            + ":onRequestAddTableToOrder:success");
                    onAddTableToOrder(response.getTable());
                    callback.onFinish(response);
                }else{
                    Log.d("LOG", SelectTableViewModel.class.getSimpleName()
                            + ":onRequestAddTableToOrder:failed:"+response.getMessage());
                    if (!StringUtils.isEmpty(response.getMessage())) {
                        response.setMessage(getString(R.string.add_table_to_order_failed));
                    }
                    callback.onFinish(response);
                }
            }
        });
    }

    @Override
    public void onRequestRemoveTableFromOrder(final String tableID, final GetCallback<TableResponse> callback) {
        centerVM.onRequestRemoveTableFromOrder(tableID, new GetCallback<TableResponse>() {
            @Override
            public void onFinish(TableResponse response) {
                if (response.isSuccess()) {
                    Log.d("LOG", getClass().getSimpleName() + ":onRequestRemoveTableFromOrder:success");
                    onRemoveOrderTable(response.getTable());
                    callback.onFinish(response);
                }else{
                    Log.d("LOG", getClass().getSimpleName() + ":onRequestRemoveTableFromOrder:failed:"+response.getMessage());
                    if (!StringUtils.isEmpty(response.getMessage())) {
                        response.setMessage(getString(R.string.remove_table_from_order_failed));
                    }
                    callback.onFinish(response);
                }
            }
        });
    }

    @Override
    public TableRequestManager getRegionTableRequestManager() {
        return requestManager;
    }

    @Override
    public RegionTableSocketService getRegionTableSocketService() {
        return socketService;
    }

    @Override
    public String getToken() {
        if (StringUtils.isEmpty(token)) {
            token = centerVM.getToken();
        }
        return token;
    }

    /*
    * End ITableVM
    * */

}
