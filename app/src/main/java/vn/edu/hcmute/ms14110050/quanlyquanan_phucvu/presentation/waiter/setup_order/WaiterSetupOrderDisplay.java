package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.StringRes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.ISetupOrder;

public class WaiterSetupOrderDisplay {
    public final ObservableField<String> id = new ObservableField<String>();
    public final ObservableField<String> creater = new ObservableField<String>();

    public final ObservableBoolean isInValidCreatedTime = new ObservableBoolean(false);
    public final ObservableField<String> createdDate = new ObservableField<String>();

    public final ObservableField<String> numberCustomer = new ObservableField<String>();
    public final ObservableField<String> orderStatus = new ObservableField<String>();
    public final ObservableField<String> totalCost = new ObservableField<String>();

    public final ObservableField<String> descriptionOrder = new ObservableField<String>();
    public final ObservableBoolean isShowDescription = new ObservableBoolean();
    public final ObservableBoolean isEmptyDescription = new ObservableBoolean();

    public ObservableBoolean isCreateOrder = new ObservableBoolean(true);

    private ISetupOrder.View view;
    private WaiterSetupOrderViewModel viewModel;
    private Order order;

    public void onStart(ISetupOrder.View view, WaiterSetupOrderViewModel viewModel, ObservableBoolean isCreateOrder) {
        this.view = view;
        this.viewModel = viewModel;
        this.isCreateOrder = isCreateOrder;
    }

    public void onStop() {
        view = null;
        viewModel = null;
    }

    protected String getString(@StringRes int idRes, Object... args) {
        return view != null ? view.getContext().getString(idRes, args) : "";
    }

    protected String getString(@StringRes int idRes) {
        return view != null ? view.getContext().getString(idRes) : "";
    }

    private Handler handler = new Handler();

    public void showInfo() {
        order = viewModel.getOrder();

        showOrderId();

        String _creater = getString(R.string.display_creater_order, order.getWaiterFullname());
        creater.set(_creater);

        showCreatedTime();

        showNumberCustomer(order);
        showStatusOrder();
        showFinalCost(order);
        showDescriptionOrder(order);

        showMenu();
    }

    private void showMenu() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view != null && viewModel != null && order != null) {
                    User waiter = viewModel.getWaiter();

                    String waiterUserName = order.getWaiterUsername();

                    boolean availableShow = waiterUserName != null
                            && waiterUserName.equals(waiter.getUsername());

                    String username = waiter.getUsername();

                    // lấy thông tin người được bàn giao cuối cùng (nếu có)
                    ArrayList<String> delegacies = order.getDelegacies();
                    String delegacy = "";
                    if (delegacies != null && delegacies.size() > 0) {
                        delegacy = delegacies.get(delegacies.size() - 1);
                    }

                    availableShow = availableShow || delegacy.equals(username);

                    boolean updateMenuResult =
                            view.onUpdateMenu(availableShow, isCreateOrder.get(), order.getStatusFlag());

                    if (!updateMenuResult) {
                        handler.postDelayed(this, 400);
                    }
                }
            }
        }, 500);
    }

    private void showOrderId() {
        String buff = StrUtil.isEmpty(order.getId()) ? getString(R.string.no_value) : order.getId();
        String content = getString(R.string.display_order_id, buff);
        id.set(content);
    }

    private void showCreatedTime() {
        String dateTimeStr = "";

        if (!isCreateOrder.get()) {
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

            SimpleDateFormat inputFormat =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

            try {
                Date date = inputFormat.parse(order.getCreatedTime());

                dateTimeStr = outputFormat.format(date);
                isInValidCreatedTime.set(false);
            } catch (ParseException e) {
                e.printStackTrace();
                dateTimeStr = " " + getString(R.string.not_determine);
                isInValidCreatedTime.set(true);
            }

            String dateTime = getString(R.string.content_created_date, dateTimeStr);
            createdDate.set(dateTime);
        }
    }

    public void showNumberCustomer(Order order) {
        numberCustomer.set(getString(R.string.display_number_customer, order.getCustomerNumber()));
    }

    private void showStatusOrder() {
        orderStatus.set(getString(R.string.display_status_order, getString(order.getStatusValue())));
    }

    public void showFinalCost(Order order) {
        totalCost.set(getString(R.string.display_total_cost_order, order.getFinalCost()));
    }

    public void showDescriptionOrder(Order order) {
        String description = order.getDescription();

        // không hiển thị ghi chú nếu đang xác nhận hoặc xem lại hóa đơn và ghi chú rỗng
        if (!isCreateOrder.get() && StrUtil.isEmpty(description)) {
            isShowDescription.set(false);
        }else{
            isShowDescription.set(true);
        }

        isEmptyDescription.set(StrUtil.isEmpty(order.getDescription()));

        String buff = isEmptyDescription.get() ? getString(R.string.no_value) : order.getDescription();
        descriptionOrder.set(getString(R.string.display_description_order, buff));
    }

}
