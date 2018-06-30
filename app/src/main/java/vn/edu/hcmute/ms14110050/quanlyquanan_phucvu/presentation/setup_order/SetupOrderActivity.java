package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view.MyProgressDialog;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ActivitySetupOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.SelectFoodDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview.FoodAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.recycler.TableAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.SelectTableDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food.InputCountCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food.InputCountDialogFragment;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.EXTRA_ORDER_ID;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.EXTRA_PROCESS_MODE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.EXTRA_USER;

public class SetupOrderActivity extends BaseActivity<ActivitySetupOrderBinding, ISetupOrder.View, SetupOrderViewModel>
        implements ISetupOrder.View{

    public static void startActivity(@NonNull Activity context, User waiter, @OrderMode int processMode, String orderID) {
        Intent intent = new Intent(context, SetupOrderActivity.class);
        // chế độ xử lý order :  tạo hoặc sửa
        intent.putExtra(EXTRA_PROCESS_MODE, processMode);
        Gson gson = new Gson();
        intent.putExtra(EXTRA_USER, gson.toJson(waiter));
        if (!StringUtils.isEmpty(orderID)) {
            intent.putExtra(EXTRA_ORDER_ID, orderID);
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readIntent();
        // sau khi đọc intent
        // nếu vẫn chưa xác định được mode xử lý order hoặc order id
        int orderMode = viewModel.getProcessMode();
        if ((orderMode != OrderMode.ADD && orderMode != OrderMode.VIEW)
                || viewModel.getWaiter() == null) {
            Log.d("LOG", getClass().getSimpleName() + ":onCreate(): not get nesccessary information");
            // trở về màn hình cũ
            onBackPressed();
            return;
        }

        initRecyclerViews();
    }

    @Override
    protected void onStart() {
        if (viewModel == null) {
            viewModel = initViewModel();
            binding.setViewmodel(viewModel);
        }
        super.onStart();
        Log.d("LOG", getClass().getSimpleName() + ":onStart():viewmodel:" + viewModel);
        binding.setActivity(this);
        initSelectFoodFragment();
    }

    private void initSelectFoodFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        SelectFoodDialogFragment fragment = SelectFoodDialogFragment.newInstance();
        fragment.setCenterViewModel(viewModel);
        ft.replace(R.id.frame_layout, fragment, "dialog");
        ft.commit();
    }

    private TableAdapter tablesAdapter;
    private FoodAdapter foodAdapter;

    private void initRecyclerViews() {
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.recyclerviewTables.setLayoutManager(manager);
        tablesAdapter = new TableAdapter();
        binding.recyclerviewTables.setAdapter(tablesAdapter);
        tablesAdapter.setContainerVM(viewModel);
        viewModel.setTableDataListener(tablesAdapter);

        LinearLayoutManager _manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        foodAdapter = new FoodAdapter();
        binding.recyclerviewFoods.setLayoutManager(_manager);
        binding.recyclerviewFoods.setAdapter(foodAdapter);
        foodAdapter.setContainerVM(viewModel);
        viewModel.setFoodDataListener(foodAdapter);
    }

    // TODO : kiểm tra dữ liệu đã thao tác
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void readIntent() {
        if (getIntent() != null && getIntent().hasExtra(EXTRA_PROCESS_MODE)) {
            int processMode = getIntent().getIntExtra(EXTRA_PROCESS_MODE, -1);
            viewModel.setProcessMode(processMode);
            if (getIntent().hasExtra(EXTRA_USER)) {
                String waiterJson = getIntent().getStringExtra(EXTRA_USER);
                Gson gson = new Gson();
                User waiter = gson.fromJson(waiterJson, new TypeToken<User>() {}.getType());
                viewModel.setWaiter(waiter);
            }
            if (getIntent().hasExtra(EXTRA_ORDER_ID)) {
                viewModel.setOrderID(getIntent().getStringExtra(EXTRA_ORDER_ID));
            }
        }
    }

    /*
    * BaseActivity
    * */

    @Override
    protected ActivitySetupOrderBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_setup_order);
    }

    @Override
    protected SetupOrderViewModel initViewModel() {
        return new SetupOrderViewModel();
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

    /*
    * End BaseActivity
    * */

    /*
    * DataBinding
    * */

    public void onClickAddTables() {
//        removeDiaglogFragmentIfExist();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        SelectTableDialogFragment fragment = SelectTableDialogFragment.newInstance();
        fragment.setChangeNetworkStateContainer(this);
        fragment.setCenterViewModel(viewModel);
        fragment.show(ft, "dialog");
    }

    private void removeDiaglogFragmentIfExist() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev).commit();
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    /*
    * End DataBinding
    * */

    /*
    * ISetupOrder
    * */

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onShowLoadTablesByOrderIDProgress() {
        binding.recyclerviewTables.setEnabled(false);
    }

    @Override
    public void onAnimationShowStatus() {
        binding.txtStatus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    @Override
    public void onHideLoadTablesByOrderIDProgress() {
        binding.recyclerviewTables.setEnabled(true);
    }

    @Override
    public void onBackPrevActivity() {
        onBackPressed();
    }

    private AlertDialog progressDialog;

    @Override
    public void showProgress(int messageIdRes) {
        progressDialog = MyProgressDialog.create(this, messageIdRes);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private AlertDialog inputNumberCustomerDialog;

    @Override
    public void openInputNumberCustomerView(int customerNumber, InputCallback callback) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            ft.remove(fragment);
        }
        ft.addToBackStack(null);

        InputCountDialogFragment _fragment  =
                InputCountDialogFragment.newInstance(
                        getString(R.string.title_input_number_customer),
                        getString(R.string.hint_number_customer), customerNumber);
        _fragment.setListener(callback);
        _fragment.show(ft, "dialog");
    }

    /*
    * End ISetupOrder
    * */
}
