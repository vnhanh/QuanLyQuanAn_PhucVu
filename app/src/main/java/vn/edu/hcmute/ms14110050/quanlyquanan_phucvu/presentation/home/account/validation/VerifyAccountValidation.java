package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.validation;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.PasswordValidator;

/**
 * Created by Vo Ngoc Hanh on 6/16/2018.
 */

public class VerifyAccountValidation extends BaseValidation {
    public PasswordValidator passwordValidator;

    public VerifyAccountValidation() {
        passwordValidator = new PasswordValidator();

        registerValidator(passwordValidator);
    }
}
