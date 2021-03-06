package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.FullOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrderFoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrdersResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.PayableOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateDetailOrderStatusResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;

/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public interface OrderService {

    @GET("order/loadOrder/{id}")
    Observable<FullOrderResponse> loadOrder(@Header("Authorization") String token, @Path("id") String orderID);

    @GET("order/getOrder/{id}")
    Observable<OrderResponse> getOrder(@Header("Authorization") String token, @Path("id") String orderID);

    @PUT("foods/orderFood")
    @FormUrlEncoded
    Observable<OrderFoodResponse> updateFoodForOrder(@Header("Authorization") String token, @FieldMap Map<String,Object> fields);

    @PUT("order/updateOrCreateDetailOrder")
    @FormUrlEncoded
    Observable<OrderResponse> updateOrCreateDetailOrder(@Header("Authorization") String token, @FieldMap Map<String,Object> fields);

    @POST("order/makeOrder")
    @FormUrlEncoded
    Observable<UpdateStatusOrderResponse> makeOrder(@Header("Authorization") String token, @Field("id") String orderID);

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
    Observable<OrderResponse> updateNumberCustomer(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/updateDescription")
    @FormUrlEncoded
    Observable<OrderResponse> updateDescription(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/updateStatusOrder")
    @FormUrlEncoded
    Observable<OrderResponse> updateStatusOrder(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/updateStatusOrder")
    @FormUrlEncoded
    Observable<PayableOrderResponse> setPayableOrder(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/removeOrder")
    @FormUrlEncoded
    Observable<ResponseValue> removeOrder(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @GET("order/getOrdersForWaiter")
    Observable<OrdersResponse> getOrdersWaiting(@Header("Authorization") String token);

    @PUT("order/suggestDelegacy")
    @FormUrlEncoded
    Observable<ResponseValue> suggestDelegacy(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/agreeBecomeDelegacy")
    @FormUrlEncoded
    Observable<ResponseValue> agreeBecomeDelegacy(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/disagreeBecomeDelegacy")
    @FormUrlEncoded
    Observable<ResponseValue> disagreeBecomeDelegacy(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/updateStatusDetailOrder")
    @FormUrlEncoded
    Observable<UpdateDetailOrderStatusResponse> updateDetailOrderStatus(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/removeStatusDetailOrder")
    @FormUrlEncoded
    Observable<OrderResponse> removeStatusDetailOrder(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

    @PUT("order/reProcessOrder")
    @FormUrlEncoded
    Observable<ResponseValue> reProcessOrder(@Header("Authorization") String token, @FieldMap Map<String, Object> fields);

}
