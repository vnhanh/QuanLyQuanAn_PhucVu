package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order;

import java.util.WeakHashMap;

public class OrderParamsMaker {
    public static WeakHashMap<String, Object> createUpdateStatus(String orderID, int orderFlag) {
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("id", orderID);
        map.put("flag_status", orderFlag);
        return map;
    }
}
