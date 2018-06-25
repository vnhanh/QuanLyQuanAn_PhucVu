package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFood;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface ICategoryFoodDataListener {
    void onAddItem(CategoryFood item);

    void onUpdateItem(CategoryFood item);

    void onDeleteItem(String itemID);

    void onGetCategories(ArrayList<CategoryFood> categories);
}
