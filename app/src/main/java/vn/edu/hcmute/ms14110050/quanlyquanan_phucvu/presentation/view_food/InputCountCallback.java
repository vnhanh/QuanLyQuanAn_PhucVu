package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputProcessorCallback;

/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public class InputCountCallback implements InputCallback {
    private WeakReference<InputProcessorCallback> processorRef;
    private WeakReference<TextInputLayout> tilRef;
    private WeakReference<EditText> edtRef;

    public InputCountCallback(InputProcessorCallback processor) {
        this.processorRef = new WeakReference<InputProcessorCallback>(processor);
    }

    @Override
    public void setTextInputLayout(TextInputLayout til){
        tilRef = new WeakReference<TextInputLayout>(til);
    }

    @Override
    public void setEditText(EditText editText) {
        edtRef = new WeakReference<EditText>(editText);
    }

    @Override
    public void onChangeInput(String input) {
        InputProcessorCallback processor = processorRef.get();
        TextInputLayout til = tilRef.get();
        EditText edt = edtRef.get();

        if (processor == null || til == null || edt == null) {
            return;
        }
        processor.onChangeInputValue(til, edt, input);
    }

    @Override
    public void onSubmit() {
        InputProcessorCallback processor = processorRef.get();
        if (processor != null) {
            processor.onSubmitInputProcessor();
        }
    }
}
