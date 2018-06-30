package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public interface IFoodView extends LifeCycle.View {

    void onLoadingCategories();

    void onEndLoadingCategories();
}
