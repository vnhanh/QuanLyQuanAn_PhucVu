package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.find_account;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.LayoutFindAccountBinding;

public class FindAccountDialog
        extends BaseNetworkDialogFragment<LayoutFindAccountBinding, FindAccountView, FindAccountViewModel>
        implements FindAccountView, DialogInterface.OnClickListener{

    public static FindAccountDialog newInstance(String username) {
        FindAccountDialog dialog = new FindAccountDialog();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            viewModel.setMyUserName(username);
        }
    }

    private GetCallback<String> callback;

    public void setCallback(GetCallback<String> callback) {
        this.callback = callback;
    }

    @Override
    protected FindAccountViewModel initViewModel() {
        return new FindAccountViewModel();
    }

    @Override
    protected LayoutFindAccountBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.layout_find_account, container, false);
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding.setViewmodel(viewModel);
        binding.setUsername("");
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // tạm thời
        return new AlertDialog.Builder(getActivity())
                .setView(binding.getRoot())
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.action_ok, this).create();
    }

    // Bấm nút OK
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (viewModel == null) {
            onToast(R.string.err_not_found_viewmodel);
        }
        else{

            String username = viewModel.getCurrentInput();

            if (!checkValid(username)) {
                onToast(R.string.msg_username_not_valid);
            }
            else{
                if (viewModel.isMyUserName()) {
                    onToast(R.string.err_your_username);
                }else{
                    callback.onFinish(username);
                }
            }
        }
    }

    private boolean checkValid(String username) {
        return !StringUtils.isEmpty(username);
    }
}
