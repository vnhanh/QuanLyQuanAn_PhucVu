package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.validation;

import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Observable;
import java.util.regex.Pattern;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.TAG_VALIDATE;


/**
 * Created by Vo Ngoc Hanh on 5/20/2018.
 */

public class TextWatcherHandler extends Observable implements TextWatcher, IValidator{

    EditText editText;
    TextInputLayout textInputLayout;
    protected Pattern pattern;

    protected int errMsgId = -1;
    protected String errMsg;

    protected boolean isValid = false;

    public TextWatcherHandler() {

    }

    public TextWatcherHandler(String reg, @StringRes int errMsgId) {
        this.errMsgId = errMsgId;
        if (!StringUtils.isEmpty(reg)) {
            pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        }
    }

    /*
    * IValidatior implement
    * */
    // Get valid hiện tại để Validation class xác định valid toàn cục của 1 layout
    @Override
    public boolean getValid() {
        checkValid(getContent());
        setError();

        return this.isValid;
    }
    /*
    * END
    * */

    public void setEditText(EditText editText) {
        this.editText = editText;
        editText.addTextChangedListener(this);
        if (errMsg == null) {
            try {
                errMsg = editText.getContext().getString(errMsgId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        checkSetViews();
    }

    public void setTextInputLayout(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
        checkSetViews();
    }

    private void checkSetViews() {
        if (isReady()) {
            if (listener != null) {
                listener.onValidatorReady();
            }
        }
    }

    @Override
    public boolean isReady() {
        return editText != null && textInputLayout != null;
    }

    private IValidatorReadyListener listener;

    @Override
    public void setValidatorReadyListener(IValidatorReadyListener listener) {
        this.listener = listener;
    }

    public String getContent() {
        return editText.getText().toString();
    }

    /*
    * TextWatcher implement
    * */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        runValid(editable.toString());
    }

    // Check valid của 1 edittext và set error (nếu có)
    private void runValid(String text) {
        if (pattern != null) {
            checkValid(text);

            setError();
            updateValid();
        }
    }

    protected void checkValid(String text) {
        isValid = pattern.matcher(text).matches();
    }

    /*
    * END
    * */

    protected void setError() {
        if (textInputLayout != null) {
            textInputLayout.setError(isValid ? "" : errMsg);
        } else if (editText != null) {
            editText.setError(isValid ? "" : errMsg);
        }
    }

    protected void updateValid() {
        setChanged();
        notifyObservers(TAG_VALIDATE);
    }

    @Override
    public void destroy() {
        textInputLayout = null;
        editText = null;
    }
}
