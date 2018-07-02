package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/15/2018.
 */

public interface AccountContract {
    interface View extends LifeCycle.View {

        void onUpdateUserProfile(User user);

        void onUpdateProfileSuccess();

        void onUpdateProfileFailed();

        void notiNotChangeUserProfile();

        void showVerifyPasswordDialog();

        void onCancelVerifyAccountDialog();

        void onVerifyPasswordFailed();

        void showInputNewPassword();

        void onCancelInputNewPassword();

        void onChangePasswordSuccess();

        void onChangePasswordFailed();

        void onLogout();
    }
}
