package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.recycler.viewholder;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Typeface;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseVHViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderCheckable;

public class ItemOrderVM extends BaseVHViewModel<IItemOrderView> {
    public final ObservableInt createrStyle = new ObservableInt();
    public final ObservableField<String> contentCreater = new ObservableField<>();
    public final ObservableField<String> contentTables = new ObservableField<>();
    public final ObservableField<String> contentFinalCost = new ObservableField<>();
    public final ObservableField<String> contentStatus = new ObservableField<>();

    private OrderCheckable data;

    public Order getOrder() {
        return data != null ? data.getOrder() : null;
    }

    public OrderCheckable getData() {
        return data;
    }

    public void setData(OrderCheckable item, boolean isCreater) {
        this.data = item;
        Order order = item.getOrder();
        if (order == null) {
            createrStyle.set(Typeface.NORMAL);
            return;
        }
        createrStyle.set(isCreater ? Typeface.BOLD : Typeface.NORMAL);

        contentCreater.set(order.getWaiterFullname());

        StringBuilder tablesBuilder = new StringBuilder();
        int size = order.getTables().size();
        for (int i = 0; i < size; i++) {
            if (i < size - 1) {
                tablesBuilder.append(order.getTables().get(i) + ", ");
            }else{
                tablesBuilder.append(order.getTables().get(i));
            }
        }
        contentTables.set(tablesBuilder.toString());

        contentFinalCost.set(getString(R.string.content_final_cost, order.getFinalCost()));
        contentStatus.set(getString(order.getStatusValue()));
    }
}
