package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.table.recycler;

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
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ChefItemRecyclerTableBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.table.recycler.viewholder.ChefTableVH;


/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class ChefTableAdapter extends BaseAdapter<ChefTableVH,Table> {

    private final int VIEW_DATA = VIEW_EMPTY + 1;

    public ChefTableAdapter(Activity activity) {
        super(activity);
    }

    /*
    * UI
    * */

    @Override
    public int getItemViewType(int position) {
        return constainData() ? VIEW_DATA : VIEW_EMPTY;
    }

    @NonNull
    @Override
    public ChefTableVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_DATA) {
            ChefItemRecyclerTableBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.chef_item_recycler_table, parent, false);
            return new ChefTableVH(binding);
        }
        else{
            View view = inflater.inflate(R.layout.item_recycler_empty, parent, false);
            TextView txtMessage = view.findViewById(R.id.txt_message);
            txtMessage.setText(parent.getContext().getString(R.string.no_table));
            return new ChefTableVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChefTableVH holder, int position) {
        if (constainData()) {
            holder.onBind(list.get(position));
        }
    }

    /*
    * Xử lý dữ liệu
    * */

    private int findTable(@NonNull String tableID) {
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (tableID.equals(list.get(i).getId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    protected void sortList() {
        RegionTableSort.sortTables(list, new GetCallback<ArrayList<Table>>() {
            @Override
            public void onFinish(ArrayList<Table> _tables) {
                onSortListFinish(_tables);
            }
        });
    }

    private void onSortListFinish(ArrayList<Table> _tables) {
        list = _tables;
        runNotifyDataSetChanged();
    }

    @Override
    public void onAddItem(Table table) {
        int index = findTable(table.getId());
        if (index < 0) {
            list.add(table);
            sortList();
        }
    }

    @Override
    public boolean onUpdateItem(Table table) {
        int index = findTable(table.getId());
        if (index >= 0) {
            Table old = list.get(index);
            if (!old.equalValue(table)) {
                list.set(index, table);
                notifyItemChanged(index);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onUpdateOrAddItem(Table table) {
        int index = findTable(table.getId());
        if (index >= 0) {
            Table old = list.get(index);
            if (!old.equals(table)) {
                list.set(index, table);
                notifyItemChanged(index);
                return true;
            }
        }else{
            list.add(table);
            sortList();
        }
        return false;
    }

    @Override
    public boolean onRemoveItem(String tableID) {
        int index = findTable(tableID);
        if (index >= 0) {
            list.remove(index);
            notifyItemRemoved(index);
            return true;
        }
        return false;
    }
}
