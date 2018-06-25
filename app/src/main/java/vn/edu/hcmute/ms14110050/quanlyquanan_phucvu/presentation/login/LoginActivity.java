package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view.MyProgressDialog;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ActivityLoginBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.HomeActivity;

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
    public Context getContext() {
        return this;
    }

    AlertDialog progressDialog;

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = MyProgressDialog.create(this, R.string.message_logining);
        }
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(int idRes) {
        Toast.makeText(this, getString(idRes), Toast.LENGTH_SHORT).show();
    }

    // todo: đang test
    @Override
    public void openHomeActivity(String username) {
        HomeActivity.startActivity(this, username);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onLoginFailed() {
        showMessage(R.string.message_login_failed);
        binding.edtPassword.setText("");
        binding.edtPassword.requestFocus();
    }

    @Override
    public void onLoginError() {
        showMessage(R.string.message_login_failed);
    }

    @Override
    public void onDisconnectToServer() {
        showMessage(R.string.message_disconnect_server);
    }
}
