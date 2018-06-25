package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.LayoutInputNewPasswordBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.AccountViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.validation.InputNewPasswordValidation;

/**
 * Created by Vo Ngoc Hanh on 6/16/2018.
 */

public class InputNewPasswordDialog {
    public static AlertDialog create(Context context, AccountViewModel viewModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LayoutInputNewPasswordBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.layout_input_new_password, null, false);
        builder.setView(binding.getRoot());
        binding.setPassword("");
        binding.setValidation(new InputNewPasswordValidation());
        binding.setViewmodel(viewModel);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.d("LOG", VerifyAccountDialog.class.getSimpleName() + ":onDismis()");
                binding.edtPassword.setText("");
                binding.edtConfirmPassword.setText("");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDownAnimation;
        return dialog;
    }
}
