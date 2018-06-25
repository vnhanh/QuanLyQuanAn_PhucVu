package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.validation;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.ConfirmPasswordValidator;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.PasswordValidator;

/**
 * Created by Vo Ngoc Hanh on 6/16/2018.
 */

public class InputNewPasswordValidation extends BaseValidation {
    public PasswordValidator passwordValidator;
    public ConfirmPasswordValidator confirmPasswordValidator;

    public InputNewPasswordValidation() {
        passwordValidator = new PasswordValidator();
        confirmPasswordValidator = new ConfirmPasswordValidator();
        passwordValidator.addObserver(confirmPasswordValidator);
        registerValidator(passwordValidator);
        registerValidator(confirmPasswordValidator);
    }
}
