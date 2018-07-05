package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sort.RegionTableSort;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ItemRecyclerFoodBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.IFoodVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview.viewholder.FoodVH;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class FoodAdapter extends BaseAdapter<FoodVH,Food>{
    private IFoodVM containerVM;

    private final int VIEW_DATA = VIEW_EMPTY + 1;

    public FoodAdapter(Activity activity) {
        super(activity);
    }

    public void setContainerVM(IFoodVM containerVM) {
        this.containerVM = containerVM;
    }

    @Override
    public int getItemViewType(int position) {
        return constainData() ? VIEW_DATA : VIEW_EMPTY;
    }

    @NonNull
    @Override
    public FoodVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_DATA) {
            ItemRecyclerFoodBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.item_recycler_food, parent, false);
            return new FoodVH(binding, containerVM);
        }
        else{
            View view = inflater.inflate(R.layout.item_recycler_empty, parent, false);
            TextView txtMessage = view.findViewById(R.id.txt_message);
            txtMessage.setText(parent.getContext().getString(R.string.no_food));
            return new FoodVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FoodVH holder, int position) {
        if (constainData()) {
            holder.onBind(list.get(position));
        }
    }

    @Override
    public void onAddItem(Food item) {
        int index = findItem(item.getId());
        if (index < 0) {
            list.add(item);
            sortList();
        }
    }

    @Override
    public boolean onUpdateItem(Food item) {
        int index = findItem(item.getId());
        if (index >= 0) {
            list.set(index, item);
            notifyItemChanged(index);
            return true;
        }
        return false;
    }

    // Cẩn thận khi dùng cái này
    @Override
    public boolean onUpdateOrAddItem(Food item) {
        int index = findItem(item.getId());
        if (index >= 0) {
            list.set(index, item);
            notifyItemChanged(index);
            return true;
        }else{
            list.add(item);
            sortList();
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

    private int findItem(@NonNull String foodID) {
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (foodID.equals(list.get(i).getId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    protected void sortList() {
        RegionTableSort.sortFoods(list, new GetCallback<ArrayList<Food>>() {
            @Override
            public void onFinish(ArrayList<Food> _tables) {
                onSortListFinish(_tables);
            }
        });
    }

    private void onSortListFinish(ArrayList<Food> _foods) {
        list = _foods;
        runNotifyDataSetChanged();
    }
}
