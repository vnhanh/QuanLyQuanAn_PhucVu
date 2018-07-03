package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.MessageRunnable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.ToastRunnable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IProgressView;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view.MyProgressDialog;


public abstract class BaseFragment<B extends ViewDataBinding, V extends LifeCycle.View, VM extends BaseViewModel>
        extends Fragment implements LifeCycle.View {

    protected B binding;
    protected VM viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = initViewModel();
    }

    protected abstract VM initViewModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = initBinding(inflater, container);
        binding.setVariable(BR.viewmodel, viewModel);

        return binding.getRoot();
    }

    protected abstract B initBinding(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onStart() {
        super.onStart();
        if (viewModel != null) {
            onRegisterChangeNetworkStateListener();
            onAttachViewModel();
        }
    }

    protected abstract void onAttachViewModel();

    protected abstract void onRegisterChangeNetworkStateListener();

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.onViewResumed();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (viewModel != null) {
            if (viewModel instanceof OnChangeNetworkStateListener) {
                unRegisterChangeNetworkStateListener();
            }
            viewModel.onViewDetached();
        }
    }

    protected abstract void unRegisterChangeNetworkStateListener();

    @Override
    @CallSuper
    public void onDestroyView() {
        viewModel.onDestroy();
        viewModel = null;
        binding = null;
        super.onDestroyView();
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

    @Override
    public void onToast(int msgIdRes) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new ToastRunnable(getActivity(), msgIdRes));
        }

    }

    @Override
    public void onShowMessage(String message, @ColorRes final int colorTextIsRes){
        if (getActivity() != null) {
            getActivity().runOnUiThread(new MessageRunnable(getContext(), binding.getRoot(), message, colorTextIsRes));
        }
    }

    @Override
    public void onShowMessage(@StringRes final int msgResId, @ColorRes final int colorTextIsRes){
        if (getActivity() != null) {
            getActivity().runOnUiThread(new MessageRunnable(getContext(), binding.getRoot(), getString(msgResId), colorTextIsRes));
        }
    }

    private AlertDialog progressDialog;

    @Override
    public void showProgress(@StringRes int idRes) {
        if (progressDialog == null) {
            progressDialog = MyProgressDialog.create(getContext(), idRes);
        }
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    /*
     * End
     * */
}
