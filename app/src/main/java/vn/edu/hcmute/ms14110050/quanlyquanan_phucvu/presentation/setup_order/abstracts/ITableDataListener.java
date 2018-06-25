package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

/**
 * Created by Vo Ngoc Hanh on 6/23/2018.
 */

public interface ITableDataListener {

    void onAddTable(Table table);

    void onUpdateTable(Table table);

    void onUpdateOrAddTable(Table table);

    void onDeleteTable(String tableID);

    void onGetTablesByOrderID(ArrayList<Table> tables);

    ArrayList<Table> getTables();
}
