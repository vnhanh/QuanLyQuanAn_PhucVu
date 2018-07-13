package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ChefItemRecyclerOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder.ChefItemOrderVH;


public class ChefItemOrderAdapter extends BaseAdapter<ChefItemOrderVH, Order> {
    private View.OnClickListener onClickItemListener;

    private final int VIEW_ORDER = VIEW_EMPTY + 1;

    public ChefItemOrderAdapter(Activity activity, @NonNull View.OnClickListener onClickItemListener) {
        super(activity);
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public ChefItemOrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_ORDER) {
            ChefItemRecyclerOrderBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.chef_item_recycler_order, parent, false);

            return new ChefItemOrderVH(binding, onClickItemListener);
        }
        else{
            View view = inflater.inflate(R.layout.item_recycler_empty, parent, false);
            return new ChefItemOrderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChefItemOrderVH holder, int position) {
        if (constainData()) {
            holder.onBind(list.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (constainData()) {
            return VIEW_ORDER;
        }
        return VIEW_EMPTY;
    }

    /*
     * Implement
     * */

    private int findItem(String id) {
        if (id == null) {
            return -1;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Order order = list.get(i);
            if (order != null && id.equals(order.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onAddItem(Order item) {
        if (!constainData()) {
            list = new ArrayList<>();
            list.add(item);
            runNotifyDataSetChanged();
        }else{
            list.add(0, item);
            notifyItemInserted(0);
        }
    }

    @Override
    public boolean onUpdateItem(Order item) {
        if (!constainData()) {
            return false;
        }
        int index = findItem(item.getId());
        if (index > -1) {
            list.remove(index);
            list.add(0, item);
            notifyItemMoved(index,0);
            return true;
        }
        return false;
    }

    @Override
    public boolean onUpdateOrAddItem(Order item) {
        /*int index = findItem(item.getId());
        if (index > -1) {
            orders.set(index, item);
            notifyItemChanged(index);
            return true;
        }else{
            orders.add(item);
            notifyItemInserted(orders.size() - 1);
            return false;
        }*/
        return false;
    }

    @Override
    public boolean onRemoveItem(String id) {
        if (!constainData()) {
            return false;
        }
        int index = findItem(id);
        if (index > -1) {
            if (list.size() > 1) {
                list.remove(index);
                notifyItemRemoved(index);
            }else{
                list.remove(index);
                runNotifyDataSetChanged();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void sortList() {
        // do nothing
    }

    /*
    * End
    * */
}
