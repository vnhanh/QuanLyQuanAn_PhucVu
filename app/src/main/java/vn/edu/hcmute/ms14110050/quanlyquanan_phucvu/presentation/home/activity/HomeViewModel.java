package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.activity;

import android.support.annotation.NonNull;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.OnChangeSocketStateListener;

public class HomeViewModel extends BaseNetworkViewModel<IHomeView>
        implements OnChangeSocketStateListener {

    private boolean FLAG_IS_LOGINING = true;

    private String userName;
    private int typeAccount;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTypeAccount(int typeAccount) {
        this.typeAccount = typeAccount;
    }

    public int getTypeAccount() {
        return typeAccount;
    }

    @Override
    public void onViewAttach(@NonNull IHomeView viewCallback) {
        super.onViewAttach(viewCallback);
        if (!FLAG_IS_LOGINING) {
            showToast(R.string.server_disconnect);
            getView().onLogout();
        }
    }

    /*
     * OnChangeSocketStateListener
     * */
    @Override
    public void onSocketConnect() {

    }

    @Override
    public void onSocketDisconnect() {
        FLAG_IS_LOGINING = false;

        showToast(R.string.server_disconnect);

        if (isViewAttached()) {
            getView().onLogout();
        }
    }

    /*
     * End OnChangeSocketStateListener
     * */

}
