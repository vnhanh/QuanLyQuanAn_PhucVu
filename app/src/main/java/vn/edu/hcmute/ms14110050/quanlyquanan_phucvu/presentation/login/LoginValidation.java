package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login;


import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.PasswordValidator;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.UsernameValidator;

/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class LoginValidation extends BaseValidation {
    public UsernameValidator usernameValidator;
    public PasswordValidator passwordValidator;

    public LoginValidation() {
        usernameValidator = new UsernameValidator();
        passwordValidator = new PasswordValidator();

        registerValidator(usernameValidator);
        registerValidator(passwordValidator);
    }

    @Override
    public void destroy() {
        usernameValidator.destroy();
        passwordValidator.destroy();
    }
}
