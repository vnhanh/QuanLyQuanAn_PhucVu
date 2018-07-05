package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentListOrdersBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.activity.HomeActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.recycler.ItemOrderAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.SetupOrderActivity;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant.COLOR_ERROR;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListOrdersFragment
        extends BaseNetworkFragment<FragmentListOrdersBinding, ListOrdersContract.View, ListOrdersViewModel>
        implements ListOrdersContract.View{

    private ItemOrderAdapter adapter;

    public ListOrdersFragment() {

        // Required empty public constructor
    }

    @Override
    protected ListOrdersViewModel initViewModel() {
        return new ListOrdersViewModel();
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
    protected FragmentListOrdersBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_list_orders, container, false);
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

        OrdersConstributor constributor = viewModel.getOrderConstributor();
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
        private ListOrdersViewModel viewModel;

        public OnClickItemOrderListener(Activity activity, ListOrdersViewModel viewModel) {
            this.activity = activity;
            this.viewModel = viewModel;
        }

        @Override
        public void onClick(View v) {
            String orderID = (String) v.getTag(v.getId());
            SetupOrderActivity.startActivity(activity, viewModel.getUser(), OrderMode.VIEW, orderID);
        }

        public void destroy() {
            activity = null;
            viewModel = null;
        }
    };

    /*
    * ListOrdersContract.View
    * */

    @Override
    public void openAddOrderScreen() {
        if (getActivity() == null) {
            Log.d("LOG", getClass().getSimpleName() + ":openAddOrderScreen():failed:getActivity() is null");
            onShowMessage(R.string.message_process_error, COLOR_ERROR);
            return;
        }
        SetupOrderActivity.startActivity(getActivity(), viewModel.getUser(), OrderMode.CREATE, null);
    }

    /*
    * END ListOrdersContract.View
    * */
}
