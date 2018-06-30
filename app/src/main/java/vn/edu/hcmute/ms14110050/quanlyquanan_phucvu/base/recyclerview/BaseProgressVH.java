package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view.MyProgressDialog;

/**
 * Created by Vo Ngoc Hanh on 6/26/2018.
 */

public abstract class BaseProgressVH<DATABINDING extends ViewDataBinding, VIEWMODEL extends BaseVHViewModel>
        extends BaseViewHolder<DATABINDING, VIEWMODEL>
        implements IProgressVH {

    public BaseProgressVH(View view) {
        super(view);
    }

    public BaseProgressVH(DATABINDING binding) {
        super(binding);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onShowMessage(int strIdRes) {
        Toast.makeText(getContext(), getString(strIdRes), Toast.LENGTH_SHORT).show();
    }

    private AlertDialog progressDialog;

    @Override
    public void showProgress(@StringRes int idRes) {
        progressDialog = MyProgressDialog.create(getContext(), idRes);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
