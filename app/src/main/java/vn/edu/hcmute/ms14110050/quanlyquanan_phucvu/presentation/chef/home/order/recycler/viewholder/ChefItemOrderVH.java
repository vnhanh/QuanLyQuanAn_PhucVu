package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ChefItemRecyclerOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;


public class ChefItemOrderVH extends BaseViewHolder<ChefItemRecyclerOrderBinding,ChefItemOrderVM> implements IItemOrderView{

    public ChefItemOrderVH(View view) {
        super(view);
        TextView textView = view.findViewById(R.id.txt_message);
        textView.setText(R.string.no_order);
    }

    public ChefItemOrderVH(ChefItemRecyclerOrderBinding binding, View.OnClickListener onClickItemListener) {
        super(binding);
        binding.getRoot().setOnClickListener(onClickItemListener);
    }

    @Override
    protected ChefItemOrderVM initViewModel() {
        return new ChefItemOrderVM();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public void onBind(Order order) {
        if (viewmodel != null) {
            viewmodel.setOrder(order);

            View rootView = binding.getRoot();
            rootView.setTag(binding.getRoot().getId(), viewmodel.getOrder().getId());
        }
    }
}
