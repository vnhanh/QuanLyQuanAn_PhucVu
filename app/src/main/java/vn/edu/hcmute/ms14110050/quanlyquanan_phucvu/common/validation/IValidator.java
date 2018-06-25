package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation;

/**
 * Created by Vo Ngoc Hanh on 6/4/2018.
 */

public interface IValidator {
    boolean isReady();

    void setValidatorReadyListener(IValidatorReadyListener listener);

    boolean getValid();

    void destroy();

    interface IValidatorReadyListener {
        void onValidatorReady();
    }
}
