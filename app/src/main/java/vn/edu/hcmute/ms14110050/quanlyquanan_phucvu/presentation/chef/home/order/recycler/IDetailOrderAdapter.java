package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IRecyclerAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;

public interface IDetailOrderAdapter extends IRecyclerAdapter<DetailOrder> {
    void onRemoveDetails(String orderID);
}
