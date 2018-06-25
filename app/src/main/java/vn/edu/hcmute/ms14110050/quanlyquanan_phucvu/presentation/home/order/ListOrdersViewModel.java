package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;

import android.util.Log;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class ListOrdersViewModel extends BaseNetworkViewModel<ListOrdersContract.View> implements GetCallback<User> {
    private User user;

    /*
    * Property
    * */

    public User getUser() {
        return user;
    }

    /*
    * End Property
    * */

    /*
    * ONCLICK VIEW
    * */
    public void onClickAddOrderButton() {
        if (isViewAttached()) {
            getView().openAddOrderScreen();
        }
    }

    /*
    * IMPLEMENT GetCallback<User>
    * */
    @Override
    public void onFinish(User user) {
        this.user = user;
    }

}
