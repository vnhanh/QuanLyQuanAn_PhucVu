package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview;

import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IViewHolder;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public interface IProgressVH extends IViewHolder {
    void onShowMessage(@StringRes int idRes);

    void showProgress(@StringRes int idRes);

    void hideProgress();
}
