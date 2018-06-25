package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.recycler.viewholder;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.ContainerViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseVHViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IProgressVH;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.table.RegionTableRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.ITableVM;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class TableVHViewModel extends BaseVHViewModel<IProgressVH> {
    private Table table;
    public ObservableBoolean isSelected = new ObservableBoolean(false);
    private ITableVM containerVM;

    /*
    * Property
    * */

    public void setContainerViewModel(ITableVM containerViewModel) {
        this.containerVM = containerViewModel;

    }

    public void setTable(Table table) {
        this.table = table;

        String orderID = getOrderID();
        boolean isSelect = !StringUtils.isEmpty(orderID) && orderID.equals(table.getOrderID());
        isSelected.set(isSelect);
    }

    private String getOrderID() {
        return containerVM != null ? containerVM.getOrderID() : "";
    }

    /*
    * End Property
    * */

    // Khi người dùng bấm vào item
    public void onClickItem() {
        if (containerVM == null || table == null || getOrderID() == null) {
            Log.d("LOG", getClass().getSimpleName()
                    + ":onCLickItem():error:containerVM is:" + containerVM + ":table is:" + table
                    + ":orderID:" + getOrderID());
            if (isViewAttached()) {
                getView().onShowMessage(R.string.found_error);
            }
            return;
        }

        if (isSelected.get()) {
            if (isViewAttached()) {
                getView().showProgress(R.string.message_removing_ordered_table);
            }
            containerVM.onRequestRemoveTableFromOrder(table.getId(), new GetCallback<TableResponse>() {
                @Override
                public void onFinish(TableResponse response) {
                    onGetResponseValue(response);
                }
            });
        }else{
            if (isViewAttached()) {
                getView().showProgress(R.string.message_setting_ordered_table);
            }
            containerVM.onRequestAddTableToOrder(table, new GetCallback<TableResponse>() {
                @Override
                public void onFinish(TableResponse response) {
                    onGetResponseValue(response);
                }
            });
        }
    }

    private void onGetResponseValue(ResponseValue response) {
        hideProgress();
        showErrorMessageIfExist(response);
    }

    private void hideProgress() {
        if (isViewAttached()) {
            getView().hideProgress();
        }
    }

    private void showErrorMessageIfExist(ResponseValue response) {
        if (!response.getSuccess()) {
            String errorMsg = "";
            if (StringUtils.isEmpty(response.getMessage())) {
                errorMsg = getView().getContext().getString(R.string.message_process_error);
            }else{
                errorMsg = response.getMessage();
            }

            Toast.makeText(getView().getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
