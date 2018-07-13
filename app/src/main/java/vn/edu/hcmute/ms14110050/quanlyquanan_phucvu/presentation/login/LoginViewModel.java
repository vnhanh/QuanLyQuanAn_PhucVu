package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.ApiUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.AuthenticationService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginResponseData;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_SUCCESS;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.NATIVE_TYPE_USER;


/**
 * Created by Vo Ngoc Hanh on 5/11/2018.
 */


public class LoginViewModel extends BaseNetworkViewModel<LoginContract.View> {
    AuthenticationService authService;

    public LoginViewModel() {
        authService = ApiUtil.getAuthService();
    }

    @Override
    public void onViewAttach(@NonNull LoginContract.View viewCallback) {
        super.onViewAttach(viewCallback);
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
    }

    // Click nút đăng nhập
    public void onSubmit(LoginRequest request) {
        showProgress(R.string.message_logining);

        authService.login(request)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponseData response) {
                        hideProgress();

                        if (response.isSuccess()) {
                            onLoginSuccess(response);
                        }
                        else{
                            String message = getString(R.string.message_login_failed, response.getMessage());
                            onLoginFailed(message);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", getClass().getSimpleName() + ":onSubmit():get error:" + e.getMessage());

                        hideProgress();

                        if (e.getMessage().startsWith("failed to connect to")) {
                            onLoginError(R.string.message_disconnect_server);
                        }
                        else{
                            onLoginError(R.string.message_login_error);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onLoginError(@StringRes int msgResId) {
        if (isViewAttached()) {
            getView().onLoginError(msgResId);
        }
    }

    // Đăng nhập thành công
    // Load dữ liệu người dùng và thoát
    private void onLoginSuccess(LoginResponseData response) {
        SSharedReference.setToken(getView().getContext(), response.getToken());

        if (!isViewAttached()) {
            return;
        }
        showMessage(R.string.message_login_success, COLOR_SUCCESS);
        getView().openHomeActivity(response.getUsername());
    }

    // Đăng nhập thất bại
    private void onLoginFailed(int msgResId) {
        if (isViewAttached()) {
            getView().onLoginFailed(msgResId);
        }
    }

    // Đăng nhập thất bại
    private void onLoginFailed(String message) {
        if (isViewAttached()) {
            getView().onLoginFailed(message);
        }
    }
}