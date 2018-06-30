package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * Created by Vo Ngoc Hanh on 6/27/2018.
 */

public interface InputCallback {
    void setTextInputLayout(TextInputLayout til);

    void setEditText(EditText edt);

    void onChangeInput(String input);

    void onSubmit();
}
