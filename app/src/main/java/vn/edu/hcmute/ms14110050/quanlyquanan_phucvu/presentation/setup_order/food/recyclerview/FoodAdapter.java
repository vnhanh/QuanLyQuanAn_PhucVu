package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sort.RegionTableSort;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ItemRecyclerFoodBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IListAdapterListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.IFoodVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview.viewholder.FoodVH;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodVH> implements IListAdapterListener<Food> {
    private ArrayList<Food> foods = new ArrayList<>();
    private IFoodVM containerVM;

    private final int EMPTY_VIEW = -1;
    private final int DATA_VIEW = 0;

    public void setContainerVM(IFoodVM containerVM) {
        this.containerVM = containerVM;
    }

    @Override
    public int getItemViewType(int position) {
        return hasNoData() ? EMPTY_VIEW : DATA_VIEW;
    }

    private boolean hasNoData() {
        return foods == null || foods.size() == 0;
    }

    @NonNull
    @Override
    public FoodVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == EMPTY_VIEW) {
            View view = inflater.inflate(R.layout.item_recycler_empty, parent, false);
            TextView txtMessage = view.findViewById(R.id.txt_message);
            txtMessage.setText(parent.getContext().getString(R.string.no_food));
            return new FoodVH(view);
        }else{
            ItemRecyclerFoodBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.item_recycler_food, parent, false);
            return new FoodVH(binding, containerVM);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FoodVH holder, int position) {
        if (!hasNoData()) {
            holder.onBind(foods.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return hasNoData() ? 1 : foods.size();
    }

    @Override
    public void onAddItem(Food item) {
        int index = findItem(item.getId());
        if (index < 0) {
            foods.add(item);
            sortList();
        }
    }

    @Override
    public boolean onUpdateItem(Food item) {
        int index = findItem(item.getId());
        if (index >= 0) {
            foods.set(index, item);
            notifyItemChanged(index);
            return true;
        }
        return false;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return this;
    }

    // Cẩn thận khi dùng cái này
    @Override
    public boolean onUpdateOrAddItem(Food item) {
        int index = findItem(item.getId());
        if (index >= 0) {
            foods.set(index, item);
            notifyItemChanged(index);
            return true;
        }else{
            foods.add(item);
            sortList();
        }
        return false;
    }

    @Override
    public boolean onRemoveItem(String id) {
        int index = findItem(id);
        if (index >= 0) {
            foods.remove(index);
            notifyItemRemoved(index);
            return true;
        }
        return false;
    }

    @Override
    public void onGetList(ArrayList<Food> list) {
        this.foods = list;
        sortList();
    }

    @Override
    public ArrayList<Food> getList() {
        if (foods == null) {
            foods = new ArrayList<>();
        }
        return foods;
    }

    private int findItem(@NonNull String foodID) {
        if (foods != null) {
            int size = foods.size();
            for (int i = 0; i < size; i++) {
                if (foodID.equals(foods.get(i).getId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void sortList() {
        RegionTableSort.sortFoods(foods, new GetCallback<ArrayList<Food>>() {
            @Override
            public void onFinish(ArrayList<Food> _tables) {
                onSortListFinish(_tables);
            }
        });
    }

    private void onSortListFinish(ArrayList<Food> _foods) {
        foods = _foods;
        notifyDataSetChanged();
    }
}
