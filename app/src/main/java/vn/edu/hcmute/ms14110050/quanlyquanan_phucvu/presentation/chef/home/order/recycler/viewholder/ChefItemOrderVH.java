package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ChefItemRecyclerDetailOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;


public class ChefItemOrderVH extends BaseViewHolder<ChefItemRecyclerDetailOrderBinding,ChefItemOrderVM> implements IItemOrderView{
    private WeakReference<Activity> weakActivity;

    public ChefItemOrderVH(View view) {
        super(view);
        TextView textView = view.findViewById(R.id.txt_message);
        textView.setText(R.string.no_order);
    }

    public ChefItemOrderVH(WeakReference<Activity> weakActivity,
                           ChefItemRecyclerDetailOrderBinding binding,
                           View.OnClickListener onClickItemListener,
                           IDetailOrderProcessor processor) {

        super(binding);
        this.weakActivity = weakActivity;
        binding.getRoot().setOnClickListener(onClickItemListener);
        viewmodel.setDetailOrderProcessor(processor);
    }

    @Override
    protected ChefItemOrderVM initViewModel() {
        return new ChefItemOrderVM();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public void onBind(DetailOrder item, int position) {
        if (viewmodel != null) {
            viewmodel.setData(item, position);

            View rootView = binding.getRoot();
            rootView.setTag(binding.getRoot().getId(), viewmodel.getDetailOrder().getOrderID());
        }
    }

    @Override
    public void onClick(String orderID) {
//        Activity activity = weakActivity.get();
//        if (activity != null) {
//            ChefSetupOrderActivity.startActivity(activity, orderID);
//        }
    }
}
