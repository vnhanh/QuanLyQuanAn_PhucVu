package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public interface ListOrdersContract {
    interface View extends LifeCycle.View{

        void openAddOrderScreen();

    }
}
