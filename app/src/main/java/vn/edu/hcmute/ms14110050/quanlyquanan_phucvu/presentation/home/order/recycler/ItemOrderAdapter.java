package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.recycler;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ItemRecyclerOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.recycler.viewholder.ItemOrderVH;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IRecyclerViewAdapterListener;

public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderVH> implements IRecyclerViewAdapterListener<Order>{
    private ArrayList<Order> orders = new ArrayList<>();
    private View.OnClickListener onClickItemListener;

    public ItemOrderAdapter(@NonNull View.OnClickListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public ItemOrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRecyclerOrderBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.item_recycler_order, parent, false);

        return new ItemOrderVH(binding, onClickItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemOrderVH holder, int position) {
        holder.onBind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    /*
     * Implement IRecyclerViewAdapterListener<Order>
     * */
    private int findItem(String id) {
        if (id == null) {
            return -1;
        }
        int size = orders.size();
        for (int i = 0; i < size; i++) {
            if ((orders.get(i) != null && id.equals(orders.get(i).getId()))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onAddItem(Order item) {
        for (Order order : orders) {
            if (order.getId().equals(item.getId())) {
                return;
            }
        }
        orders.add(0, item);
        notifyItemInserted(0);
    }

    @Override
    public boolean onUpdateItem(Order item) {
        int index = findItem(item.getId());
        if (index > -1) {
            orders.remove(index);
            orders.add(0, item);
            notifyItemMoved(index,0);
            return true;
        }
        return false;
    }

    // TODO: có cần ?
    @Override
    public boolean onUpdateOrAddItem(Order item) {
        int index = findItem(item.getId());
        if (index > -1) {
            orders.set(index, item);
            notifyItemChanged(index);
            return true;
        }else{
            orders.add(item);
            notifyItemInserted(orders.size() - 1);
            return false;
        }
    }

    @Override
    public boolean onRemoveItem(String id) {
        int index = findItem(id);
        if (index > -1) {
            orders.remove(index);
            notifyItemRemoved(index);
            return true;
        }
        return false;
    }

    @Override
    public void onGetList(ArrayList<Order> list) {
        orders = list;
        notifyDataSetChanged();
    }

    @Override
    public ArrayList<Order> getList() {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        return orders;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return this;
    }

    /*
    * End IRecyclerViewAdapterListener<Order>
    * */
}
