package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.helper;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.RemoveDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.UpdateDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.socket_data.UpdateStatusDetailOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.IDetailOrderAdapter;


public class ChefOrdersConstributor implements TabLayout.OnTabSelectedListener {
    private IDetailOrderAdapter detailOrderAdapter;
    private ArrayList<Order> list = new ArrayList<>();
    private TabLayout tabLayout;
    private ArrayList<View> tabViews;

    /*
    * Property
    * */

    public void registerAdapter(@NonNull IDetailOrderAdapter orderAdapter) {
        this.detailOrderAdapter = orderAdapter;
    }

    public void setTabViews(@NonNull ArrayList<View> tabViews) {
        this.tabViews = tabViews;
    }

    public void registerTabLayout(@NonNull TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        tabLayout.addOnTabSelectedListener(this);
    }

    public void destroy() {
        tabLayout = null;
        detailOrderAdapter = null;
        tabViews = null;
    }

    /*
    * End
    * */

    /*
    * IOrderConstributor
    * */

    // lưu chỉ số các hóa đơn do phục vụ tạo ở mỗi tab trạng thái còn chưa mở
    private int[] counts;

    public void onGetList(ArrayList<Order> _list) {
        if (list == null) {
            list = new ArrayList<>();
        }else{
            this.list.clear();
        }
        detailOrderAdapter.clearAll();
        counts = new int[3];

        if (_list != null) {
            for (Order order : _list) {
                // add hóa đơn vào recyclerview nếu có trạng thái như tab layout đang chọn
                ArrayList<DetailOrder> details = order.getDetailOrders();

                for (DetailOrder detailOrder : details) {
                    int status = detailOrder.getStatusFlag();
                    if (status == tabIndex + 1) {
                        detailOrderAdapter.onAddItem(detailOrder);
                    }
                    if (status > 0 && status <= counts.length) {
                        counts[status-1]++;
                    }
                }

                list.add(order);
            }
            showCountStatusOnTabLayout();
        }
    }

    private void showCountStatusOnTabLayout() {
        for (int i = 0; i < counts.length; i++) {
            updateTab(i);
        }
    }

    private void updateTab(int _tabIndex) {
        View tab = tabViews.get(_tabIndex);
        TextView txtCount = tab.findViewById(R.id.txt_count);

        if (counts[_tabIndex] > 0) {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(counts[_tabIndex]));
        }
        else{
            txtCount.setVisibility(View.INVISIBLE);
        }

        tabLayout.getTabAt(_tabIndex).setCustomView(tab);
    }

    public void onUpdateStatus(UpdateStatusOrderResponse data) {
        if (data == null) {
            return;
        }
        Order order = data.getOrder();
        int oldStatus = data.getOldStatus();

        String detailOrderID = data.getDetailOrderID();
        if (order == null) {
            return;
        }

        String orderID = order.getId();
        if (StrUtil.isEmpty(orderID)) {
            return;
        }

        int index = findItem(order.getId());
        int oldDetailOrderStatus = data.getOldDetailOrderStatus();
        int newDetailOrderStatus = data.getNewDetailOrderStatus();

        int status = order.getStatusFlag();
        if (status >= OrderFlag.PAYING) {
            for (Order _order : list) {
                if (orderID.equals(_order.getId())) {
                    ArrayList<DetailOrder> detailOrders = _order.getDetailOrders();
                    int size = detailOrders.size();
                    for (int i = 0; i < size; i++) {
                        DetailOrder _detail = detailOrders.get(i);
                        int _status = _detail.getStatusFlag();
                        if (_status > 0 && _status <= counts.length) {
                            counts[_status - 1]--;
                        }
                    }
                }
            }
            showCountStatusOnTabLayout();
            list.remove(index);
            detailOrderAdapter.onRemoveDetails(orderID);
            return;
        }

//        Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():tabIndex:" + tabIndex
//                + ":isOutRange:" + isOutRange);

        // update hiển thị UI

        if (oldDetailOrderStatus == tabIndex + 1) {
            ArrayList<DetailOrder> details = order.getDetailOrders();
            for (DetailOrder detail : details) {
                String _detailOrderID = detail.getId();
                if (detailOrderID.equals(_detailOrderID)) {
                    detailOrderAdapter.onRemoveItem(detailOrderID);
                }
            }
        }
        else if(newDetailOrderStatus == tabIndex +1){
            ArrayList<DetailOrder> details = order.getDetailOrders();
            for (DetailOrder detail : details) {
                String _detailOrderID = detail.getId();
                if (detailOrderID.equals(_detailOrderID)) {
                    detailOrderAdapter.onAddItem(detail);
                }
            }
        }

        // update Tab
        if (oldDetailOrderStatus > 0 && oldDetailOrderStatus <= counts.length) {
            counts[oldDetailOrderStatus - 1]--;
            updateTab(oldDetailOrderStatus - 1);
        }
        if (newDetailOrderStatus > 0 && newDetailOrderStatus <= counts.length) {
            counts[newDetailOrderStatus -1]++;
            updateTab(newDetailOrderStatus - 1);
        }

        // update dữ liệu

        if (index == -1) {
            list.add(order);
        }else{
            list.set(index, order);
        }
    }

    private void addDetailOrders(Order order) {
        ArrayList<DetailOrder> details = order.getDetailOrders();

        for (DetailOrder detailOrder : details) {
            int status = detailOrder.getStatusFlag();
            if (status == tabIndex + 1) {
                detailOrderAdapter.onAddItem(detailOrder);
            }
        }
    }

    public void onRemoveItem(String orderID) {
        if (detailOrderAdapter != null) {
            detailOrderAdapter.onRemoveDetails(orderID);
        }

        int index = findItem(orderID);

        if (index > -1) {
            Order order = list.get(index);
            int status = order.getStatusFlag();

            // update số hóa đơn ĐANG CHỜ
            ArrayList<DetailOrder> details = order.getDetailOrders();
            int size = details.size();
            for (int i = 0; i < size; i++) {
                DetailOrder _detail = details.get(i);
                int _status = _detail.getStatusFlag();
                if (_status > 0 && _status <= counts.length) {
                    counts[_status - 1]--;
                }
            }
            showCountStatusOnTabLayout();

            list.remove(index);
        }
    }

    private int findItem(@NonNull String id) {
        if (id == null || id.equals("") || list == null || list.size() == 0) {
            return -1;
        }

        int size = list.size();
        for (int i = 0; i < size; i++) {
            Order order = list.get(i);

            if (order != null && order.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    /*
    * End
    * */

    /*
    * TabLayout Select Listener
    * */
    private int tabIndex = 0;

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tabIndex = tab.getPosition();

        // load các order theo chỉ số tabIndex(tương ứng với statusFlag)
        detailOrderAdapter.clearAll();
        for (Order order : list) {
            if (order != null) {
                addDetailOrders(order);
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /*
     * End
     * */

    public void onUpdateDetailOrderStatus(Order order, String detailOrderID, int oldDetailStatus, int newDetailStatus){
        if (order == null || order.getId() == null || detailOrderID == null) {
            Log.d("LOG", getClass().getSimpleName() + ":onUpdateDetailOrderStatus():param is null");
            return;
        }

        if (oldDetailStatus == newDetailStatus) {
            Log.d("LOG", getClass().getSimpleName() + ":onUpdateDetailOrderStatus():status not changed");
            return;
        }

        if (list == null) {
            list = new ArrayList<>();
        }

        String orderID = order.getId();
        int newStatus = order.getStatusFlag();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Order _order = list.get(i);
            String _orderID = _order.getId();

            if (orderID.equals(_orderID)) {
                ArrayList<DetailOrder> details = order.getDetailOrders();
                for(DetailOrder detail : details){
                    String _id = detail.getId();
                    if (detailOrderID.equals(_id)) {
                        if (newDetailStatus == tabIndex + 1) {
                            detailOrderAdapter.onAddItem(detail);
                        }
                        else if(oldDetailStatus == tabIndex +1){
                            detailOrderAdapter.onRemoveItem(detailOrderID);
                        }
                        break;
                    }
                }
                list.set(i, order);

                break;
            }
        }

        for (Order _order : list) {
            String _orderID = _order.getId();
            if (orderID.equals(_orderID)) {
                // update UI
                int oldStatus = _order.getStatusFlag();
                if (oldStatus != newStatus) {

                }
            }
        }
    }

    // Chi tiết hóa đơn bị hủy
    public void onDetailOrderRemoved(RemoveDetailOrderSocketData data) {
        if (data == null) {
            return;
        }
        String orderID = data.getOrderID();
        String detailOrderID = data.getDetailOrderID();
        if (orderID == null || detailOrderID == null) {
            return;
        }
        int index = findItem(orderID);
        if (index > -1) {
            detailOrderAdapter.onRemoveItem(detailOrderID);

            Order order = list.get(index);
            ArrayList<DetailOrder> details = order.getDetailOrders();
            int size = details.size();

            for (int i = 0; i < size; i++) {
                DetailOrder detail = details.get(i);
                if (detailOrderID.equals(detail.getId())) {
                    int _status = detail.getStatusFlag();
                    details.remove(i);
                    counts[_status -1]--;
                    updateTab(_status - 1);
                    break;
                }
            }
        }
    }

    public void onStatusDetailOrderUpdated(UpdateStatusDetailOrderSocketData data) {
        if (data == null) {
            return;
        }
        Order order = data.getOrder();
        DetailOrder detailOrder = data.getDetailOrder();
        int oldDetailOrderStatus = data.getOldDetailOrderStatus();
        String detailOrderID = detailOrder.getId();
        int newStatus = detailOrder.getStatusFlag();

        int index = findItem(order.getId());
        if (index > -1) {
            Order _order = list.get(index);

            ArrayList<DetailOrder> details = _order.getDetailOrders();
            int size = details.size();
            int i = 0;
            for (; i < size; i++) {
                DetailOrder detail = details.get(i);
                String _detailOrderId = detail.getId();
                if (detailOrderID.equals(_detailOrderId)) {
                    detail.setStatusFlag(newStatus);
                    break;
                }
            }

            if (i == size) {
                details.add(detailOrder);
                _order.setDetailOrders(details);
                list.set(index, _order);
            }
        }else{
            list.add(order);
        }

        // update recyclerview
        if (oldDetailOrderStatus == tabIndex + 1) {
            detailOrderAdapter.onRemoveItem(detailOrderID);
        }
        if (newStatus == tabIndex + 1) {
            detailOrderAdapter.onAddItem(detailOrder);
        }

        // update TabLayout
        if (oldDetailOrderStatus > 0) {
            counts[oldDetailOrderStatus -1]--;
            updateTab(oldDetailOrderStatus - 1);
        }
        if (newStatus > 0) {
            counts[newStatus - 1]++;
            updateTab(newStatus - 1);
        }
    }

    public void onDetailOrderUpdated(UpdateDetailOrderSocketData data) {
        if (data == null || data.getOrderID() == null || data.getDetailOrder() == null) {
            return;
        }
        String orderID = data.getOrderID();
        String detailOrderID = data.getDetailOrder().getId();
        DetailOrder newDetail = data.getDetailOrder();
        int status = newDetail.getStatusFlag();

        int index = findItem(orderID);
        if (index > -1) {
            Order order = list.get(index);
            ArrayList<DetailOrder> details = order.getDetailOrders();
            int size = details.size();
            for (int i = 0; i < size; i++) {
                DetailOrder detail = details.get(i);
                String _detailOrderID = detail.getId();
                if (detailOrderID.equals(_detailOrderID)) {
                    if (status == tabIndex + 1) {
                        detailOrderAdapter.onUpdateItem(newDetail);
                    }
                    details.set(i, newDetail);
                    break;
                }
            }
        }
    }
}
