package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.account;

import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.Callback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.ApiUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.AuthenticationService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.ChangePasswordRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.GetProfileResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.ResAccFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.FindAccountResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/16/2018.
 */

public class AccountRequestManager {
    private static AccountRequestManager INSTANCE;
    AuthenticationService authService;

    private AccountRequestManager() {
        authService = ApiUtil.getAuthService();
    }

    public static AccountRequestManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AccountRequestManager();
        }
        return INSTANCE;
    }

    public void getProfile(@NonNull String token, @NonNull String username, final GetCallback<User> callback) {
        if (authService == null) {
            createAuthService();
        }
        authService.getProfile(token, username)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GetProfileResponse response) {
                        if (response.isSuccess()) {
                            callback.onFinish(response.getUser());
                        }else{
                            Log.d("LOG", AccountRequestManager.class.getSimpleName()
                                    + ":getProfile:on failed:message:" + response.getMessage());
                            callback.onFinish(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", AccountRequestManager.class.getSimpleName()
                                + ":getProfile:onError():error:" + e.getMessage());
                        callback.onFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void update(@NonNull String token, @NonNull User user, final GetCallback<Boolean> callback) {
        if (authService == null) {
            createAuthService();
        }
        authService.updateProfile(token, user)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseValue>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseValue response) {
                        if (response.isSuccess()) {
                            callback.onFinish(true);
                        } else {
                            callback.onFinish(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", AccountRequestManager.class.getSimpleName() + ":update:onError():" + e.getMessage());
                        callback.onFinish(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void verifyAccount(@NonNull String token, @NonNull LoginRequest request, final GetCallback<Boolean> callback) {
        if (authService == null) {
            createAuthService();
        }
        authService.verify(token, request)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseValue>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseValue responseValue) {
                        if (responseValue.isSuccess()) {
                            callback.onFinish(true);
                        }else{
                            callback.onFinish(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", AccountRequestManager.class.getSimpleName() + ":verify:onError():" + e.getMessage());
                        callback.onFinish(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updatePassword(@NonNull String token, @NonNull ChangePasswordRequest request, final GetCallback<Boolean> callback) {
        if (authService == null) {
            createAuthService();
        }
        authService.updatePassword(token, request)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseValue>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseValue responseValue) {
                        if (responseValue.isSuccess()) {
                            callback.onFinish(true);
                        }else{
                            Log.d("LOG", AccountRequestManager.class.getSimpleName()
                                    + ":updatePassword():failed:" + responseValue.getMessage());
                            callback.onFinish(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", AccountRequestManager.class.getSimpleName()
                                + ":updatePassword():error:" + e.getMessage());
                        callback.onFinish(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void findAccount(@NonNull String token, @NonNull String username, int typeAccount,
                            final GetCallback<FindAccountResponse> callback) {

        authService.findAccount(token, username, typeAccount)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<FindAccountResponse>() {
                    @Override
                    public void onNext(FindAccountResponse response) {
                        callback.onFinish(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", AccountRequestManager.class.getSimpleName()
                                + ":findAccount():error:" + e.getMessage());
                        FindAccountResponse response = new FindAccountResponse();
                        response.setSuccess(false);
                        response.setMessage("Lỗi xử lý");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void createAuthService() {
        authService = ApiUtil.getAuthService();
    }

    public void logout(String token, String username, final Callback<ResponseValue> callback) {
        authService.logout(token, username)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseValue>() {
                    @Override
                    public void onNext(ResponseValue response) {
                        callback.onGet(response, ResAccFlag.LOGOUT);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", AccountRequestManager.class.getSimpleName()
                                + ":findAccount():error:" + e.getMessage());
                        ResponseValue response = new ResponseValue();
                        response.setSuccess(false);
                        response.setMessage("Lỗi xử lý");
                        callback.onGet(response, ResAccFlag.LOGOUT);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
