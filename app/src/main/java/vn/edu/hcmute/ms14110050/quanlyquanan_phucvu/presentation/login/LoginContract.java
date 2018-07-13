package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login;

import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/14/2018.
 */

public interface LoginContract {
    interface View extends LifeCycle.View {

        void openHomeActivity(String username, int typeAccount);

        void onLoginFailed(int msgResId);

        void onLoginFailed(String message);

        void onLoginError(int msgResId);
    }
}
