package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodResponse;


/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public class GetFoodCallback implements GetCallback<FoodResponse> {
    private WeakReference<ViewFoodModel> viewmodelRef;

    public GetFoodCallback(ViewFoodModel viewFoodModel) {
        viewmodelRef = new WeakReference<ViewFoodModel>(viewFoodModel);
    }

    @Override
    public void onFinish(FoodResponse response) {
        ViewFoodModel viewmodel = viewmodelRef.get();
        if (viewmodel != null) {
            viewmodel.onGetFoodResponse(response);
        }
    }
}