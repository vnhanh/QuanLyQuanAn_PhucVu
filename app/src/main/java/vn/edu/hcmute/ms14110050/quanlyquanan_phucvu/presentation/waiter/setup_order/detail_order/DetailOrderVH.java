package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.detail_order;

import android.view.View;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.WaiterItemRecyclerDetailOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.IOrderVM;

public class DetailOrderVH extends BaseViewHolder<WaiterItemRecyclerDetailOrderBinding, DetailOrderVM>
        implements IDetailOrderVH {

    public DetailOrderVH(View view) {
        super(view);
    }

    public DetailOrderVH(WaiterItemRecyclerDetailOrderBinding binding, IOrderVM centerVM) {
        super(binding);
        viewmodel.setCenterVM(centerVM);
    }

    @Override
    protected DetailOrderVM initViewModel() {
        return new DetailOrderVM();
    }

    public void onBind(DetailOrder detailOrder, int position) {
        if (viewmodel != null) {
            viewmodel.setData(detailOrder, position);
        }
    }
}
