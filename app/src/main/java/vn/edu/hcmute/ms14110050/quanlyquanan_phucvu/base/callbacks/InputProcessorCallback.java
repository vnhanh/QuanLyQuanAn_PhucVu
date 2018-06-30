package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * Created by Vo Ngoc Hanh on 6/30/2018.
 */

public interface InputProcessorCallback {
    void onChangeInputValue(TextInputLayout til, EditText edt, String input);

    void onSubmitInputProcessor();
}
