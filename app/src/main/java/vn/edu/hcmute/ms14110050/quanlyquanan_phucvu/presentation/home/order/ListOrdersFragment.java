package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;


import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentListOrdersBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.HomeActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.recycler.ItemOrderAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.SetupOrderActivity;

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
        adapter = new ItemOrderAdapter(itemOrderListener);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(manager);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);
        viewModel.setOrderAdapter(adapter);
    }

    private OnClickItemOrderListener itemOrderListener;

    private static class OnClickItemOrderListener implements View.OnClickListener {
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
            Toast.makeText(getContext(), getString(R.string.message_process_error), Toast.LENGTH_SHORT).show();
            return;
        }
        SetupOrderActivity.startActivity(getActivity(), viewModel.getUser(), OrderMode.CREATE, null);
    }

    /*
    * END ListOrdersContract.View
    * */
}
