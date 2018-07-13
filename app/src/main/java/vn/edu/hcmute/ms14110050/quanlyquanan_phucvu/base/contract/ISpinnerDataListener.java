package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.contract;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.Region;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface ISpinnerDataListener<DATA> {
    void onGetList(ArrayList<DATA> list);

    void onAddItem(DATA item);

    void onUpdateItem(DATA item);

    void onDeleteItem(String id);
}
