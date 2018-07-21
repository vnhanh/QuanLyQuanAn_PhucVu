package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.detail_order;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.WaiterItemRecyclerDetailOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.IOrderVM;

public class WaiterDetailOrderAdapter extends BaseAdapter<DetailOrderVH, DetailOrder> {
    private IOrderVM centerVM;
    private final int VIEW_DATA = VIEW_EMPTY + 1;

    public WaiterDetailOrderAdapter(Activity activity, IOrderVM centerVM) {
        super(activity);
        this.centerVM = centerVM;
    }

    @Override
    public int getItemViewType(int position) {
        return constainData() ? VIEW_DATA : VIEW_EMPTY;
    }

    @NonNull
    @Override
    public DetailOrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_DATA) {

            WaiterItemRecyclerDetailOrderBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.waiter_item_recycler_detail_order, parent, false);
            return new DetailOrderVH(binding, centerVM);
        }
        else{
            View view = inflater.inflate(R.layout.item_recycler_empty, parent, false);
            TextView txtMessage = view.findViewById(R.id.txt_message);
            txtMessage.setText(parent.getContext().getString(R.string.no_detail_order));
            return new DetailOrderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DetailOrderVH holder, int position) {
        if (constainData()) {
            holder.onBind(list.get(position), position);
        }
    }

    private int findItem(@NonNull String id) {
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (id.equals(list.get(i).getId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void onAddItem(DetailOrder item) {
        if(!constainData()){
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(item);
            runNotifyDataSetChanged();
        }
        else{
            int index = findItem(item.getId());
            if (index < 0) {
                list.add(item);
                notifyItemInserted(list.size() - 1);
            }
        }
    }

    @Override
    public boolean onUpdateItem(DetailOrder item) {
        int index = findItem(item.getId());
        if (index >= 0) {
            list.set(index, item);
            notifyItemChanged(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean onRemoveItem(String id) {
        int index = findItem(id);
        if (index >= 0) {
            list.remove(index);
            notifyItemRemoved(index);
            return true;
        }
        return false;
    }

    @Override
    protected void sortList() {

    }
}
