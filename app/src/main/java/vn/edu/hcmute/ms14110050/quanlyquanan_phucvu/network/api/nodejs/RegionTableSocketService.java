package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs;

import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.BaseSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.Region;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_ADD_REGION;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_ADD_TABLE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_DELETE_REGION;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_DELTE_TABLE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_REMOVE_ORDER_TABLE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_REGION;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_ACTIVED_TABLE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_TABLE;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class RegionTableSocketService extends BaseSocketService{

    public void onEventRegionAdded(final GetCallback<Region> callback) {
        listenEvent(SOCKET_EVENT_ADD_REGION, callback, "region", new TypeToken<Region>(){}.getType());
    }

    public void onEventRegionUpdate(final GetCallback<Region> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_REGION, callback, "region", new TypeToken<Region>(){}.getType());
    }

    public void onEventRegionDelete(final GetCallback<String> callback) {
        listenEvent(SOCKET_EVENT_DELETE_REGION, callback, "id", null);
    }

    public void listenAddTableEvent(GetCallback<Table> callback) {
        listenEvent(SOCKET_EVENT_ADD_TABLE, callback, "table", new TypeToken<Table>(){}.getType());
    }

    public void listenUpdateTableEvent(GetCallback<Table> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_TABLE, callback, "table", new TypeToken<Table>(){}.getType());
    }

    public void listenUpdateActivedTableEvent(GetCallback<Table> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_ACTIVED_TABLE, callback, "table", new TypeToken<Table>(){}.getType());
    }

    public void listenRemoveOrderTable(GetCallback<Table> callback) {
        listenEvent(SOCKET_EVENT_REMOVE_ORDER_TABLE, callback, "table", new TypeToken<Table>(){}.getType());
    }

    public void listenDeleteTable(GetCallback<String> callback) {
        listenEvent(SOCKET_EVENT_DELTE_TABLE, callback, "id", null);
    }
}
