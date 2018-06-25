package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation;

import android.databinding.ObservableBoolean;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.TAG_VALIDATE;


/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public abstract class BaseValidation implements Observer{
    protected ArrayList<IValidator> validators = new ArrayList<>();
    public final ObservableBoolean valid = new ObservableBoolean(false);

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof Integer) {
            Integer tag = (Integer) o;
            if (tag == TAG_VALIDATE) {
                checkValid();
            }
        }
    }

    public void checkValid() {
        if (validators == null) {
            return;
        }
        boolean check = true;
        for (IValidator validator : validators) {
            if (!validator.getValid()) {
                check = false;
                break;
            }
        }
        valid.set(check);
    }

    public void registerValidator(IValidator validator) {
        if (validator instanceof Observable) {
            ((Observable) validator).addObserver(this);
        }
        validators.add(validator);

        validator.setValidatorReadyListener(new IValidator.IValidatorReadyListener() {
            @Override
            public void onValidatorReady() {
                checkValidatorsReady();
            }
        });
    }

    private void checkValidatorsReady() {
        for (IValidator validator : validators) {
            if (!validator.isReady()) {
                return;
            }
        }
        checkValid();
    }

    public void destroy() {
        if (validators == null) {
            return;
        }
        for (IValidator validator : validators) {
            validator.destroy();
        }
        validators.clear();
        validators = null;
    }
}
