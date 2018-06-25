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
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.ChangeNetworkStateContainer;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view.MyProgressDialog;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentAccountBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.HomeActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.dialog.InputNewPasswordDialog;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.dialog.VerifyAccountDialog;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.validation.AccountValidation;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login.LoginActivity;

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
        Toast.makeText(getContext(), getString(R.string.message_not_change_profile), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateProfileSuccess() {
        Toast.makeText(getContext(), getString(R.string.update_profile_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateProfileFailed() {
        Toast.makeText(getContext(), getString(R.string.update_profile_failed), Toast.LENGTH_SHORT).show();
    }

    AlertDialog progressDialog;

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = MyProgressDialog.create(getContext(), R.string.message_processing);
        }
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
        Toast.makeText(getContext(), getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getContext(), getString(R.string.update_password_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChangePasswordFailed() {
        Toast.makeText(getContext(), getString(R.string.update_password_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogout() {
        LoginActivity.startActivity(getActivity());
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
