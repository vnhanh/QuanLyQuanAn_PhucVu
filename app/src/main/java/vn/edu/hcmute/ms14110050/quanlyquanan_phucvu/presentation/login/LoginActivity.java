package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ActivityLoginBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.activity.HomeActivity;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_ERROR;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginContract.View, LoginViewModel>
        implements LoginContract.View{

    public static void startActivity(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.setData(new LoginRequest());
        binding.setValidation(new LoginValidation());
    }

    @Override
    protected void onDestroy() {
        binding.getValidation().destroy();
        super.onDestroy();
    }

    @Override
    protected ActivityLoginBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    @Override
    protected LoginViewModel initViewModel() {
        return new LoginViewModel();
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

    @Override
    public void openHomeActivity(String username) {
        HomeActivity.startActivity(this, username);
        finish();
    }

    @Override
    public void onLoginFailed(int msgResId) {
        onLoginFailed(getString(msgResId));
    }

    @Override
    public void onLoginFailed(String message) {
        onShowMessage(message, COLOR_ERROR);
        binding.edtPassword.setText("");
        binding.edtPassword.requestFocus();
    }

    @Override
    public void onLoginError(int msgResId) {
        onShowMessage(msgResId, COLOR_ERROR);
        binding.edtUsername.requestFocus();
    }
}
