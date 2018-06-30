package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity;

import android.content.IntentFilter;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.ChangeNetworkStateContainer;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.NetworkChangeReceiver;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseViewModel;


public abstract class BaseActivity<B extends ViewDataBinding, V extends LifeCycle.View, VM extends BaseNetworkViewModel>
        extends AppCompatActivity implements ChangeNetworkStateContainer{

    protected B binding;
    protected VM viewModel;
    protected NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = initViewModel();
        binding = initBinding();
        binding.setVariable(BR.viewmodel, viewModel);
        // khởi tạo và đăng ký NetworkChangeReceiver
        initNetworkChangeReceiver();
    }

    protected abstract B initBinding();

    protected abstract VM initViewModel();

    private void initNetworkChangeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel != null) {
            networkChangeReceiver.register(viewModel);
            onAttachViewModel();
        }
    }

    protected abstract void onAttachViewModel();

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.onViewResumed();
        }
    }

    @Override
    protected void onStop() {
        if (viewModel != null) {
            networkChangeReceiver.unregister(viewModel);
            viewModel.onViewDetached();
        }
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        unregisterReceiver(networkChangeReceiver);
        viewModel.onDestroy();
        viewModel = null;
        binding = null;
        super.onDestroy();
    }

    @Override
    public NetworkChangeReceiver getChangeNetworkStateListener() {
        return networkChangeReceiver;
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    public String string(@StringRes int resId) {
        return getResources().getString(resId);
    }
}
