package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.ApiUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.AuthenticationService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginResponseData;

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
        if (isViewAttached()) {
            getView().showProgress();
        }

        // set đúng loại tài khoản
        request.setTypeUser(NATIVE_TYPE_USER);

        authService.login(request)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponseData response) {
                        hideProgress();

                        if (response.getSuccess()) {
                            Map<String,String> map = response.getUser();
                            String typeUser = map.get("type_account");
                            int _type = Integer.parseInt(typeUser);
                            if (_type == NATIVE_TYPE_USER) {
                                onLoginSuccess(response);
                            }else{
                                onLoginFailed();
                            }
                        }else{
                            onLoginFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", getClass().getSimpleName() + ":onSubmit():get error:" + e.getMessage());
                        hideProgress();
                        if (e.getMessage().startsWith("failed to connect to")) {
                            onDisconnectToServer();
                        }else{
                            onLoginError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onDisconnectToServer() {
        if (isViewAttached()) {
            getView().onDisconnectToServer();
        }
    }

    private void onLoginError() {
        if (isViewAttached()) {
            getView().onLoginError();
        }
    }

    private void hideProgress() {
        if (isViewAttached()) {
            getView().hideProgress();
        }
    }

    // Đăng nhập thành công
    // Load dữ liệu người dùng và thoát
    private void onLoginSuccess(LoginResponseData response) {
        SSharedReference.setToken(getView().getContext(), response.getToken());

        if (!isViewAttached()) {
            return;
        }
        getView().showMessage(R.string.message_login_success);
        getView().openHomeActivity(response.getUsername());
    }

    // Đăng nhập thất bại
    private void onLoginFailed() {
        if (isViewAttached()) {
            getView().onLoginFailed();
        }
    }
}