package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.abstracts;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.DelegacyResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.SuggestDelegacy;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public interface WaiterListOrdersContract {
    interface View extends LifeCycle.View{

        void openAddOrderScreen();

        // hiển thị hộp thoại xác nhận đề nghị bàn giao
        void onShowConfirmSuggestDelegacy(SuggestDelegacy suggest);

        // hiển thị hộp thoại thông báo phản hồi bàn giao
        void onShowNotiResponseDelegacy(DelegacyResponse response);
    }
}
