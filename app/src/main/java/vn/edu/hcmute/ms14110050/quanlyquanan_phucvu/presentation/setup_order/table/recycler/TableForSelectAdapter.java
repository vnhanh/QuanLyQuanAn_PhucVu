package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.recycler;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sort.RegionTableSort;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ItemRecyclerTableBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.ITableVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.recycler.viewholder.TableViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ITableDataListener;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class TableForSelectAdapter extends RecyclerView.Adapter<TableViewHolder> implements ITableDataListener{
    private ArrayList<Table> tables = new ArrayList<>();
    private ITableVM containerVM;

    public TableForSelectAdapter() {

    }
    /*
    * Property
    * */

    public void setCenterViewModel(ITableVM viewModel) {
        this.containerVM = viewModel;
    }

    /*
    * UI
    * */

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == EMPTY_VIEW) {
            View view = inflater.inflate(R.layout.item_recycler_empty, parent, false);
            TextView txtMessage = view.findViewById(R.id.txt_message);
            txtMessage.setText(parent.getContext().getString(R.string.no_table));
            return new TableViewHolder(view);
        }else{
            ItemRecyclerTableBinding binding =
                    DataBindingUtil.inflate(inflater, R.layout.item_recycler_table, parent, false);
            return new TableViewHolder(binding, containerVM);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        if (!hasNoData()) {
            holder.onBind(tables.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return hasNoData() ? 1 : tables.size();
    }

    private final int EMPTY_VIEW = -1;
    private final int DATA_VIEW = 0;

    @Override
    public int getItemViewType(int position) {
        return hasNoData() ? EMPTY_VIEW : DATA_VIEW;
    }

    private boolean hasNoData() {
        return tables == null || tables.size() == 0;
    }

    /*
    * Xử lý dữ liệu
    * */

    private int findTable(@NonNull String tableID) {
        if (tables != null) {
            int size = tables.size();
            for (int i = 0; i < size; i++) {
                if (tableID.equals(tables.get(i).getId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void sortList() {
        RegionTableSort.sortTables(tables, new GetCallback<ArrayList<Table>>() {
            @Override
            public void onFinish(ArrayList<Table> _tables) {
                onSortListFinish(_tables);
            }
        });
    }

    private void onSortListFinish(ArrayList<Table> _tables) {
        tables = _tables;
        notifyDataSetChanged();
    }

    @Override
    public void onAddTable(Table table) {
        int index = findTable(table.getId());
        if (index < 0) {
            tables.add(table);
            sortList();
        }
    }

    @Override
    public void onUpdateTable(Table table) {
        int index = findTable(table.getId());
        if (index >= 0) {
            Table old = tables.get(index);
            if (!old.equalValue(table)) {
                tables.set(index, table);
                notifyItemChanged(index);
            }
        }
    }

    @Override
    public void onUpdateOrAddTable(Table table) {
        int index = findTable(table.getId());
        if (index >= 0) {
            Table old = tables.get(index);
            if (!old.equals(table)) {
                tables.set(index, table);
                notifyItemChanged(index);
            }
        }else{
            tables.add(table);
            sortList();
        }
    }

    @Override
    public void onDeleteTable(String tableID) {
        int index = findTable(tableID);
        if (index >= 0) {
            tables.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public void onGetTablesByOrderID(ArrayList<Table> tables) {
        this.tables = tables;
        sortList();
    }

    @Override
    public ArrayList<Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        return tables;
    }
}
