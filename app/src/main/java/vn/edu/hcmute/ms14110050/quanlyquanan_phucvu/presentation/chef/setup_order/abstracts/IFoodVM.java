package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts;

import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;


/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface IFoodVM {

    String getOrderID();

    Context getContext();
}
