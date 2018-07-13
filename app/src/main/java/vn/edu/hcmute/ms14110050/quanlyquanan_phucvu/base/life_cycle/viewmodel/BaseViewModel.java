package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;


public abstract class BaseViewModel <V extends LifeCycle.View> implements LifeCycle.ViewModel<V> {

    private V view;

    @Override
    public void onViewAttach(@NonNull V viewCallback) {
        this.view = viewCallback;
    }

    @Override
    public void onViewResumed() {

    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onDestroy() {

    }

    protected void showToast(@StringRes int messageIdRes) {
        if (isViewAttached()) {
            getView().onToast(messageIdRes);
        }
    }

    protected void showToast(String message) {
        if (isViewAttached()) {
            getView().onToast(message);
        }
    }

    protected void showMessage(@StringRes int messageIdRes, @ColorRes int colorIdRes) {
        if (isViewAttached()) {
            getView().onShowMessage(messageIdRes, colorIdRes);
        }
    }

    protected void showMessage(String message, @ColorRes int colorIdRes) {
        if (isViewAttached()) {
            getView().onShowMessage(message, colorIdRes);
        }
    }

    protected void showProgress(@StringRes int idRes) {
        if (isViewAttached()) {
            getView().showProgress(idRes);
        }
    }

    protected void hideProgress() {
        if (isViewAttached()) {
            getView().hideProgress();
        }
    }

    public final boolean isViewAttached() {
        return view != null;
    }

    public final V getView() {
        return view;
    }

    protected Context getContext() {
        return view != null ? view.getContext() : null;
    }

    protected Resources getResources() {
        return view != null ? view.getContext().getResources() : null;
    }

    protected String getString(@StringRes int idRes, Object... args) {
        return view != null ? view.getContext().getString(idRes, args) : "";
    }

    protected String getString(@StringRes int idRes) {
        return view != null ? view.getContext().getString(idRes) : "";
    }
}
