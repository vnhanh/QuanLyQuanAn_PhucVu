package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.RectangleImageTransform;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.ChangePasswordRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.account.AccountRequestManager;

/**
 * Created by Vo Ngoc Hanh on 6/15/2018.
 */

public class AccountViewModel extends BaseNetworkViewModel<AccountContract.View>
        implements GetCallback<User>{
    private AccountRequestManager requestManager;
    private User user;
    private String token;
    public final ObservableField<Drawable> profileDrawable = new ObservableField<>();
    private BindableFieldTarget profileTarget;

    @Override
    public void onViewAttach(@NonNull AccountContract.View viewCallback) {
        super.onViewAttach(viewCallback);

        createAccountRequestManager();

        profileTarget = new BindableFieldTarget(profileDrawable, getView().getContext().getResources());
    }

    @Override
    public void onViewDetached() {
        requestManager = null;
        super.onViewDetached();
    }

    private void createAccountRequestManager() {
        if (requestManager == null) {
            requestManager = AccountRequestManager.getInstance();
        }
    }

    public void onSubmitSaveUserProfile(User data) {
        if (user != null && user.equals(data)) {
            if (isViewAttached()) {
                getView().notiNotChangeUserProfile();
            }
            return;
        }
        showProgress();
        if (token == null) {
            token = SSharedReference.getToken(getView().getContext());
        }
        requestManager.update(token, data, new GetCallback<Boolean>() {
            @Override
            public void onFinish(Boolean success) {
                hideProgress();
                if (success) {
                    onUpdateProfileSuccess();
                }else{
                    onUpdateProfileFailed();
                }
            }
        });
    }

    private void onUpdateProfileSuccess() {
        if (isViewAttached()) {
            getView().onUpdateProfileSuccess();
        }
    }

    private void onUpdateProfileFailed() {
        if (isViewAttached()) {
            getView().onUpdateProfileFailed();
        }
    }

    public void onResetAction(View view) {
        updateUserProfileByClone();
    }

    public String getGenderStrValue() {
        if (isViewAttached() && user != null) {
            int index = user.isGender() ? 0 : 1;
            return getView().getContext().getResources().getStringArray(R.array.genders)[index];
        }
        return "";
    }

    /*
    * IMPLEMENT GetCallback<User>
    * */
    @Override
    public void onFinish(User user) {
        Log.d("LOG", getClass().getSimpleName() + ":on update user profile");
        this.user = user;
        if (user == null) {
            Log.d("LOG", getClass().getSimpleName() + ":on update user profile:user is null");
            if (isViewAttached()) {
                getView().onUpdateUserProfile(null);
            }
            return;
        }
        updateUserProfileByClone();
    }

    private void updateUserProfileByClone() {
        try {
            User clone = (User) user.clone();
            onUpdateProfileImage();
            if (isViewAttached()) {
                getView().onUpdateUserProfile(clone);
            }
            Log.d("LOG", getClass().getSimpleName() + ":on update user profile:user fullname:" + user.getFullname());
        } catch (CloneNotSupportedException e) {
            Log.d("LOG", getClass().getSimpleName() + ":on update user profile:clone:error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void onUpdateProfileImage() {
        Picasso.get()
                .load(user.getUrlImgProfile())
                .placeholder(R.drawable.ic_account_120dp_0dp)
                .error(R.drawable.ic_account_120dp_0dp)
                .transform(
                        new RectangleImageTransform(
                                getView().getContext().getResources(),
                                R.dimen.max_size_profile_image,
                                R.dimen.corner_profile_image)
                ).into(profileTarget);
    }
    /*
    * END
    * */

    /*
    * Onclick MenuItems
    * */

    public void onClickChangePasswordMenuItem() {
        if (isViewAttached()) {
            getView().showVerifyPasswordDialog();
        }

    }

    public void onClickLogoutMenuItem() {
        if (!isViewAttached()) {
            Log.d("LOG", getClass().getSimpleName() + ":onClickLogoutMenuItem():view is null");
            return;
        }
        SSharedReference.clearToken(getView().getContext());
        getView().onLogout();
    }

    /*
    * VERIFY ACCOUNT
    * */
    public void onClickVerifyPassword(String password) {
        createAccountRequestManager();

        if (token == null) {
            token = SSharedReference.getToken(getView().getContext());
        }

        if (isViewAttached()) {
            getView().onCancelVerifyAccountDialog();
        }
        showProgress();

        LoginRequest request = new LoginRequest();
        request.setUsername(user.getUsername());
        request.setPassword(password);
        request.setTypeUser(user.getTypeUser());
        requestManager.verifyAccount(token, request, new GetCallback<Boolean>() {
            @Override
            public void onFinish(Boolean result) {
                if (result) {
                    onVerifyPasswordSuccess();
                }else{
                    onVerifyPasswordFailed();
                }
            }
        });
    }

    // Bấm vào nút HỦY trong layout XÁC THỰC TÀI KHOẢN
    public void onClickCancelVerifyAccount() {
        if (isViewAttached()) {
            getView().onCancelVerifyAccountDialog();
        }
    }

    // Khi xác thực tài khoản thành công
    private void onVerifyPasswordSuccess() {
        hideProgress();
        if (isViewAttached()) {
            getView().showInputNewPassword();
        }
    }

    // Khi xác thực tài khoản thất bại
    private void onVerifyPasswordFailed() {
        hideProgress();
        if (isViewAttached()) {
            getView().onVerifyPasswordFailed();
        }
    }

    /*
    * END VERIFY ACCOUNT
    * */

    /*
    * INPUT NEW PASSWORD
    * */
    public void onClickSubmitNewPassword(String newPassword) {
        createAccountRequestManager();

        if (token == null) {
            token = SSharedReference.getToken(getView().getContext());
        }

        if (isViewAttached()) {
            getView().onCancelInputNewPassword();
        }
        showProgress();

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUsername(user.getUsername());
        request.setPassword(newPassword);

        requestManager.updatePassword(token, request, new GetCallback<Boolean>() {
            @Override
            public void onFinish(Boolean result) {
                hideProgress();
                if (result) {
                    onChangePasswordSuccess();
                }else{
                    onChangePasswordFailed();
                }
            }
        });

    }

    private void onChangePasswordSuccess() {
        if (isViewAttached()) {
            getView().onChangePasswordSuccess();
        }
    }

    private void onChangePasswordFailed() {
        if (isViewAttached()) {
            getView().onChangePasswordFailed();
        }
    }

    public void onClickCancelInputNewPassword() {
        if (isViewAttached()) {
            getView().onCancelInputNewPassword();
        }
    }

    /*
    * END INPUT NEW PASSWORD
    * */

    private void showProgress() {
        if (isViewAttached()) {
            getView().showProgress();
        }
    }

    private void hideProgress() {
        if (isViewAttached()) {
            getView().hideProgress();
        }
    }
}
