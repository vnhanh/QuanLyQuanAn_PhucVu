package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IViewHolder;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public interface IProgressView {
    void onToast(@StringRes int msgIdRes);

    void onToast(String message);

    void onShowMessage(String message, @ColorRes int colorTextIsRes);

    void onShowMessage(@StringRes int messageIdRes, @ColorRes int colorTextIsRes);

    void showProgress(@StringRes int idRes);

    void hideProgress();
}
