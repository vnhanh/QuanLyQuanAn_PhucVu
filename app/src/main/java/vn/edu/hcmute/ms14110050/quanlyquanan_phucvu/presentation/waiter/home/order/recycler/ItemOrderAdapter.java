package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.recycler;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.WaiterItemRecyclerOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.animator.AlphaAnimator;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.IOnCheckOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderCheckable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.recycler.viewholder.ItemOrderVH;

public class ItemOrderAdapter extends BaseAdapter<ItemOrderVH, OrderCheckable> {
    private String username;
    private View.OnClickListener onClickItemListener;
    private AlphaAnimator animator = new AlphaAnimator();

    private final int VIEW_ORDER = VIEW_EMPTY + 1;
    private IOnCheckOrder onCheckItemListener;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOnCheckItemListener(IOnCheckOrder onCheckItemListener) {
        this.onCheckItemListener = onCheckItemListener;
    }

    @Override
    protected void runNotifyDataSetChanged() {
        super.runNotifyDataSetChanged();
        if (animator != null) {
            animator.reset();
        }
    }

    public ItemOrderAdapter(Activity activity, @NonNull View.OnClickListener onClickItemListener) {
        super(activity);
        this.onClickItemListener = onClickItemListener;
        animator.start();
    }

    @NonNull
    @Override
    public ItemOrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_ORDER) {
            WaiterItemRecyclerOrderBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.waiter_item_recycler_order, parent, false);

            ItemOrderVH vh = new ItemOrderVH(binding, onClickItemListener, onCheckItemListener, animator);

            return vh;
        }
        else{
            View view = inflater.inflate(R.layout.item_recycler_empty, parent, false);
            return new ItemOrderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ItemOrderVH holder, int position) {
        if (constainData()) {
            OrderCheckable item = list.get(position);
            boolean isCreater = item.getOrder().getWaiterUsername().equals(username);
            holder.onBind(list.get(position), isCreater);
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
     * Implement IRecyclerAdapter<Order>
     * */

    private int findItem(String id) {
        if (id == null) {
            return -1;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Order order = list.get(i).getOrder();
            if (order != null && id.equals(order.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onAddItem(OrderCheckable item) {
        // dữ liệu rỗng ==> tạo list, add hóa đơn vào list
        if (!constainData()) {
            list = new ArrayList<>();
            list.add(item);
            runNotifyDataSetChanged();
//            Log.d("LOG", getClass().getSimpleName() + ":onAddItem()");
        }else{
            String creater = item.getOrder().getWaiterUsername();

            // hóa đơn do chính người dùng tạo ==> ưu tiên hiển thị đầu
            if (creater != null && creater.equals(username)) {
                list.add(0, item);
                notifyItemInserted(0);
//                Log.d("LOG", getClass().getSimpleName() + ":onAddItem()");
            }
            // hóa đơn do người khác tạo
            else{
                // hóa đơn được bàn giao cho người dùng
                // ==> sẽ hiển thị ở dưới nhóm do người dùng tạo (mức ưu tiên hạng 2)
                ArrayList<String> delegacies = item.getOrder().getDelegacies();
                if (delegacies != null && delegacies.size() > 0) {
                    int delegacyCount = delegacies.size();
                    String delegacy = delegacies.get(delegacyCount - 1);

                    // người dùng được bàn giao cho hóa đơn này
                    if (username.equals(delegacy)) {
                        // tìm vị trí chèn (ngay dưới nhóm hóa đơn do chính người dùng tạo)
                        int size = list.size();
                        for (int i = 0; i < size; i++) {
                            Order order = list.get(i).getOrder();
                            String waiterUserName = order.getWaiterUsername();

                            // hóa đơn này không phải do người dùng tạo
                            if (!username.equals(waiterUserName)) {
                                list.add(i, item);
                                notifyItemInserted(i);
                                return;
                            }
                        }
                    }
                }

                // hóa đơn không được bàn giao cho người dùng
                list.add(item);
                notifyItemInserted(list.size() - 1);
//                Log.d("LOG", getClass().getSimpleName() + ":onAddItem()");
            }
        }
    }

    @Override
    public boolean onUpdateItem(OrderCheckable item) {
        if (!constainData()) {
            return false;
        }
        int index = findItem(item.getOrder().getId());
        if (index > -1) {
            if (list.size() == 1) {
                list.remove(index);
                list.add(item);
                runNotifyDataSetChanged();
            }else{
                list.set(index, item);
                notifyItemChanged(index);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onUpdateOrAddItem(OrderCheckable item) {
        /*int index = findItem(item.getId());
        if (index > -1) {
            list.set(index, item);
            notifyItemChanged(index);
            return true;
        }else{
            list.add(item);
            notifyItemInserted(list.size() - 1);
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
            Log.d("LOG", getClass().getSimpleName() + ":onRemoveItem():index:" + index);
            return true;
        }
        return false;
    }

    @Override
    protected void sortList() {
        // do nothing
    }

    /*
    * End IRecyclerAdapter<Order>
    * */

    public void destroy() {
        animator.stop();
        animator.destroy();
    }
}
