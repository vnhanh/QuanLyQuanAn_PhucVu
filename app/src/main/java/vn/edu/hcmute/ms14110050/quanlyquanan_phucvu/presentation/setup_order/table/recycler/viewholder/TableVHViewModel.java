package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.recycler.viewholder;

import android.databinding.ObservableBoolean;
import android.util.Log;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseVHViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.ITableVM;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class TableVHViewModel extends BaseVHViewModel<IViewHolder> {
    private Table table;
    public ObservableBoolean isSelected = new ObservableBoolean(false);
    public ObservableBoolean isCreatedOrder = new ObservableBoolean(false);

    private ITableVM containerVM;

    /*
    * Property
    * */

    public void setContainerViewModel(ITableVM containerViewModel) {
        this.containerVM = containerViewModel;
        isCreatedOrder.set(containerVM.isCreatedOrder());
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
            return;
        }

        if (!containerVM.isCreatedOrder()) {
            return;
        }

        if (isSelected.get()) {
            if (isViewAttached()) {
                getView().showProgress(R.string.message_removing_ordered_table);
            }
            containerVM.onRequestRemoveTableFromOrder(table.getId(), new GetCallback<TableResponse>() {
                @Override
                public void onFinish(TableResponse response) {
                    onGetRemoveTableResponseValue(response);
                }
            });
        }else{
            if (isViewAttached()) {
                getView().showProgress(R.string.message_setting_ordered_table);
            }
            Log.d("LOG", getClass().getSimpleName() + ":onClickItem():start add table to order");
            containerVM.onRequestAddTableToOrder(table, new GetCallback<TableResponse>() {
                @Override
                public void onFinish(TableResponse response) {
                    Log.d("LOG", getClass().getSimpleName() + ":onClickItem():get response");
                    onGetAddTableResponseValue(response);
                }
            });
        }
    }

    private void onGetAddTableResponseValue(TableResponse response) {
        hideProgress();

        if (!response.isSuccess()) {
            String errorMsg = getView().getContext().getString(R.string.add_table_to_order_failed);
            showErrorMessage(errorMsg, response.getMessage());
        }
    }

    private void onGetRemoveTableResponseValue(ResponseValue response) {
        hideProgress();
        if (!response.isSuccess()) {
            String errorMsg = getView().getContext().getString(R.string.remove_table_from_order_failed);
            showErrorMessage(errorMsg, response.getMessage());
        }
    }

    private void hideProgress() {
        if (isViewAttached()) {
            getView().hideProgress();
        }
    }

    private void showErrorMessage(String message, String messageResponse) {
        if (!StringUtils.isEmpty(messageResponse)) {
            message += ". " + messageResponse;
        }

        Toast.makeText(getView().getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
