package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.OnChangeNetworkStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.Callback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.RectangleImageTransform;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.ScaleType;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.OnChangeSocketStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.ChangePasswordRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.ResAccFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.account.AccountRequestManager;

/**
 * Created by Vo Ngoc Hanh on 6/15/2018.
 */

public class AccountViewModel extends BaseNetworkViewModel<AccountContract.View>
        implements GetCallback<User>, Callback<ResponseValue>{
    private AccountRequestManager requestManager;
    private User user;
    private String token;
    public final ObservableField<Drawable> profileDrawable = new ObservableField<>();
    private BindableFieldTarget profileTarget;

    public AccountViewModel() {
        createAccountRequestManager();
    }

    @Override
    public void onViewAttach(@NonNull AccountContract.View viewCallback) {
        super.onViewAttach(viewCallback);

        if (profileTarget == null) {
            profileTarget = new BindableFieldTarget(profileDrawable, getView().getContext().getResources());
        }
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestManager = null;
    }

    private void createAccountRequestManager() {
        if (requestManager == null) {
            requestManager = AccountRequestManager.getInstance();
        }
    }

    private void getToken() {
        if (token == null) {
            token = SSharedReference.getToken(getView().getContext());
        }
    }

    public void onSubmitSaveUserProfile(User data) {
        if (user != null && user.equals(data)) {
            if (isViewAttached()) {
                getView().notiNotChangeUserProfile();
            }
            return;
        }
        showProgress(R.string.message_processing);
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
//        Log.d("LOG", getClass().getSimpleName() + ":on update user profile");
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
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.max_size_profile_image);
        int corner = getContext().getResources().getDimensionPixelSize(R.dimen.normal_space);
        int bgColor = ContextCompat.getColor(getContext(), R.color.colorExtraLightGray);

        RectangleImageTransform transform =
                new RectangleImageTransform(size, size, corner, ScaleType.CENTER_INSIDE);
        transform.setBackgroundColor(bgColor);

        String url = StrUtil.getAbsoluteImgUrl(user.getUrlImgProfile());

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_account_120dp_0dp)
                .error(R.drawable.ic_account_120dp_0dp)
                .transform(transform).into(profileTarget);
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
        createAccountRequestManager();
        getToken();
        showProgress(R.string.msg_logout_ing);
        requestManager.logout(token, user.getUsername(), this);
    }

    private void onGetResponseLogout(ResponseValue response) {
        hideProgress();

        boolean isSuccess = response.isSuccess();

        if (isSuccess) {
            showMessage(R.string.msg_logout_success, Constant.COLOR_SUCCESS);
            SSharedReference.clearToken(getView().getContext());
            getView().onLogout();
        }else{
            Log.d("LOG", getClass().getSimpleName()
                    + ":logout():failed:message:" + response.getMessage()
                    + ":error:" + response.getError());

            String message = getString(R.string.msg_logout_failed, response.getMessage());
            showMessage(message, Constant.COLOR_ERROR);
        }
    }

    /*
    * VERIFY ACCOUNT
    * */
    public void onClickVerifyPassword(String password) {
        createAccountRequestManager();
        getToken();

        if (isViewAttached()) {
            getView().onCancelVerifyAccountDialog();
        }

        showProgress(R.string.message_processing);

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

    // Bấm vào nút HỦY trong hộp thoại XÁC THỰC TÀI KHOẢN
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
        getToken();

        if (isViewAttached()) {
            getView().onCancelInputNewPassword();
        }
        showProgress(R.string.message_processing);

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

    @Override
    public void onGet(ResponseValue response, int flag) {
        switch (flag) {
            case ResAccFlag.LOGOUT:
                onGetResponseLogout(response);
                break;
        }
    }

    /*
    * END INPUT NEW PASSWORD
    * */
}
