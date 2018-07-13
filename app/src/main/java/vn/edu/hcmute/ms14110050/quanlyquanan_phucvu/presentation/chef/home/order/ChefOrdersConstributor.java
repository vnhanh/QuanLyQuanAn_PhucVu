package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IRecyclerAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;


public class ChefOrdersConstributor implements TabLayout.OnTabSelectedListener {
    private IRecyclerAdapter<Order> orderAdapter;
    private ArrayList<Order> list = new ArrayList<>();
    private TabLayout tabLayout;
    private ArrayList<View> tabViews;

    /*
    * Property
    * */

    public void registerAdapter(@NonNull IRecyclerAdapter<Order> orderAdapter) {
        this.orderAdapter = orderAdapter;
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
    private int count;

    public void onGetList(ArrayList<Order> _list) {
        if (list == null) {
            list = new ArrayList<>();
        }else{
            this.list.clear();
        }
        orderAdapter.clearAll();
        count = 0;

        if (_list != null) {
            for (Order order : _list) {

                int status = order.getStatusFlag();

                // add hóa đơn vào recyclerview nếu có trạng thái như tab layout đang chọn
                if (status == tabIndex + 1) {
                    orderAdapter.onAddItem(order);
                }

                // update số hóa đơn đang chờ
                if (status == OrderFlag.PENDING) {
                    count++;
                }

                list.add(order);
            }
            showIndicatorPendingTab();
        }
    }

    // Chỉ hiển thị con số hóa đơn ĐANG CHỜ
    private void showIndicatorPendingTab() {

        View tab = tabViews.get(0);

        TextView txtCount = tab.findViewById(R.id.txt_count);
        if (count > 0) {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(count));
        }else{
            txtCount.setVisibility(View.INVISIBLE);
        }

        tabLayout.getTabAt(0).setCustomView(tab);
    }

    public void onUpdateStatus(int oldStatus, Order order) {
        if (order == null) {
            return;
        }
        String orderID = order.getId();
        if (StringUtils.isEmpty(orderID)) {
            return;
        }

        int index = findItem(order.getId());
        int status = order.getStatusFlag();
        boolean isOutRange = status == OrderFlag.CREATING || status >= OrderFlag.EATING;

//        Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():tabIndex:" + tabIndex
//                + ":isOutRange:" + isOutRange);

        // update hiển thị UI
        if (oldStatus == tabIndex + 1) {
//            Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():remove item");

            // remove item trong recyclerview
            boolean res = orderAdapter.onRemoveItem(orderID);
        } else if (status == tabIndex + 1) {
//            Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():add item");
            orderAdapter.onAddItem(order);
        }

        // update số hóa đơn ĐANG CHỜ
        if (status == OrderFlag.PENDING) {
            count ++;
            showIndicatorPendingTab();
        }
        else if (oldStatus == OrderFlag.PENDING) {
            count--;
            showIndicatorPendingTab();
        }

        // update dữ liệu

        if (index == -1) {
            if (!isOutRange) {
//                Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():add item to list data");
                list.add(order);
            }
        }else{
            if (isOutRange) {
//                Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():remove item to list data");
                list.remove(index);
            }else{
//                Log.d("LOG", getClass().getSimpleName() + ":onUpdateStatus():update item to list data");
                list.set(index, order);
            }
        }
    }

    public void onRemoveItem(String orderID) {
        if (orderAdapter != null) {
            orderAdapter.onRemoveItem(orderID);
        }

        int index = findItem(orderID);

        if (index > -1) {
            Order order = list.get(index);
            int status = order.getStatusFlag();

            // update số hóa đơn ĐANG CHỜ
            if (status == OrderFlag.PENDING) {
                count--;
                showIndicatorPendingTab();
            }

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
        orderAdapter.clearAll();
        for (Order order : list) {
            if (order != null) {
                int status = order.getStatusFlag();
                if (status == tabIndex + 1) {
                    orderAdapter.onAddItem(order);
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
