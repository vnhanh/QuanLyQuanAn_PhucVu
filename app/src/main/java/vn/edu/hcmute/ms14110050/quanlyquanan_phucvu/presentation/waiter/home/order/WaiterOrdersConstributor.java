package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderCheckable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.IOnCheckOrder;
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

                int status = order.getStatusFlag();

                if (status == tabIndex + 1) {
                    orderAdapter.onAddItem(item);
                }
                String creater = order.getWaiterUsername();
                if (creater != null && creater.equals(username)) {
                    counts[status - 1]++;
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

    // Nhận event update trạng thái hóa đơn
    public void onUpdateStatus(int oldStatus, Order order) {
        if (order == null) {
            return;
        }
        String orderID = order.getId();
        if (StrUtil.isEmpty(orderID)) {
            return;
        }

        int index = findItem(order.getId());
        int status = order.getStatusFlag();
        boolean isOutRange = status == OrderFlag.CREATING || status == OrderFlag.COMPLETE;

//        Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():tabIndex:" + tabIndex
//                + ":isOutRange:" + isOutRange);

        // tạo mới item ==> chưa được check
        OrderCheckable item = new OrderCheckable(order, false);

        // update hiển thị UI
        if (oldStatus == tabIndex + 1) {
            // remove item trong recyclerview
            orderAdapter.onRemoveItem(orderID);
//            Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():remove item");
        } else if (status == tabIndex + 1) {
            orderAdapter.onAddItem(item);
//            Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():add item");
        }

        // update TabLayout
        String creater = order.getWaiterUsername();
        if (creater != null && creater.equals(username)) {
//            Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():update tablayout");

            if (!isOutRange) {
                counts[oldStatus - 1]--;
            }

            if (status <= counts.length) {
                counts[status - 1]++;
            }
            updateTab(oldStatus - 1);
            updateTab(status - 1);
        }

        // update dữ liệu
        if (index == -1) {
            if (!isOutRange) {
//                Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():add item to list data");
                list.add(item);
            }
        }else{
            if (isOutRange) {
//                Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():remove item to list data");
                list.remove(index);
            }else{
//                Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():update item to list data");
                list.set(index, item);
            }
        }
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
                int status = order.getStatusFlag();
                if (status == tabIndex + 1) {
                    orderAdapter.onAddItem(item);
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
