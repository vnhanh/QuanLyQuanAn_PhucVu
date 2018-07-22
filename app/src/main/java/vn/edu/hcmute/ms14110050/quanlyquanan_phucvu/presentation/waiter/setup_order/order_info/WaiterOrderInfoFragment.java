package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.order_info;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.WaiterFragmentInfoOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.WaiterSetupOrderDisplay;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.WaiterSetupOrderViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.detail_order.WaiterDetailOrderAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table.recycler.TableAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.view_food.InputOneTextDialogFragment;

public class WaiterOrderInfoFragment
        extends BaseNetworkFragment<WaiterFragmentInfoOrderBinding, IOrderInfoView, WaiterSetupOrderViewModel>
        implements IOrderInfoView{

    private WaiterSetupOrderViewModel viewmodel;

    public void setViewmodel(WaiterSetupOrderViewModel viewmodel) {
        this.viewmodel = viewmodel;
    }

    /*
    * Abstract
    * */

    @Override
    protected WaiterSetupOrderViewModel initViewModel() {
        return viewmodel;
    }

    @Override
    protected WaiterFragmentInfoOrderBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.waiter_fragment_info_order, container, false);
    }

    @Override
    protected void onAttachViewModel() {
        Log.d("LOG", getClass().getSimpleName()+":onAttach()");
        viewModel.setOrderInfoView(this);
        viewModel.onStart();
    }

    /*
    * End
    *
    * */

    /*
    * LifeCyle
    * */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        WaiterSetupOrderDisplay displayer = new WaiterSetupOrderDisplay();
        binding.setDisplayer(displayer);
        viewModel.setDisplayer(displayer);
        
        initRecyclerViews();

        return view;
    }

    private TableAdapter tablesAdapter;
    private WaiterDetailOrderAdapter detailOrderAdapter;

    private void initRecyclerViews() {
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.recyclerviewTables.setLayoutManager(manager);
        tablesAdapter = new TableAdapter(getActivity(), true);
        binding.recyclerviewTables.setAdapter(tablesAdapter);
        tablesAdapter.setContainerVM(viewModel);
        viewModel.setTableDataListener(tablesAdapter);

        LinearLayoutManager _manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        detailOrderAdapter = new WaiterDetailOrderAdapter(getActivity(), viewmodel);
        binding.recyclerviewFoods.setLayoutManager(_manager);
        binding.recyclerviewFoods.setAdapter(detailOrderAdapter);

        viewModel.setDetailOrderAdapter(detailOrderAdapter);
    }

    @Override
    protected void stop() {
//        Log.d("LOG", getClass().getSimpleName()+":stop");
    }

    @Override
    protected void destroy() {
//        Log.d("LOG", getClass().getSimpleName()+":destroy");
        binding = null;
    }

    /*
    * End
    * */

    /*
    * IView
    * */

    @Override
    public void onAnimationShowStatus() {
        binding.txtStatus.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
    }

    @Override
    public void onAnimationNumberCustomer() {
        binding.txtNumberCustomer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
    }

    @Override
    public void onAnimationFinalCost() {
        binding.txtTotalCost.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
    }

    @Override
    public void onAnimationDescriptionOrder() {
        binding.txtDescription.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
    }

    @Override
    public void openConfirmDialog(@StringRes int messageResId, final GetCallback<Void> callback) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_exit)
                .setMessage(messageResId)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callback.onFinish(null);
                    }
                }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public <Void> void openConfirmDialog(@StringRes int titleResId, @StringRes int messageResId,
                                         final GetCallback<Void> callback) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(titleResId)
                .setMessage(messageResId)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callback.onFinish(null);
                    }
                }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void openInputNumberCustomerView(int customerNumber, InputCallback callback) {
        removePrevFragDialog();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        InputOneTextDialogFragment _fragment  =
                InputOneTextDialogFragment.newInstance(
                        getString(R.string.title_input_number_customer),
                        getString(R.string.hint_number_customer),
                        InputType.TYPE_CLASS_NUMBER, String.valueOf(customerNumber));
        _fragment.setListener(callback);
        _fragment.show(ft, "dialog");
    }

    @Override
    public void openDescriptionDialog(String description, InputCallback listener) {
        removePrevFragDialog();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        InputOneTextDialogFragment _fragment  =
                InputOneTextDialogFragment.newInstance(
                        getString(R.string.title_input_description),
                        getString(R.string.hint_input_description),
                        InputType.TYPE_CLASS_TEXT, description);
        _fragment.setListener(listener);
        _fragment.show(ft, "dialog");
    }

    private void removePrevFragDialog() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment fragment = getChildFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            ft.remove(fragment);
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    /*
    * End
    * */
}
