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
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ChefItemRecyclerDetailOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestApi;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder.ChefItemOrderVH;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder.IDetailOrderProcessor;


public class ChefDetailOrderAdapter extends BaseAdapter<ChefItemOrderVH, DetailOrder> implements IDetailOrderAdapter{
    private View.OnClickListener onClickItemListener;
    private IDetailOrderProcessor processor;
    private final int VIEW_ORDER = VIEW_EMPTY + 1;

    public ChefDetailOrderAdapter(Activity activity,
                                  @NonNull View.OnClickListener onClickItemListener,
                                  @NonNull IDetailOrderProcessor processor) {
        super(activity);
        this.onClickItemListener = onClickItemListener;
        this.processor = processor;
    }

    public void destroy() {
        onClickItemListener = null;
        processor = null;
    }

    @NonNull
    @Override
    public ChefItemOrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_ORDER) {
            ChefItemRecyclerDetailOrderBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.chef_item_recycler_detail_order, parent, false);

            return new ChefItemOrderVH(weakActivity, binding, onClickItemListener, processor);
        }
        else{
            View view = inflater.inflate(R.layout.item_recycler_empty, parent, false);
            return new ChefItemOrderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChefItemOrderVH holder, int position) {
        if (constainData()) {
            holder.onBind(list.get(position), position);
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
            DetailOrder item = list.get(i);

            if (item != null) {
                String _id = item.getId();

                if (id.equals(_id)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void onAddItem(DetailOrder item) {
        if (!constainData()) {
            list = new ArrayList<>();
            list.add(item);
            runNotifyDataSetChanged();
        }else{
            list.add(item);
            notifyItemInserted(list.size()-1);
        }
    }

    @Override
    public boolean onUpdateItem(DetailOrder item) {
        if (!constainData()) {
            return false;
        }

        String id = item.getId();
        int index = findItem(id);

        if (index > -1) {
            list.set(index, item);
            notifyItemChanged(index);
            return true;
        }
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
                list.clear();
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

    @Override
    public void onRemoveDetails(String orderID) {
        if (orderID == null || !constainData()) {
            return;
        }

        int i = 0;
        while (i < list.size()) {
            DetailOrder item = list.get(i);
            String _orderID = item.getOrderID();

            if (orderID.equals(_orderID)) {
                list.remove(i);
                notifyItemRemoved(i);
            }else{
                i++;
            }
        }
    }

    /*
    * End
    * */
}
