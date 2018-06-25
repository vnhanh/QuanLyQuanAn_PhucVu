package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding;

import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.EditText;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.TextWatcherHandler;


/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class InjectView {

    @BindingAdapter("addTextWatcher")
    public static void addTextWatcher(EditText editText, TextWatcherHandler handler) {
        handler.setEditText(editText);
    }

    @BindingAdapter("addErrorViewer")
    public static void addErrorViewer(TextInputLayout textInputLayout, TextWatcherHandler handler) {
        handler.setTextInputLayout(textInputLayout);
    }
}
