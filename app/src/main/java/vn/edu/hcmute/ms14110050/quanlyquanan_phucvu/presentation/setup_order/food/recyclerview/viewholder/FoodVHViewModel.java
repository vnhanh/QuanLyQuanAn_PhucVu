package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview.viewholder;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseVHViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IProgressVH;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class FoodVHViewModel extends BaseVHViewModel<IProgressVH> {
    private Food food;



    // số lượng đã order
    public ObservableInt count = new ObservableInt();
    public ObservableBoolean isOrdering = new ObservableBoolean(false);

    private String orderId;
    private String token;

    /*
    * DataBinding
    * */
    public void onClickOrderBtn() {
        isOrdering.set(!isOrdering.get());
    }

    /*
    * End DataBinding
    * */
}
