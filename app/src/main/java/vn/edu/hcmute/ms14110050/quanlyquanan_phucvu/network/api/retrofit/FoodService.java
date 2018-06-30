package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFoodsResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodsResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface FoodService {

    @GET("categoryFood/allCategoryFoods")
    Observable<CategoryFoodsResponse> getAllCategoryFoods(@Header("Authorization") String token);

    @GET("foods/allFoods/{categoryID}")
    Observable<FoodsResponse> getFoodsByCategory(@Header("Authorization") String token, @Path("categoryID") String categoryID);

    @GET("foods/food/{foodID}")
    Observable<FoodResponse> getFoodByID(@Header("Authorization") String token, @Path("foodID") String foodID);
}
