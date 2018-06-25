package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment;

import android.databinding.ViewDataBinding;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.ChangeNetworkStateContainer;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;

/**
 * Created by Vo Ngoc Hanh on 6/20/2018.
 */

public abstract class BaseNetworkFragment<B extends ViewDataBinding, V extends LifeCycle.View, VM extends BaseNetworkViewModel>
        extends BaseFragment<B, V, VM> {

    ChangeNetworkStateContainer changeNetworkStateContainer;

    public void setChangeNetworkStateContainer(ChangeNetworkStateContainer changeNetworkStateContainer) {
        this.changeNetworkStateContainer = changeNetworkStateContainer;
    }

    @Override
    protected void onRegisterChangeNetworkStateListener() {
        if (changeNetworkStateContainer != null) {
            changeNetworkStateContainer.getChangeNetworkStateListener().register(viewModel);
        }
    }

    @Override
    protected void unRegisterChangeNetworkStateListener() {
        if (changeNetworkStateContainer != null) {
            changeNetworkStateContainer.getChangeNetworkStateListener().unregister(viewModel);
        }
    }

}
