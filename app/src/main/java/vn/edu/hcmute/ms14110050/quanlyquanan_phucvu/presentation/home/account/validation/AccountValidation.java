package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.validation;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.EmailValidator;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.IValidator;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.PhoneValidator;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.UsernameValidator;

/**
 * Created by Vo Ngoc Hanh on 6/15/2018.
 */

public class AccountValidation extends BaseValidation {
    Resources resources;

    public EmailValidator emailValidator = new EmailValidator();

    public PhoneValidator phoneValidator = new PhoneValidator();

    public AccountValidation(Context context) {
        this.resources = context.getResources();

        registerValidator(emailValidator);
        registerValidator(phoneValidator);
    }

    public void destroy() {
        resources = null;
        emailValidator.destroy();
        phoneValidator.destroy();
    }
}
