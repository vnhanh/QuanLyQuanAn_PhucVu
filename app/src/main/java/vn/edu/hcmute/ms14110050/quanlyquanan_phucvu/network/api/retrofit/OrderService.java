package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;

/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public interface OrderService {

    @GET("order/getOrder/{id}")
    Observable<OrderResponse> getOrder(@Header("Authorization") String token, @Path("id") String orderID);

    @PUT("foods/orderFood")
    @FormUrlEncoded
    Observable<FoodResponse> updateFoodForOrder(@Header("Authorization") String token, @FieldMap Map<String,Object> fields);

    @PUT("order/updateOrCreateDetailOrder")
    @FormUrlEncoded
    Observable<OrderResponse> updateOrCreateDetailOrder(@Header("Authorization") String token, @FieldMap Map<String,Object> fields);

    @POST("order/createOrder")
    @FormUrlEncoded
    Observable<OrderResponse> createOrder(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/orderTable")
    @FormUrlEncoded
    Observable<TableResponse> orderTable(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/removeOrderTable")
    @FormUrlEncoded
    Observable<TableResponse> removeTableFromOrder(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/updateNumberCustomer")
    @FormUrlEncoded
    Observable<ResponseValue> updateNumberCustomer(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);
}