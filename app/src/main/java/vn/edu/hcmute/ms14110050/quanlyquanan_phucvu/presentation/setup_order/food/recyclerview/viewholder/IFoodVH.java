package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview.viewholder;

import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IProgressVH;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;

/**
 * Created by Vo Ngoc Hanh on 6/27/2018.
 */

public interface IFoodVH extends IProgressVH{

    void onStartViewFoodActivity(Context context, String orderID, DetailOrder detailOrder, Food food);
}
