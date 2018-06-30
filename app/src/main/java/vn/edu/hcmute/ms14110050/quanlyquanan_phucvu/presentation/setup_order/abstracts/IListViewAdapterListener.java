package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

/**
 * Created by Vo Ngoc Hanh on 6/23/2018.
 */

public interface IListViewAdapterListener<DATA> {

    void onAddItem(DATA item);

    boolean onUpdateItem(DATA item);

    boolean onUpdateOrAddItem(DATA item);

    boolean onRemoveItem(String id);

    void onGetList(ArrayList<DATA> list);

    ArrayList<DATA> getList();
}
