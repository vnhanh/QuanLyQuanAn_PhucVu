package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseProgressVH;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ItemRecyclerFoodBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.IFoodVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food.ViewFoodActivity;

/**
 * Created by Vo Ngoc Hanh on 6/26/2018.
 */

public class FoodVH extends BaseProgressVH<ItemRecyclerFoodBinding, FoodVHViewModel> implements IFoodVH {

    public FoodVH(View view) {
        super(view);
    }

    public FoodVH(ItemRecyclerFoodBinding binding, @NonNull IFoodVM containerViewModel) {
        super(binding);
        viewmodel.setContainerViewModel(containerViewModel);
    }

    public void onBind(Food food) {
        Animation alphaAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.getRoot().startAnimation(alphaAnim);
        viewmodel.setFood(food);
    }

    @Override
    protected FoodVHViewModel initViewModel() {
        return new FoodVHViewModel();
    }

    @Override
    protected void attachToViewModel() {
        viewmodel.attachView(this);
    }

    @Override
    public Context getContext() {
        return binding.getRoot().getContext();
    }

    /*
    * IFoodVH
    * */

    @Override
    public void onStartViewFoodActivity(Context context, String orderID, DetailOrder detailOrder, Food food) {
        if (context instanceof Activity) {
            ViewFoodActivity.startActivity((Activity) context, orderID, detailOrder, food);
        }
    }
}
