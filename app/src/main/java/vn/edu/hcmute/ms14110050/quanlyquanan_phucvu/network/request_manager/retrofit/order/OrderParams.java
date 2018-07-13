package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order;

import java.util.ArrayList;
import java.util.WeakHashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;

public class OrderParams {
    public static WeakHashMap<String, Object> createUpdateStatus(String orderID, int orderFlag) {
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("id", orderID);
        map.put("flag_status", orderFlag);
        return map;
    }

    public static WeakHashMap<String, Object> createSuggestDelegacy(String username, String delegacyUserName) {
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("handover_username", username);
        map.put("delegacy_username", delegacyUserName);

        return map;
    }

    public static WeakHashMap<String, Object> createAgreeDelegacy(String username, String handoverUserName) {
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("username", username);
        map.put("handover_username", handoverUserName);

        return map;
    }

    public static WeakHashMap<String, Object> createDiagreeDelegacy(String username, String handoverUserName) {
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("username", username);
        map.put("handover_username", handoverUserName);

        return map;
    }

    public static WeakHashMap<String, Object> createRequestCreateOrder(Order order) {
        WeakHashMap<String, Object> map = new WeakHashMap<>();

        map.put("waiter_username", order.getWaiterUsername());
        map.put("waiter_fullname", order.getWaiterFullname());
        map.put("flag_status", order.getStatusFlag());
        map.put("flag_set_table", order.getStatusFlag());
        map.put("number_customer", order.getCustomerNumber());

        return map;
    }

    public static WeakHashMap<String, Object> createOrderFood(String id, int oldCount, int newCount, int inventory) {
        WeakHashMap<String, Object> fields = new WeakHashMap<>();
        fields.put("foodID", id);
        fields.put("oldCount", oldCount);
        fields.put("newCount", newCount);
        fields.put("inventory", inventory);

        return fields;
    }
}
