package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import android.support.annotation.NonNull;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.RegionTableSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IListAdapterListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;

/**
 * Created by Vo Ngoc Hanh on 6/23/2018.
 */

public class TableSocketListener {
    private IListAdapterListener<Table> tableDataListener;
    private IOrderVM centerVM;

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

    public void destroy() {
        centerVM = null;
        socketService.destroy();
    }

    private Order getOrder() {
        return centerVM.getOrder();
    }

    public void listenSockets(@NonNull IListAdapterListener<Table> dataListener, @NonNull IOrderVM centerVM) {
        this.centerVM = centerVM;
        this.tableDataListener = dataListener;

        socketService.listeneAddTableToOrder(new GetCallback<Table>() {
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
     * Khi có table được add vào order trên CSDL (chính user đang dùng hoặc user khác) mới kích hoạt event này
     * Chỉ update UI khi các dữ liệu cần thiết khác NULL, khác rỗng và table này thuộc order hiện tại
     * @param table
     */
    public void onAddTableToOrder(Table table) {
        Order order = getOrder();
        if (order != null && tableDataListener != null && table != null) {
            if (order.getId().equals(table.getOrderID())) {
                tableDataListener.onAddItem(table);
                order.getTables().add(table.getId());
            } else {
                if (tableDataListener.onRemoveItem(table.getId())) {
                    order.getTables().remove(table.getId());
                }
            }
        }
    }

    /**
     * Khi có table được remove ra khỏi order nào đó
     * Chỉ update UI khi dữ liệu hợp lệ và table này nằm trong khu vực đang chọn
     * @param table
     */
    public void onRemoveOrderTable(Table table) {
        if (tableDataListener != null && table != null) {
            if (tableDataListener.onRemoveItem(table.getId())) {
                Order order = getOrder();
                order.getTables().remove(table.getId());
            }
        }
    }

    /**
     * Khi có table được update trường actived trên CSDL
     * Chỉ update UI khi dữ liệu hợp lệ và table này nằm trong khu vực đang chọn
     */
    private void onUpdateActivedTable(Table table) {
        if (tableDataListener != null && table != null) {
            Order order = getOrder();
            if (order.getId().equals(table.getOrderID())){
                if (!table.isActived()) {
                    tableDataListener.onRemoveItem(table.getId());
                    order.getTables().remove(table.getId());
                }
            }
        }
    }

    // remove table ra khỏi recyclerview nếu nó tồn tại
    private void onRemoveTable(String tableID) {
        if (tableDataListener != null && tableID != null) {
            if (tableDataListener.onRemoveItem(tableID)) {
                Order order = getOrder();
                order.getTables().remove(tableID);
            }
        }
    }
}
