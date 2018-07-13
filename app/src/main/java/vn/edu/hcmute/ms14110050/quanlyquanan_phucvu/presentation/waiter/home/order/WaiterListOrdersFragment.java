package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order;


import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.ActivityUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.WaiterFragmentListOrdersBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.DelegacyResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.SuggestDelegacy;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.activity.HomeActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.abstracts.WaiterListOrdersContract;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.find_account.FindAccountDialog;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.home.order.recycler.ItemOrderAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.contract.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.WaiterSetupOrderActivity;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_ERROR;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaiterListOrdersFragment
        extends BaseNetworkFragment<WaiterFragmentListOrdersBinding, WaiterListOrdersContract.View, WaiterListOrdersViewModel>
        implements WaiterListOrdersContract.View, DialogInterface.OnClickListener{

    private ItemOrderAdapter adapter;

    public WaiterListOrdersFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_lst_orders, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.hand_over) {
            onShowFindUserDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected WaiterListOrdersViewModel initViewModel() {
        return new WaiterListOrdersViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initRecyclerView();
        initTabLayout();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.registerChangeUserProfileListener(viewModel);
        }
    }

    @Override
    public void onDestroy() {
        adapter.destroy();
        itemOrderListener.destroy();
        super.onDestroy();
    }

    @Override
    protected WaiterFragmentListOrdersBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.waiter_fragment_list_orders, container, false);
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

    private void initRecyclerView() {
        if (itemOrderListener == null) {
            itemOrderListener = new OnClickItemOrderListener(getActivity(), viewModel);
        }
        adapter = new ItemOrderAdapter(getActivity(), itemOrderListener);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(manager);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);
        viewModel.setOrderAdapter(adapter);
    }

    private void initTabLayout() {
        View tab01 = LayoutInflater.from(getContext()).inflate(R.layout.tab_item_home, null);
        TextView txtStatus01 = tab01.findViewById(R.id.txt_status);
        txtStatus01.setText(R.string.title_status_pending);

        View tab02 = LayoutInflater.from(getContext()).inflate(R.layout.tab_item_home, null);
        TextView txtStatus02 = tab02.findViewById(R.id.txt_status);
        txtStatus02.setText(R.string.title_status_cooking);

        View tab03 = LayoutInflater.from(getContext()).inflate(R.layout.tab_item_home, null);
        TextView txtStatus03 = tab03.findViewById(R.id.txt_status);
        txtStatus03.setText(R.string.title_status_prepare);

        View tab04 = LayoutInflater.from(getContext()).inflate(R.layout.tab_item_home, null);
        TextView txtStatus04 = tab04.findViewById(R.id.txt_status);
        txtStatus04.setText(R.string.title_status_eating);

        View tab05 = LayoutInflater.from(getContext()).inflate(R.layout.tab_item_home, null);
        TextView txtStatus05 = tab05.findViewById(R.id.txt_status);
        txtStatus05.setText(R.string.title_status_paying);

        TabLayout tabLayout = binding.tabLayout;
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setCustomView(tab01);
        tabLayout.getTabAt(1).setCustomView(tab02);
        tabLayout.getTabAt(2).setCustomView(tab03);
        tabLayout.getTabAt(3).setCustomView(tab04);
        tabLayout.getTabAt(4).setCustomView(tab05);

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

        WaiterOrdersConstributor constributor = viewModel.getOrderConstributor();
        constributor.registerTabLayout(binding.tabLayout);
        ArrayList<View> customTabs = new ArrayList<>();
        customTabs.add(tab01);
        customTabs.add(tab02);
        customTabs.add(tab03);
        customTabs.add(tab04);
        customTabs.add(tab05);
        constributor.setTabViews(customTabs);
    }

    private OnClickItemOrderListener itemOrderListener;

    public static class OnClickItemOrderListener implements View.OnClickListener {
        private Activity activity;
        private WaiterListOrdersViewModel viewModel;

        public OnClickItemOrderListener(Activity activity, WaiterListOrdersViewModel viewModel) {
            this.activity = activity;
            this.viewModel = viewModel;
        }

        @Override
        public void onClick(View v) {
            String orderID = (String) v.getTag(v.getId());
            WaiterSetupOrderActivity.startActivity(activity, viewModel.getUser(), OrderMode.VIEW, orderID);
        }

        public void destroy() {
            activity = null;
            viewModel = null;
        }
    };

    /*
    * WaiterListOrdersContract.View
    * */

    @Override
    public void openAddOrderScreen() {
        if (getActivity() == null) {
            Log.d("LOG", getClass().getSimpleName() + ":openAddOrderScreen():failed:getActivity() is null");
            onShowMessage(R.string.message_process_error, COLOR_ERROR);
            return;
        }
        WaiterSetupOrderActivity.startActivity(getActivity(), viewModel.getUser(), OrderMode.CREATE, null);
    }

    private static class ShowConfirmDelegacyDialogRunnable implements Runnable{
        private WeakReference<Activity> weakActivity;
        private WeakReference<DialogInterface.OnClickListener> weakDialogListener;
        private SuggestDelegacy suggest;

        public ShowConfirmDelegacyDialogRunnable(Activity activity,
                                                 DialogInterface.OnClickListener listener,
                                                 SuggestDelegacy suggest) {

            this.weakActivity = new WeakReference<>(activity);
            this.weakDialogListener = new WeakReference<>(listener);
            this.suggest = suggest;
        }

        @Override
        public void run() {
            Activity activity = weakActivity.get();
            DialogInterface.OnClickListener listener = weakDialogListener.get();

            if (activity != null && listener != null) {
                String message = activity.getString(R.string.msg_noti_suggest_delegacy,
                        suggest.getHandoverFullName());

                AlertDialog confirmDelegacyDialog = new AlertDialog.Builder(activity)
                        .setTitle(R.string.title_confirm_delegacy)
                        .setMessage(message)
                        .setPositiveButton(R.string.action_yes, listener)
                        .setNegativeButton(R.string.action_no, listener).create();

                confirmDelegacyDialog.show();
            }
        }
    }

    @Override
    public void onShowConfirmSuggestDelegacy(SuggestDelegacy suggest) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new ShowConfirmDelegacyDialogRunnable(getActivity(),this,suggest));
        }
    }

    // Khi người dùng bấm các nút trên hộp thoại XÁC NHẬN ĐỀ NGHỊ BÀN GIAO
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (viewModel != null) {
                    viewModel.onAcceptHandOver();
                }
                dialog.dismiss();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                if (viewModel != null) {
                    viewModel.onDisagreeHandOver();
                }
                dialog.dismiss();
                break;
        }
    }

    private static class ShowResponseDelegacyDialogRunnable implements Runnable{
        private WeakReference<Activity> weakActivity;
        private DelegacyResponse response;

        public ShowResponseDelegacyDialogRunnable(Activity activity, DelegacyResponse response) {

            this.weakActivity = new WeakReference<>(activity);
            this.response = response;
        }

        @Override
        public void run() {
            Activity activity = weakActivity.get();

            if (activity != null) {
                String message = "";

                if (response.isOk()) {
                    String fails = response.getFails();

                    if (StringUtils.isEmpty(fails)) {
                        message = activity.getString(R.string.msg_noti_response_delegacy_accept, response.getDelegacyFullName());
                    }
                    else{
                        message = activity.getString(
                                R.string.msg_noti_response_delegacy_accept_failed,
                                response.getDelegacyFullName(),
                                fails
                        );
                    }
                }
                else{
                    message = activity.getString(
                            R.string.msg_noti_response_delegacy_refuse,
                            response.getDelegacyFullName(),
                            response.getMessage()
                    );
                }

                AlertDialog confirmDelegacyDialog = new AlertDialog.Builder(activity)
                        .setTitle(R.string.title_response_delegacy)
                        .setMessage(message)
                        .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        }).create();

                confirmDelegacyDialog.show();
            }
        }
    }

    // Hiển thị hộp thoại thông báo phản hồi bàn giao
    @Override
    public void onShowNotiResponseDelegacy(DelegacyResponse response) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new ShowResponseDelegacyDialogRunnable(getActivity(), response));
        }
    }

    /*
    * END WaiterListOrdersContract.View
    * */

    // Hiển thị hộp thoại nhập username, tìm tài khoản nhân viên
    private void onShowFindUserDialog() {
        ActivityUtils.removePrevFragment(getFragmentManager(), Constant.TAG_DIALOG);

        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            FindAccountDialog dialog = FindAccountDialog.newInstance(viewModel.getUser().getUsername());
            dialog.setCallback(viewModel.getOnSubmitDelegacyUserNameCallback());
            dialog.show(transaction, Constant.TAG_DIALOG);
        }
    }
}
