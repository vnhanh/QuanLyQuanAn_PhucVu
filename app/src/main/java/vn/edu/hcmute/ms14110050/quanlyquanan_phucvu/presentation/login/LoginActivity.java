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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onLoginFailed() {
        onShowMessage(R.string.message_login_failed, COLOR_ERROR);
        binding.edtPassword.setText("");
        binding.edtPassword.requestFocus();
    }

    @Override
    public void onLoginError() {
        onShowMessage(R.string.message_login_failed, COLOR_ERROR);
    }

    @Override
    public void onDisconnectToServer() {
        onShowMessage(R.string.message_disconnect_server, COLOR_ERROR);
    }
}
