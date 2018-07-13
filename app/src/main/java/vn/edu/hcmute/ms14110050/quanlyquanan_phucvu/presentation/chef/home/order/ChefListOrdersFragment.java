package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ChefFragmentListOrdersBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.ChefItemOrderAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.ChefSetupOrderActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.activity.HomeActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChefListOrdersFragment
        extends BaseNetworkFragment<ChefFragmentListOrdersBinding, ListOrdersContract.View, ChefListOrdersViewModel>
        implements ListOrdersContract.View{

    private ChefItemOrderAdapter adapter;

    public ChefListOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    protected ChefListOrdersViewModel initViewModel() {
        return new ChefListOrdersViewModel();
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
        itemOrderListener.destroy();
        super.onDestroy();
    }

    @Override
    protected ChefFragmentListOrdersBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.chef_fragment_list_orders, container, false);
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

    private void initRecyclerView() {
        if (itemOrderListener == null) {
            itemOrderListener = new OnClickItemOrderListener(getActivity());
        }
        adapter = new ChefItemOrderAdapter(getActivity(), itemOrderListener);
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

        TabLayout tabLayout = binding.tabLayout;
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setCustomView(tab01);
        tabLayout.getTabAt(1).setCustomView(tab02);
        tabLayout.getTabAt(2).setCustomView(tab03);

        if (getContext() != null) {
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        }

        ChefOrdersConstributor constributor = viewModel.getOrderConstributor();
        constributor.registerTabLayout(binding.tabLayout);
        ArrayList<View> customTabs = new ArrayList<>();
        customTabs.add(tab01);
        customTabs.add(tab02);
        customTabs.add(tab03);
        constributor.setTabViews(customTabs);
    }

    private OnClickItemOrderListener itemOrderListener;

    private static class OnClickItemOrderListener implements View.OnClickListener {
        private Activity activity;

        public OnClickItemOrderListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            String orderID = (String) v.getTag(v.getId());
            ChefSetupOrderActivity.startActivity(activity, orderID);
        }

        public void destroy() {
            activity = null;
        }
    };

    /*
    * ListOrdersContract.View
    * */

    /*
    * END ListOrdersContract.View
    * */
}
