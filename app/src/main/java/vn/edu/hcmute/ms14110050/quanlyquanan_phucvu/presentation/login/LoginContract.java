package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login;

import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/14/2018.
 */

public interface LoginContract {
    interface View extends LifeCycle.View {

        void showProgress();

        void showMessage(@StringRes int idRes);

        void openHomeActivity(String username);

        void onLoginFailed();

        void hideProgress();

        void onLoginError();

        void onDisconnectToServer();
    }
}
