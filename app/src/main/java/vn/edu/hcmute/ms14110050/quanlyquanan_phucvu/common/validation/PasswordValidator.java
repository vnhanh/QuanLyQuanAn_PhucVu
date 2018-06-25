package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation;

import android.text.Editable;

import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable.SendObject;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.IntegerUnique;


/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class PasswordValidator extends TextWatcherHandler{
    public static final int TAG_PASSWORD_CHANGE = IntegerUnique.generate();

    public PasswordValidator() {
        this.errMsgId = R.string.validate_password;

        this.pattern = Pattern.compile(
                "^.{1,}$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        super.afterTextChanged(editable);

        SendObject newPasswordObject = new SendObject(TAG_PASSWORD_CHANGE, editable.toString());
        setChanged();
        notifyObservers(newPasswordObject);
    }
}
