package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.RegionTableSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.table.TableRequestManager;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface ITableVM {
    boolean isCreatedOrder();

    void onRequestAddTableToOrder(Table table, GetCallback<TableResponse> callback);

    void onRequestRemoveTableFromOrder(String tableID, GetCallback<TableResponse> callback);

    TableRequestManager getRegionTableRequestManager();

    RegionTableSocketService getRegionTableSocketService();

    String getOrderID();

    String getToken();
}
