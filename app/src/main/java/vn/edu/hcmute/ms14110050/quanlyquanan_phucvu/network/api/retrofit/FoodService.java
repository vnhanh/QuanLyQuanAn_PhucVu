package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFoodsResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodsResponse;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface FoodService {

    @GET("categoryFood/allCategoryFoods")
    Observable<CategoryFoodsResponse> getAllCategoryFoods(@Header("Authorization") String token);

    @GET("categoryFood/allFoods/{categoryID}")
    Observable<FoodsResponse> getFoodsByCategory(@Header("Authorization") String token, @Path("category_id") String categoryID);
}
