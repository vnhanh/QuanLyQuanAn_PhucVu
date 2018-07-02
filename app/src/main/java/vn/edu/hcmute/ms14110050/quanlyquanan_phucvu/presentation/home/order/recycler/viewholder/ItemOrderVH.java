package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.recycler.viewholder;

import android.content.Context;
import android.view.View;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ItemRecyclerOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;

public class ItemOrderVH extends BaseViewHolder<ItemRecyclerOrderBinding,ItemOrderVM> implements IItemOrderView{

    public ItemOrderVH(ItemRecyclerOrderBinding binding, View.OnClickListener onClickItemListener) {
        super(binding);
        binding.getRoot().setOnClickListener(onClickItemListener);
    }

    @Override
    protected ItemOrderVM initViewModel() {
        return new ItemOrderVM();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public void onBind(Order order) {
        if (viewmodel != null) {
            viewmodel.setOrder(order);
            binding.getRoot().setTag(binding.getRoot().getId(), viewmodel.getOrder().getId());
        }
    }
}
