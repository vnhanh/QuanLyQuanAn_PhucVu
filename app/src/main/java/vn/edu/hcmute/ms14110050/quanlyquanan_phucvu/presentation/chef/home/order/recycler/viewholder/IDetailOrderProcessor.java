package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;

public interface IDetailOrderProcessor {
    void requestUpdateStatusDetailOrder(DetailOrder detailOrder, int newStatus);
}
