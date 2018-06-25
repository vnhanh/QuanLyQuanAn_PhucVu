package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/20/2018.
 */

public class BaseNetworkViewModel<VIEW extends LifeCycle.View> extends BaseViewModel<VIEW>
                                                                implements OnChangeNetworkStateListener {
    public final ObservableBoolean isNetworkConnected = new ObservableBoolean(false);

    /*
        * OnChangeNetworkStateListener
        * */
    @Override
    public void onConnect() {
        isNetworkConnected.set(true);
    }

    @Override
    public void onDisconnect() {
        isNetworkConnected.set(false);
    }

    /*
    * END OnChangeNetworkStateListener
    * */

}
