package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.detail_order;

import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;

public interface IDetailOrderVH extends IViewHolder {
    void onStartViewFoodActivity(Context context, String orderID, DetailOrder detailOrder, String foodId);
}
