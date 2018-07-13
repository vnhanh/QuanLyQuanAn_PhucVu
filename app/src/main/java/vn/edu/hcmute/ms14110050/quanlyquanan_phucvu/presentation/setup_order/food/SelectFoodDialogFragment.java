package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentSelectFoodsBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview.FoodAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.spinner.CategoryFoodAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.spinner.CategoryFoodSpinnerInstaller;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class SelectFoodDialogFragment
        extends BaseNetworkDialogFragment<FragmentSelectFoodsBinding, IFoodView, SelectFoodViewModel>
        implements IFoodView {

    private IOrderVM centerVM;

    public void setCenterViewModel(IOrderVM centerViewModel) {
        this.centerVM = centerViewModel;
    }

    public static SelectFoodDialogFragment newInstance() {
        return new SelectFoodDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initRegionSpinner();
        initRecyclerView();

        return view;
    }

    private CategoryFoodAdapter catFoodAdapter;

    private void initRegionSpinner() {
        catFoodAdapter = CategoryFoodSpinnerInstaller.setup(getContext(), binding, viewModel);
    }

    private FoodAdapter foodAdapter;

    private void initRecyclerView() {
        final LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(manager);
        binding.recyclerview.setHasFixedSize(true);
        foodAdapter = new FoodAdapter(getActivity());
        binding.recyclerview.setAdapter(foodAdapter);
        foodAdapter.setContainerVM(viewModel);
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
        viewModel.setCatDataListener(catFoodAdapter);
        viewModel.setFoodsDataListener(foodAdapter);
        viewModel.setCenterViewModel(centerVM);
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
        binding.spinnerCategoryFoods.setEnabled(false);
        binding.recyclerview.setEnabled(false);
    }

    @Override
    public void onEndLoadingCategories() {
        binding.spinnerCategoryFoods.setEnabled(true);
        binding.recyclerview.setEnabled(true);
    }

    /*
    * End IFoodView
    * */
}
