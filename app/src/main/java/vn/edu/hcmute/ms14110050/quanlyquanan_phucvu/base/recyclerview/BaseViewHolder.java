package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.BR;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.MessageRunnable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.ToastRunnable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view.MyProgressDialog;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public abstract class BaseViewHolder<DATABINDING extends ViewDataBinding, VIEWMODEL extends BaseVHViewModel>
                            extends RecyclerView.ViewHolder implements IViewHolder{

    protected DATABINDING binding;
    protected VIEWMODEL viewmodel;

    public BaseViewHolder(View view) {
        super(view);
    }

    public BaseViewHolder(DATABINDING binding) {
        super(binding.getRoot());

        this.binding = binding;
        viewmodel = initViewModel();
        binding.setVariable(BR.viewmodel, viewmodel);
        if (this instanceof IViewHolder) {
            viewmodel.attachView((IViewHolder) this);
        }
    }

    @Override
    public Context getContext() {
        return binding != null ? binding.getRoot().getContext() : null;
    }

    protected Resources getResources() {
        return binding != null ? binding.getRoot().getResources() : null;
    }

    protected String getString(@StringRes int strIdRes) {
        return binding != null ? binding.getRoot().getResources().getString(strIdRes) : "";
    }

    protected abstract VIEWMODEL initViewModel();

    @Override
    public void onToast(int msgIdRes) {
        if (getContext() instanceof Activity) {
            Log.d("LOG", getClass().getSimpleName() + ":onToast():context is activity instance");
            Activity activity = (Activity) getContext();
            activity.runOnUiThread(new ToastRunnable(activity, msgIdRes));
        }else{
            Log.d("LOG", getClass().getSimpleName() + ":onToast():context is not activity instance");
            Toast.makeText(getContext(), msgIdRes, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShowMessage(String message, int colorTextIsRes) {
        if (getContext() instanceof Activity) {
            Log.d("LOG", getClass().getSimpleName() + ":onShowMessage():context is activity instance");
            Activity activity = (Activity) getContext();
            activity.runOnUiThread(new MessageRunnable(getContext(), binding.getRoot(), message, colorTextIsRes));
        }else{
            Log.d("LOG", getClass().getSimpleName() + ":onShowMessage():context is not activity instance");
            try {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Toast.LENGTH_SHORT);
                View view = snackbar.getView();
                TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
                int textColor = ContextCompat.getColor(getContext(), colorTextIsRes);
                textView.setTextColor(textColor);
                snackbar.show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("LOG",
                        getClass().getSimpleName()
                                + ":onShowMessage():context is not Activity instance:catch exception:" + e.getMessage()
                );
            }
        }
    }

    @Override
    public void onShowMessage(int messageIdRes, int colorTextIsRes) {
        onShowMessage(getString(messageIdRes), colorTextIsRes);
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
