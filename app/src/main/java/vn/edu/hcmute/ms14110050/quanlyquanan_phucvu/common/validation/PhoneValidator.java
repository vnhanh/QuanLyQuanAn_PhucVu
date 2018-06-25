package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation;

import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;


/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public class PhoneValidator extends TextWatcherHandler {

    public PhoneValidator() {
        this.errMsgId = R.string.validate_phone;

        this.pattern = Pattern.compile(
                "^[0-9]{10,11}$");
    }

}
