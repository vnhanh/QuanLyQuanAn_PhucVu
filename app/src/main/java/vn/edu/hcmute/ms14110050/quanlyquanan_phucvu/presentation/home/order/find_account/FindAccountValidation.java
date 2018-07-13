package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.find_account;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.BaseValidation;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation.UsernameValidator;

public class FindAccountValidation extends BaseValidation {
    public UsernameValidator validator =  new UsernameValidator();

    public FindAccountValidation() {

        registerValidator(validator);
    }

    @Override
    public void destroy() {
        super.destroy();
        validator.destroy();
    }
}
