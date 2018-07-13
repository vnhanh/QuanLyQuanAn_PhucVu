package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.view_food;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;


/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public class InputOneTextDialogFragment extends DialogFragment {
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_HINT_INPUT = "EXTRA_HINT_INPUT";
    private static final String EXTRA_TYPE_INPUT = "EXTRA_TYPE_INPUT";
    private static final String EXTRA_INIT_TEXT = "EXTRA_INIT_TEXT";

    private InputCallback listener;

    private String title;
    private String inputHint;
    private int typeInput;
    private String initText;

    public void setListener(InputCallback listener) {
        this.listener = listener;
    }

    public static InputOneTextDialogFragment newInstance(String title, String inputHint, int typeInput, String initText) {
        InputOneTextDialogFragment fragment = new InputOneTextDialogFragment();

        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_TITLE, title);
        arguments.putString(EXTRA_HINT_INPUT, inputHint);
        arguments.putInt(EXTRA_TYPE_INPUT, typeInput);
        arguments.putString(EXTRA_INIT_TEXT, initText);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readIntent();

        if (title == null || inputHint == null) {
            Toast.makeText(getActivity(), getString(R.string.not_start_activity), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private void readIntent() {
        if (getArguments() != null) {
            title = getArguments().getString(EXTRA_TITLE);
            inputHint = getArguments().getString(EXTRA_HINT_INPUT);
            typeInput = getArguments().getInt(EXTRA_TYPE_INPUT);
            initText = getArguments().getString(EXTRA_INIT_TEXT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_input_count, null, false);

        builder.setView(view);

        TextView textView = view.findViewById(R.id.title);
        textView.setText(title);

        final TextInputLayout til = view.findViewById(R.id.til);

        final EditText editText = view.findViewById(R.id.edt);
        editText.setInputType(typeInput);
        editText.setHint(inputHint);

        listener.setTextInputLayout(til);
        listener.setEditText(editText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener != null) {
                    listener.onChangeInput(s.toString());
                }
            }
        });

        editText.setText(initText);
        editText.setSelection(editText.getText().toString().length());

        final AlertDialog dialog = builder.create();

        TextView btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSubmit();
                }
                dialog.dismiss();
            }
        });

        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onDestroy() {
        listener = null;
        super.onDestroy();
    }
}
