package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.ContainerViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentSelectFoodsBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.recycler.TableForSelectAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.spinner.CategoryFoodAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.spinner.CategoryFoodSpinnerInstaller;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.EXTRA_ORDER_ID;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class SelectFoodDialogFragment
        extends BaseNetworkDialogFragment<FragmentSelectFoodsBinding, IFoodView, SelectFoodViewModel>
        implements IFoodView {

    private IFoodVM containerVM;

    public static SelectFoodDialogFragment newInstance() {

        return new SelectFoodDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        initRegionSpinner();
        initRecyclerView();

        return new AlertDialog.Builder(getActivity())
                .setView(binding.getRoot())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

    private CategoryFoodAdapter catFoodAdapter;

    private void initRegionSpinner() {
        catFoodAdapter = CategoryFoodSpinnerInstaller.setup(getContext(), binding, viewModel);
    }

    // TODO
    private void initRecyclerView() {

    }

    /*
    * Abstract
    * */

    @Override
    protected SelectFoodViewModel initViewModel() {
        return new SelectFoodViewModel();
    }

    @Override
    protected FragmentSelectFoodsBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_select_foods, container, false);
    }

    @Override
    protected void onAttachViewModel() {
        // TODO : setup foodAdapter and set to viewmodel
        viewModel.setCatDataListener(catFoodAdapter);
//        viewModel.setFoodsDataListener();

        viewModel.onViewAttach(this);
    }

    /*
    * End Abstract
    * */

    /*
    * IFoodView
    * */

    @Override
    public void onLoadingCategories() {

    }

    @Override
    public void onEndLoadingCategories() {

    }

    @Override
    public void onLoadingFoods() {

    }

    @Override
    public void onEndLoadingFoods() {

    }

    /*
    * End IFoodView
    * */
}
