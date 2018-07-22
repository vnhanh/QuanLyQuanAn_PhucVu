package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderCheckable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.IOnCheckOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.recycler.ItemOrderAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IRecyclerAdapter;

public class WaiterOrdersConstributor implements TabLayout.OnTabSelectedListener, IOnCheckOrder {
    private IRecyclerAdapter<OrderCheckable> orderAdapter;
    private ArrayList<OrderCheckable> list = new ArrayList<>();
    private TabLayout tabLayout;
    private ArrayList<View> tabViews;
    private String username;

    // map id hóa đơn và trạng thái đã check hay chưa
    private HashMap<String, Boolean> checks = new HashMap<>();

    /*
    * Property
    * */

    public void setUsername(String username) {
        this.username = username;
        FLAG_LOADED_USER = true;

        if (orderAdapter != null) {
            if (orderAdapter instanceof ItemOrderAdapter) {
                ItemOrderAdapter adapter = (ItemOrderAdapter) orderAdapter;
                adapter.setUsername(username);
            }
        }

        onCheckShowData();
    }

    public void registerAdapter(@NonNull IRecyclerAdapter<OrderCheckable> orderAdapter) {
        this.orderAdapter = orderAdapter;
        if (orderAdapter instanceof ItemOrderAdapter) {
            ItemOrderAdapter adapter = (ItemOrderAdapter) orderAdapter;
            adapter.setOnCheckItemListener(this);

            if (FLAG_LOADED_USER) {
                adapter.setUsername(username);
            }
        }
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
        orderAdapter = null;
        tabViews = null;
    }

    /*
    * End
    * */

    /*
    * IOrderConstributor
    * */

    // lưu chỉ số các hóa đơn do phục vụ tạo ở mỗi tab trạng thái còn chưa mở
    private int[] counts = new int[5];

    private boolean FLAG_LOADED_LIST_ORDERS = false;
    private boolean FLAG_LOADED_USER = false;
    private ArrayList<Order> temp = new ArrayList<>();

    public void onGetList(ArrayList<Order> _list) {
        FLAG_LOADED_LIST_ORDERS = true;

        if (list == null) {
            list = new ArrayList<>();
        }else{
            this.list.clear();
        }
        orderAdapter.clearAll();
        counts = new int[5];
        if (checks == null) {
            checks = new HashMap<>();
        }

        temp = _list;

        onCheckShowData();
    }

    private void onCheckShowData() {
        if (FLAG_LOADED_USER && FLAG_LOADED_LIST_ORDERS) {
            onShowData();
        }
    }

    private void onShowData() {

        if (temp != null) {
            for (Order order : temp) {
                String orderID = order.getId();

                // vì có thể là load lại nên cần get checked status trước đó
                boolean isChecked = getCheckedStatus(orderID);
//                Log.d("LOG", getClass().getSimpleName() + ":onShowData():isChecked:" + isChecked);
                OrderCheckable item = new OrderCheckable(order, isChecked);

                HashMap<Integer, Integer> status = new HashMap<>();
                ArrayList<DetailOrder> details = order.getDetailOrders();

                boolean isAdded = false;
                for (DetailOrder detail : details) {
                    int _status = detail.getStatusFlag();
                    if (!status.containsKey(_status)) {
                        status.put(_status, 1);
                    }
                    if (!isAdded && _status == tabIndex + 1) {
                        orderAdapter.onAddItem(item);
                        isAdded = true;
                    }
                }

                String creater = order.getWaiterUsername();
                if (creater != null && creater.equals(username)) {
                    for (Map.Entry<Integer, Integer> entry : status.entrySet()) {
                        Integer key = entry.getKey();
                        if (key > 0) {
                            counts[key-1]++;
                        }
                    }
                }
                list.add(item);
            }
            showCountStatusOnTabLayout();
        }
    }

    private boolean getCheckedStatus(String id) {
        Boolean checked = checks.get(id);
        if (checked == null) {
            checks.put(id, false);
        }

        return checked != null && checked;
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

    private boolean isOutRange(int status) {
        return status == OrderFlag.CREATING || status == OrderFlag.COMPLETE;
    }

    // Nhận event update trạng thái hóa đơn
    public void onUpdateStatus(Order order, int oldStatus,
                               String detailOrderID, int oldDetailOrderStatus, int newDetailOrderStatus) {
        if (order == null) {
            return;
        }
        String orderID = order.getId();
        if (StrUtil.isEmpty(orderID)) {
            return;
        }

        int index = findItem(order.getId());
        int status = order.getStatusFlag();
        boolean isOutRange = isOutRange(status);

//        Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():tabIndex:" + tabIndex
//                + ":isOutRange:" + isOutRange);

        // tạo mới item ==> chưa được check
        OrderCheckable item = new OrderCheckable(order, false);

        if (index > -1) {
            HashMap<Integer, Integer> statusMap = new HashMap<>();

            // kiểm tra xem có chi tiết hóa đơn nào có trạng thái trùng với Tab
            boolean hasNoStatusCompatible = true;
            ArrayList<DetailOrder> details = order.getDetailOrders();
            int size = details.size();
            for (int i = 0; i < size; i++) {
                DetailOrder detail = details.get(i);
                int _status = detail.getStatusFlag();
                String _detailOrderID = detail.getId();

                if (detailOrderID.equals(_detailOrderID)) {
                    if (_status == tabIndex + 1) {
                        hasNoStatusCompatible = false;
                    }
                }

                if (!statusMap.containsKey(_status)) {
                    statusMap.put(_status, 1);
                }
            }

            // kiểm tra hóa đơn có đang show trên UI (recyclerview)
            boolean hasConstainedInShowed = false;
            ArrayList<OrderCheckable> showedOrders = orderAdapter.getList();
            int showedSize = showedOrders.size();

            for (int i = 0; i < showedSize; i++) {
                OrderCheckable _item = showedOrders.get(i);
                Order __order = _item.getOrder();
                if (__order != null) {
                    String _orderID = __order.getId();
                    if (orderID.equals(_orderID)) {
                        hasConstainedInShowed = true;
                        break;
                    }
                }
            }

            int listSize = list.size();
            for (int i = 0; i < listSize; i++) {
                Order _order = list.get(i).getOrder();
                if (_order.getId().equals(orderID)) {
                    ArrayList<DetailOrder> _details = _order.getDetailOrders();
                    for (DetailOrder detail : _details) {
                        int _status = detail.getStatusFlag();
                        if (statusMap.containsKey(_status)) {
                            statusMap.put(_status, 0);
                        }else{
                            statusMap.put(_status, -1);
                        }
                    }
                    break;
                }
            }

            // hóa đơn trước đó không show
            // nhưng trong các chi tiết hóa đơn có một số có trạng thái tương ứng với tab đang chọn
            if (!hasConstainedInShowed && !hasNoStatusCompatible) {
                orderAdapter.onAddItem(item);
            }
            // hoá đơn đang được show
            // nhưng sau khi update, không có chi tiết hóa đơn nào có trạng thái tương ứng với tab trạng thái đang chọn
            else if(hasConstainedInShowed && hasNoStatusCompatible){
                orderAdapter.onRemoveItem(orderID);
            }
            // hóa đơn đang được show
            // và sau khi được update thì có một số chi tiết hóa đơn có trạng thái tương ứng với tab trạng thái đang chọn
            else if (hasConstainedInShowed && !hasNoStatusCompatible) {
                orderAdapter.onUpdateItem(item);
            }

            // update TabLayout
            String creater = order.getWaiterUsername();
            if (creater != null && creater.equals(username)) {
//            Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():update tablayout");

                for (Map.Entry<Integer, Integer> entry : statusMap.entrySet()) {
                    int _status = entry.getKey();
                    int _index = entry.getValue();
                    counts[_status - 1] += _index;
                    if (_index != 0 && _status > 0 && _status <= counts.length) {
                        updateTab(_status - 1);
                    }
                }
            }

            if (status > 0 && status < OrderFlag.COMPLETE) {
                list.set(index, item);
            }else{
                list.remove(index);
            }
        }

        // kiểm tra trạng thái các chi tiết hóa đơn có trùng với tab đang được chọn
//        boolean hasConstainDetailCompatibleStatus = false;
//        // cờ đánh dấu các chi tiết hóa đơn trong hóa đơn vừa được update đều nằm ngoài range
//        boolean allOutRange = true;
//
//        ArrayList<DetailOrder> details = order.getDetailOrders();
//        for (DetailOrder detail : details) {
//            int _status = detail.getStatusFlag();
//            if (!isOutRange(_status)) {
//                allOutRange = false;
//            }
//            if (_status == tabIndex + 1) {
//                hasConstainDetailCompatibleStatus = true;
//                break;
//            }
//
//            if (statusMap.containsKey(_status)) {
//                Integer value = statusMap.get(_status);
//                // đánh dấu trạng thái này đã có
//                if (value > 0) {
//                    statusMap.put(_status, 0);
//                }
//            }
//            // trạng thái mới
//            else{
//                statusMap.put(_status, -1);
//            }
//        }
    }

    // Nhận event socket thay đổi thông tin hóa đơn
    public void onUpdateOrder(Order order) {
        if (order != null) {
            int index = findItem(order.getId());
            if (index > -1) {
                int status = order.getStatusFlag();
                if (status == tabIndex + 1) {
                    OrderCheckable item = list.get(index);
                    item.setOrder(order);
                    orderAdapter.onUpdateItem(item);
                }
            }
        }
    }

    public void onRemoveItem(String orderID) {
        if (orderAdapter != null) {
            orderAdapter.onRemoveItem(orderID);
        }

        int index = findItem(orderID);

        if (index > -1) {
            OrderCheckable item = list.get(index);

            Order order = item.getOrder();
            if (order != null) {
                int status = order.getStatusFlag();
                counts[status - 1]--;
                updateTab(status - 1);
            }

            list.remove(index);
            checks.remove(orderID);
        }
    }

    private int findItem(@NonNull String id) {
        if (id == null || id.equals("") || list == null || list.size() == 0) {
            return -1;
        }

        int size = list.size();
        for (int i = 0; i < size; i++) {
            Order order = list.get(i).getOrder();

            if (order != null && order.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onCheckOrder(String id) {
//        Log.d("LOG", getClass().getSimpleName() + ":onCheckOrder()");
        if (checks == null) {
            checks = new HashMap<>();
        }
        checks.put(id, true);

        int index = findItem(id);

        if (index > -1) {
            list.get(index).setCheck(true);
        }
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
        orderAdapter.clearAll();
        for (OrderCheckable item : list) {
            Order order = item.getOrder();
            if (order != null) {
                ArrayList<DetailOrder> details = order.getDetailOrders();
                for (DetailOrder detail : details) {
                    int _status = detail.getStatusFlag();
                    if (_status == tabIndex + 1) {
                        orderAdapter.onAddItem(item);
                        break;
                    }
                }
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
}
