package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sort;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.asynctask.SortTask;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class RegionTableSort {
    public static void sortTables(@NonNull ArrayList<Table> tables, @NonNull GetCallback<ArrayList<Table>> callback) {
        Comparator<Table> comparator = new Comparator<Table>() {
            @Override
            public int compare(Table table1, Table table2) {
                String id1 = StrUtil.deAccent(table1.getId());
                String id2 = StrUtil.deAccent(table2.getId());
                return id1.compareToIgnoreCase(id2);
            }
        };
        new SortTask<Table>(tables, comparator, callback).execute();
    }

    public static void sortFoods(@NonNull ArrayList<Food> foods, @NonNull GetCallback<ArrayList<Food>> callback) {
        Comparator<Food> comparator = new Comparator<Food>() {
            @Override
            public int compare(Food item1, Food item2) {
                String id1 = StrUtil.deAccent(item1.getId());
                String id2 = StrUtil.deAccent(item2.getId());
                return id1.compareToIgnoreCase(id2);
            }
        };
        new SortTask<Food>(foods, comparator, callback).execute();
    }
}
