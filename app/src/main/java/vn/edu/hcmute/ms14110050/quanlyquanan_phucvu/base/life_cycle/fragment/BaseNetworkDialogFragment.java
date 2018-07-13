package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment;

import android.app.Dialog;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.BR;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.ChangeNetworkStateContainer;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.MessageRunnable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.ProgressRunnable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.ToastRunnable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public abstract class BaseNetworkDialogFragment<B extends ViewDataBinding, V extends LifeCycle.View, VM extends BaseNetworkViewModel>
        extends DialogFragment implements LifeCycle.View {

    protected B binding;
    protected VM viewModel;

    protected ChangeNetworkStateContainer changeNetworkStateContainer;

    public void setChangeNetworkStateContainer(ChangeNetworkStateContainer changeNetworkStateContainer) {
        this.changeNetworkStateContainer = changeNetworkStateContainer;
    }

    protected void onRegisterChangeNetworkStateListener() {
        if (changeNetworkStateContainer != null) {
            changeNetworkStateContainer.getChangeNetworkStateListener().register(viewModel);
        }
    }

    protected void unRegisterChangeNetworkStateListener() {
        if (changeNetworkStateContainer != null) {
            changeNetworkStateContainer.getChangeNetworkStateListener().unregister(viewModel);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = initViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = initBinding(inflater, container);
        binding.setVariable(BR.viewmodel, viewModel);

        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = initBinding(inflater, null);
        binding.setVariable(BR.viewmodel, viewModel);

        return super.onCreateDialog(savedInstanceState);
    }

    protected abstract VM initViewModel();

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
            unRegisterChangeNetworkStateListener();
            viewModel.onViewDetached();
        }
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
        viewModel = null;
        binding = null;
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
    public void onToast(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new ToastRunnable(getActivity(), message));
        }
    }


    @Override
    public void onShowMessage(String message, @ColorRes final int colorTextIsRes){
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(new MessageRunnable(getContext(), binding.getRoot(), message, colorTextIsRes));
//        }
        onToast(message);
    }

    @Override
    public void onShowMessage(@StringRes final int msgResId, @ColorRes final int colorTextIsRes){
        onShowMessage(getString(msgResId), colorTextIsRes);
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private ProgressRunnable progressRunnable;

    @Override
    public void showProgress(@StringRes int idRes) {
        progressRunnable = new ProgressRunnable(getContext(), getString(idRes));

        handler.post(progressRunnable);
    }

    @Override
    public void hideProgress() {
        progressRunnable.hide();
    }

    /*
    * End
    * */

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
