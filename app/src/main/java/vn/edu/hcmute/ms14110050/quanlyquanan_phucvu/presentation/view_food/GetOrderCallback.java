package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderResponse;

/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public class GetOrderCallback implements GetCallback<OrderResponse> {
    private WeakReference<ViewFoodModel> viewmodelRef;

    public GetOrderCallback(ViewFoodModel viewFoodModel) {
        viewmodelRef = new WeakReference<ViewFoodModel>(viewFoodModel);
    }

    @Override
    public void onFinish(OrderResponse response) {
        ViewFoodModel viewmode = viewmodelRef.get();
        if (viewmode != null) {
            viewmode.onUpdateOrderResponse(response);
        }
    }
}