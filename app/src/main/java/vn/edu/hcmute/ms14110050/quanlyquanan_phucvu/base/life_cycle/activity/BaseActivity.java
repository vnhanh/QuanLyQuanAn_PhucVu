package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity;

import android.content.Context;
import android.content.IntentFilter;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.BR;


import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.ChangeNetworkStateContainer;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.NetworkChangeReceiver;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IProgressView;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view.MyProgressDialog;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.ActivityUtils.isOnUiThread;


public abstract class BaseActivity<B extends ViewDataBinding, V extends LifeCycle.View, VM extends BaseNetworkViewModel>
        extends AppCompatActivity implements ChangeNetworkStateContainer, LifeCycle.View {

    protected B binding;
    protected VM viewModel;
    protected NetworkChangeReceiver networkChangeReceiver;
    protected Handler handler = new Handler(Looper.getMainLooper());

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
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        viewModel.onDestroy();
        viewModel = null;
        binding = null;
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

    /*
     * IProgressView
     * */

    public Context getContext() {
        return this;
    }

    @Override
    public void onToast(String message) {
        runOnUiThread(new ToastRunnable(this, message));
    }

    @Override
    public void onToast(int msgIdRes) {
        runOnUiThread(new ToastRunnable(this, msgIdRes));
    }

    @Override
    public void onShowMessage(String message, @ColorRes final int colorTextIsRes){
        runOnUiThread(new MessageRunnable(this, binding.getRoot(), message, colorTextIsRes));
    }

    @Override
    public void onShowMessage(@StringRes final int messageIdRes, @ColorRes final int colorTextIsRes){
        runOnUiThread(new MessageRunnable(this, binding.getRoot(), getString(messageIdRes), colorTextIsRes));
    }

//    private Handler handler = new Handler(Looper.getMainLooper()) {
//        private AlertDialog progressDialog;
//
//        @Override
//        public void handleMessage(Message msg) {
//            Context context = (Context) msg.obj;
//            String message = msg.getData().getString("msg");
//            int flag = msg.what;
//            if (flag == 0) {
//                progressDialog = MyProgressDialog.create(context, message);
//                progressDialog.show();
//            } else if (flag == 1) {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//        }
//    };

    private AlertDialog progressDialog;

    @Override
    public void showProgress(@StringRes final int idRes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = MyProgressDialog.create(BaseActivity.this, idRes);
                progressDialog.show();
            }
        });
//        Message msg = handler.obtainMessage(0, this);
//        Bundle bundle = new Bundle();
//        bundle.putString("msg", getString(idRes));
//        msg.setData(bundle);
//        msg.sendToTarget();
    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

//        Message msg = handler.obtainMessage(1);
//        msg.sendToTarget();
    }

    /*
     * End
     * */

}
