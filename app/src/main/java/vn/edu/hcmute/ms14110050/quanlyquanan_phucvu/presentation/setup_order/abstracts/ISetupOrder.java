package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts;


import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public interface ISetupOrder {
    interface View extends LifeCycle.View {

        void onShowLoadTablesByOrderIDProgress();

        void onHideLoadTablesByOrderIDProgress();
    }
}
