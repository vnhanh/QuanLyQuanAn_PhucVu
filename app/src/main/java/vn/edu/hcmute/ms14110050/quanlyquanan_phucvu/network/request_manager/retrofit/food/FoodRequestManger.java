package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.ApiUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.FoodService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFood;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFoodsResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodsResponse;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class FoodRequestManger {
    private FoodService service;

    public FoodRequestManger() {
        createService();
    }

    private void createService() {
        if (service == null) {
            service = ApiUtil.getFoodService();
        }
    }

    public void loadCategories(@NonNull String token, final GetCallback<ArrayList<CategoryFood>> callback) {
        service.getAllCategoryFoods(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryFoodsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CategoryFoodsResponse response) {
                        if (response.getSuccess()) {
                            callback.onFinish(response.getCategoryfoods());
                        }else{
                            Log.d("LOG", FoodRequestManger.class.getSimpleName()
                                    + ":loadCategories():failed:" + response.getMessage());
                            callback.onFinish(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", FoodRequestManger.class.getSimpleName()
                                + ":loadCategories():error:" + e.getMessage());
                        callback.onFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadFoodsByCatID(String token, String selectedID, final GetCallback<ArrayList<Food>> callback) {
        service.getFoodsByCategory(token, selectedID)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FoodsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FoodsResponse response) {
                        if (response.getSuccess()) {
                            callback.onFinish(response.getFoods());
                        }else{
                            Log.d("LOG", FoodRequestManger.class.getSimpleName()
                                    + ":loadFoodsByCatID():failed:" + response.getMessage());
                            callback.onFinish(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", FoodRequestManger.class.getSimpleName()
                                + ":loadFoodsByCatID():error:" + e.getMessage());
                        callback.onFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
