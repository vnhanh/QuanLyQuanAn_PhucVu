package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.LayoutVerifyAccountBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.AccountViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.validation.VerifyAccountValidation;

/**
 * Created by Vo Ngoc Hanh on 6/16/2018.
 */

public class VerifyAccountDialog {
    public static AlertDialog create(Context context, AccountViewModel viewModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final LayoutVerifyAccountBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.layout_verify_account, null, false);
        builder.setView(binding.getRoot());
        binding.setPassword("");
        binding.setValidation(new VerifyAccountValidation());
        binding.setViewmodel(viewModel);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.d("LOG", VerifyAccountDialog.class.getSimpleName() + ":onDismis()");
                binding.edtVerifyPassword.setText("");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.UpDownAnimation;
        return dialog;
    }
}
