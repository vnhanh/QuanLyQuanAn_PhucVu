package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.recycler.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.animator.AlphaAnimator;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.WaiterItemRecyclerOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.IOnCheckOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderCheckable;

public class ItemOrderVH extends BaseViewHolder<WaiterItemRecyclerOrderBinding,ItemOrderVM> implements IItemOrderView, View.OnClickListener {
    private WeakReference<View.OnClickListener> weakListener;
    private WeakReference<IOnCheckOrder> weakOnCheckOrderListener;
    private AlphaAnimator animator;

    public ItemOrderVH(View view) {
        super(view);
        TextView textView = view.findViewById(R.id.txt_message);
        textView.setText(R.string.no_order);
    }

    public ItemOrderVH(WaiterItemRecyclerOrderBinding binding,
                       View.OnClickListener onClickItemListener,
                       IOnCheckOrder onCheckItemListener, AlphaAnimator animator) {
        super(binding);
        weakListener = new WeakReference<>(onClickItemListener);
        weakOnCheckOrderListener = new WeakReference<>(onCheckItemListener);
        this.animator = animator;
        binding.getRoot().setOnClickListener(this);
    }

    @Override
    protected ItemOrderVM initViewModel() {
        return new ItemOrderVM();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public void onBind(OrderCheckable item, boolean isCreater) {
        if (viewmodel != null) {
            final View view = binding.getRoot();
            view.setTag(view.getId(), item.getOrder().getId());

            viewmodel.setData(item, isCreater);
            binding.getRoot().setTag(binding.getRoot().getId(), viewmodel.getOrder().getId());

//            Log.d("LOG", getClass().getSimpleName() + ":onBind():isCreater:" + isCreater
//                    + ":is checked:" + item.isCheck());

            if (isCreater && !item.isCheck()) {
                animator.addView(view);
            }else{
//                Log.d("LOG", getClass().getSimpleName() + ":onBind():khong phai chinh chu");
                view.setAlpha(1f);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (viewmodel != null) {
            viewmodel.getData().setCheck(true);
        }

        IOnCheckOrder onCheckOrderListener = weakOnCheckOrderListener.get();
        if (onCheckOrderListener != null) {
            onCheckOrderListener.onCheckOrder(viewmodel.getOrder().getId());
        }

        View.OnClickListener listener = weakListener.get();
        if (listener != null) {
            listener.onClick(v);
        }
    }
}
