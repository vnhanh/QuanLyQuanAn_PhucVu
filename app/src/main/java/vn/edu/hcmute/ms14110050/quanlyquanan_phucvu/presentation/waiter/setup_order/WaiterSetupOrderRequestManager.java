package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.network.BaseRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestApi;

public class WaiterSetupOrderRequestManager extends BaseRequestManager {
    private OrderRequestApi orderApi;

    public WaiterSetupOrderRequestManager() {
        orderApi = new OrderRequestApi();
    }
}
