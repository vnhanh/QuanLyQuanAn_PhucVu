package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.processors.AsyncProcessor;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;


public abstract class BaseViewModel <V extends LifeCycle.View>
        implements LifeCycle.ViewModel<V>, Observer{

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
        if (disposable != null) {
            disposable.dispose();
        }
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

    /*
     * RxJava
     * */
    protected AsyncProcessor<ResponseValue> asyncProcessor;

    protected Disposable disposable;

    protected void createAsyncProcessor() {
        asyncProcessor = AsyncProcessor.create();
    }

    protected void setupForNewRequest() {
        createAsyncProcessor();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (this.disposable != null) {
            disposable.dispose();
        }
        this.disposable = disposable;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
