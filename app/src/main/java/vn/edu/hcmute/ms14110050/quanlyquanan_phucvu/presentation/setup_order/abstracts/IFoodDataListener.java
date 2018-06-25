package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface IFoodDataListener {
    void onAddItem(Food item);

    void onUpdateItem(Food item);

    void onAddImageItem(Food item);

    void onDeleteImageItem(Food item);

    void onUpdateActivedItem(Food item);

    void onRemoveItem(String id);

    void onGetFoods(ArrayList<Food> foods);
}
