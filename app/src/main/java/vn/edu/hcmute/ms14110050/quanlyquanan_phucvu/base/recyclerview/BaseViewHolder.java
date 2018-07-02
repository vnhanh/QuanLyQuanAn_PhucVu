package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.BR;

/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public abstract class BaseViewHolder<DATABINDING extends ViewDataBinding, VIEWMODEL extends BaseVHViewModel>
                            extends RecyclerView.ViewHolder{

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

    protected Context getContext() {
        return binding != null ? binding.getRoot().getContext() : null;
    }

    protected Resources getResources() {
        return binding != null ? binding.getRoot().getResources() : null;
    }

    protected String getString(@StringRes int strIdRes) {
        return binding != null ? binding.getRoot().getResources().getString(strIdRes) : "";
    }

    protected abstract VIEWMODEL initViewModel();
}
