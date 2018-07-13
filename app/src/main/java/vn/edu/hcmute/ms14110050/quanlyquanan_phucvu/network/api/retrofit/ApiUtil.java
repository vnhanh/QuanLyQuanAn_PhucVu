package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.BASE_URL;

/**
 * Created by Vo Ngoc Hanh on 5/14/2018.
 */

public class ApiUtil {

    public static AuthenticationService getAuthService() {
        return RetrofitClient.getClient(BASE_URL).create(AuthenticationService.class);
    }

    public static TableService getRegionTableService() {
        return RetrofitClient.getClient(BASE_URL).create(TableService.class);
    }

    public static FoodService getFoodService() {
        return RetrofitClient.getClient(BASE_URL).create(FoodService.class);
    }

    public static OrderService getOrderService() {
        return RetrofitClient.getClient(BASE_URL).create(OrderService.class);
    }
}
