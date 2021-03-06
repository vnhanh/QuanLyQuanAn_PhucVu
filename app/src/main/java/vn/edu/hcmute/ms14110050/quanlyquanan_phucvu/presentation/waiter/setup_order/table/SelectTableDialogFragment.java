package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table;


import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentSelectTableBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table.recycler.TableAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table.spinner.RegionAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table.spinner.SetupRegionSpinner;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.view_food.InputCallbackImpl;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectTableDialogFragment extends BaseNetworkDialogFragment<FragmentSelectTableBinding, ITableView, SelectTableViewModel> implements ITableView {
    private IOrderVM centerViewModel;

    public void setCenterViewModel(IOrderVM centerViewModel) {
        this.centerViewModel = centerViewModel;
    }

    public static SelectTableDialogFragment newInstance() {

        return new SelectTableDialogFragment();
    }

    public SelectTableDialogFragment() {
        // Required empty public constructor
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

    private RegionAdapter regionsAdapter;

    private void initRegionSpinner() {
        regionsAdapter = SetupRegionSpinner.setupSpinner(getContext(), binding, viewModel);
    }

    private TableAdapter tablesAdapter;

    private void initRecyclerView() {
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(manager);
        tablesAdapter = new TableAdapter(getActivity(), false);
        binding.recyclerview.setAdapter(tablesAdapter);
    }

    /*
    * BaseNetworkFragment
    * */

    @Override
    protected SelectTableViewModel initViewModel() {
        return new SelectTableViewModel();
    }

    @Override
    protected FragmentSelectTableBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_select_table, container, false);
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.setRegionDataListener(regionsAdapter);
        viewModel.setTableDataListener(tablesAdapter);
        viewModel.setCenterViewModel(centerViewModel);
        if (tablesAdapter != null) {
            tablesAdapter.setContainerVM(viewModel);
        }
        viewModel.onViewAttach(this);
    }

    /*
    * End BaseNetworkFragment
    * */

    /*
    * ITableView
    * */

    @Override
    public void onShowLoadRegionsProgress() {
        binding.spinnerRegion.setEnabled(false);
        binding.recyclerview.setEnabled(false);
    }

    @Override
    public void onHideLoadRegionsProgress() {
        binding.spinnerRegion.setEnabled(true);
        binding.recyclerview.setEnabled(true);
    }
    /*
    * End ITableView
    * */
}
