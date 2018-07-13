package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.OnChangeSocketStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.SocketManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentAccountBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.activity.HomeActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.dialog.InputNewPasswordDialog;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.dialog.VerifyAccountDialog;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.validation.AccountValidation;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login.LoginActivity;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_ERROR;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_SUCCESS;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_WARNING;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment
        extends BaseNetworkFragment<FragmentAccountBinding, AccountContract.View, AccountViewModel>
        implements AccountContract.View {


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    protected AccountViewModel initViewModel() {
        return new AccountViewModel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding.setValidation(new AccountValidation(getContext()));
        binding.setData(new User());

        return binding.getRoot();
    }


    @Override
    protected FragmentAccountBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.registerChangeUserProfileListener(viewModel);
        }
    }

    @Override
    public void onDestroy() {
        if (binding != null && binding.getValidation() != null) {
            binding.getValidation().destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (viewModel == null) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.change_password:
                viewModel.onClickChangePasswordMenuItem();
                return true;

            case R.id.logout:
                viewModel.onClickLogoutMenuItem();
                return true;
        }
        return false;
    }

    /*
    * IMPLEMENT ACCOUNTCONTRACT.VIEW
    * */

    @Override
    public void onUpdateUserProfile(User user) {
        binding.setData(user != null ? user : new User());
    }

    @Override
    public void notiNotChangeUserProfile() {
        onShowMessage(R.string.message_not_change_profile, COLOR_WARNING);
    }

    @Override
    public void onUpdateProfileSuccess() {
        onShowMessage(R.string.update_profile_success, COLOR_SUCCESS);
    }

    @Override
    public void onUpdateProfileFailed() {
        onShowMessage(R.string.update_profile_failed, COLOR_ERROR);
    }

    AlertDialog verifyAccountDialog;

    @Override
    public void showVerifyPasswordDialog() {
        if (verifyAccountDialog == null) {
            verifyAccountDialog = VerifyAccountDialog.create(getContext(), viewModel);
        }
        verifyAccountDialog.show();
    }

    @Override
    public void onCancelVerifyAccountDialog() {
        if (verifyAccountDialog != null && verifyAccountDialog.isShowing()) {
            verifyAccountDialog.dismiss();
        }
    }

    @Override
    public void onVerifyPasswordFailed() {
        onShowMessage(R.string.wrong_password, COLOR_ERROR);
    }

    AlertDialog inputNewPasswordDialog;

    @Override
    public void showInputNewPassword() {
        if (inputNewPasswordDialog == null) {
            inputNewPasswordDialog = InputNewPasswordDialog.create(getContext(), viewModel);
        }
        inputNewPasswordDialog.show();
    }

    @Override
    public void onCancelInputNewPassword() {
        if (inputNewPasswordDialog != null && inputNewPasswordDialog.isShowing()) {
            inputNewPasswordDialog.dismiss();
        }
    }

    @Override
    public void onChangePasswordSuccess() {
        onShowMessage(R.string.update_password_success, COLOR_SUCCESS);
    }

    @Override
    public void onChangePasswordFailed() {
        onShowMessage(R.string.update_password_failed, COLOR_ERROR);
    }

    @Override
    public void onLogout() {
        LoginActivity.startActivity(getActivity());
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
