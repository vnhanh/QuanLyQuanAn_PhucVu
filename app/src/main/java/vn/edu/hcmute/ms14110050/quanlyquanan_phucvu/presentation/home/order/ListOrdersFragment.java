package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.fragment.BaseNetworkFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentListOrdersBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.HomeActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.SetupOrderActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListOrdersFragment
        extends BaseNetworkFragment<FragmentListOrdersBinding, ListOrdersContract.View, ListOrdersViewModel>
        implements ListOrdersContract.View{

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
    protected FragmentListOrdersBinding initBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_list_orders, container, false);
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

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
        SetupOrderActivity.startActivity(getActivity(), viewModel.getUser(), OrderMode.ADD, null);
    }

    /*
    * END ListOrdersContract.View
    * */
}
