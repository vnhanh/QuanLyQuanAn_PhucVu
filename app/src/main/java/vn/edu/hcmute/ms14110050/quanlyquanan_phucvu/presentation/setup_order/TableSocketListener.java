package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import android.support.annotation.NonNull;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.RegionTableSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ITableDataListener;

/**
 * Created by Vo Ngoc Hanh on 6/23/2018.
 */

public class TableSocketListener {
    private ITableDataListener view;
    private String orderID;

    private RegionTableSocketService socketService;

    public RegionTableSocketService getSocketService() {
        return socketService;
    }

    public TableSocketListener() {
        createSocketService();
    }

    private void createSocketService() {
        if (socketService == null) {
            socketService = new RegionTableSocketService();
        }
    }

    public void listenSockets(@NonNull ITableDataListener dataListener, @NonNull String orderID) {
        this.view = dataListener;
        this.orderID = orderID;
        socketService.listenAddTableEvent(new GetCallback<Table>() {
            @Override
            public void onFinish(Table table) {
                onAddTable(table);
            }
        });
        socketService.listenUpdateTableEvent(new GetCallback<Table>() {
            @Override
            public void onFinish(Table table) {
                onAddTableToOrder(table);
            }
        });
        socketService.listenUpdateActivedTableEvent(new GetCallback<Table>() {
            @Override
            public void onFinish(Table table) {
                onUpdateActivedTable(table);
            }
        });
        socketService.listenRemoveOrderTable(new GetCallback<Table>() {
            @Override
            public void onFinish(Table table) {
                onRemoveOrderTable(table);
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
     * và có orderID thuộc order hiện tại
     */
    private void onAddTable(Table table) {
        if (view != null && table != null) {
            if (orderID.equals(table.getOrderID())){
                view.onAddTable(table);
            }
        }
    }

    /**
     * Khi có table được update trên CSDL
     * chỉ khi add table vào order (chính user đang dùng hoặc user khác) mới kích hoạt event này
     * Chỉ update UI khi các dữ liệu cần thiết khác NULL, khác rỗng và table này thuộc order hiện tại
     */
    public void onAddTableToOrder(Table table) {
        if (view != null && table != null) {
            if (orderID.equals(table.getOrderID())){
                view.onUpdateTable(table);
            }else{
                view.onDeleteTable(table.getId());
            }
        }
    }

    /**
     * Khi có table được update trường actived trên CSDL
     * Chỉ update UI khi dữ liệu hợp lệ và table này nằm trong khu vực đang chọn
     */
    private void onUpdateActivedTable(Table table) {
        if (view != null && table != null) {
            if (orderID.equals(table.getOrderID())){
                if (table.isActived()) {
                    view.onAddTable(table);
                }else{
                    view.onDeleteTable(table.getId());
                }
            }
        }
    }

    /**
     * Khi có table được remove ra khỏi order nào đó
     * Chỉ update UI khi dữ liệu hợp lệ và table này nằm trong khu vực đang chọn
     */
    public void onRemoveOrderTable(Table table) {
        if (view != null && table != null) {
            view.onDeleteTable(table.getId());
        }
    }

    // remove table ra khỏi recyclerview nếu nó tồn tại
    private void onRemoveTable(String tableID) {
        if (view != null && tableID != null) {
            view.onDeleteTable(tableID);
        }
    }
}
