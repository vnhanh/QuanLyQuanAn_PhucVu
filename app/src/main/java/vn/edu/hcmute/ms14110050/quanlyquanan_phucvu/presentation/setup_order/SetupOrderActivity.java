package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ActivitySetupOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.SelectFoodDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.recyclerview.FoodAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.recycler.TableAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.SelectTableDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food.InputOneTextDialogFragment;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.EXTRA_ORDER_ID;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.EXTRA_PROCESS_MODE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderConstant.EXTRA_USER;

public class SetupOrderActivity extends BaseActivity<ActivitySetupOrderBinding, ISetupOrder.View, SetupOrderViewModel>
        implements ISetupOrder.View{

    private Menu menu;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_setup_order, menu);
        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (viewModel != null) {
            if (viewModel.isCreateOrder.get()) {
                switch (item.getItemId()) {
                    case R.id.menu_create_order:
                        viewModel.onClickCreateOrder();
                        return true;

                    case R.id.menu_select_table:
                        onClickAddTables();
                        return true;

                    case R.id.menu_select_food:
                        viewModel.onClickSelectFoods();
                        return true;

                    case android.R.id.home:
                        viewModel.onClickBackButton();
                }

            }else{
                switch (item.getItemId()) {
                    case R.id.menu_confirm_order:
                        viewModel.onClickConfirmMenu();
                        return true;

                    case android.R.id.home:
                        onExit();
                        return true;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (viewModel.isCreateOrder.get()) {
                    viewModel.onClickBackButton();
                }else{
                    onExit();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readIntent();
        // sau khi đọc intent
        // nếu vẫn chưa xác định được mode xử lý order hoặc order id
        int orderMode = viewModel.getProcessMode();
        if ((orderMode != OrderMode.CREATE && orderMode != OrderMode.VIEW)
                || viewModel.getWaiter() == null) {
            Log.d("LOG", getClass().getSimpleName() + ":onCreate(): not get nesccessary information");
            // trở về màn hình cũ
            onBackPressed();
            return;
        }

        initRecyclerViews();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        if (viewModel == null) {
            viewModel = initViewModel();
            binding.setViewmodel(viewModel);
        }
        super.onStart();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(viewModel.toolbarTitle.get()));
        }

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
        tablesAdapter = new TableAdapter(this);
        binding.recyclerviewTables.setAdapter(tablesAdapter);
        tablesAdapter.setContainerVM(viewModel);
        viewModel.setTableDataListener(tablesAdapter);

        LinearLayoutManager _manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        foodAdapter = new FoodAdapter(this);
        binding.recyclerviewFoods.setLayoutManager(_manager);
        binding.recyclerviewFoods.setAdapter(foodAdapter);
        foodAdapter.setContainerVM(viewModel);
        viewModel.setFoodDataListener(foodAdapter);
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
    public void onShowLoadTablesByOrderIDProgress() {
        binding.recyclerviewTables.setEnabled(false);
    }

    @Override
    public void onAnimationShowStatus() {
        binding.txtStatus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    @Override
    public void onAnimationNumberCustomer() {
        binding.txtNumberCustomer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    @Override
    public void onAnimationFinalCost() {
        binding.txtTotalCost.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    @Override
    public void onAnimationDescriptionOrder() {
        binding.txtDescription.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    @Override
    public void onHideLoadTablesByOrderIDProgress() {
        binding.recyclerviewTables.setEnabled(true);
    }

    @Override
    public void onExit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }


    @Override
    public void openConfirmDialog(@StringRes int messageResId, final GetCallback<Void> callback) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_exit)
                .setMessage(messageResId)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callback.onFinish(null);
                    }
                }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void openInputNumberCustomerView(int customerNumber, InputCallback callback) {
        removePrevFragDialog();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        InputOneTextDialogFragment _fragment  =
                InputOneTextDialogFragment.newInstance(
                        getString(R.string.title_input_number_customer),
                        getString(R.string.hint_number_customer),
                        InputType.TYPE_CLASS_NUMBER, String.valueOf(customerNumber));
        _fragment.setListener(callback);
        _fragment.show(ft, "dialog");
    }

    @Override
    public void openDescriptionDialog(String description, InputCallback listener) {
        removePrevFragDialog();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        InputOneTextDialogFragment _fragment  =
                InputOneTextDialogFragment.newInstance(
                        getString(R.string.title_input_description),
                        getString(R.string.hint_input_description),
                        InputType.TYPE_CLASS_TEXT, description);
        _fragment.setListener(listener);
        _fragment.show(ft, "dialog");
    }

    private void removePrevFragDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            ft.remove(fragment);
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onUpdateMenu(boolean availableShow, boolean isCreateOrder, @OrderFlag int statusFlag) {
        // activity chưa tạo menu
        if (menu == null) {
            return false;
        }

        MenuItem menuCreateOrder = menu.findItem(R.id.menu_create_order);
        MenuItem menuSelectTable = menu.findItem(R.id.menu_select_table);
        MenuItem menuSelectFood = menu.findItem(R.id.menu_select_food);
        MenuItem menuConfirmOrder = menu.findItem(R.id.menu_confirm_order);

        // được phép shown menu
        if (!availableShow) {
            menuCreateOrder.setVisible(false);
            menuSelectTable.setVisible(false);
            menuSelectFood.setVisible(false);
            menuConfirmOrder.setVisible(false);
        }
        else{
            // đang tạo order
            if (isCreateOrder) {
                menuCreateOrder.setVisible(true);
                menuSelectTable.setVisible(true);
                menuSelectFood.setVisible(true);
                menuConfirmOrder.setVisible(false);
            }
            else{
                menuCreateOrder.setVisible(false);
                menuSelectTable.setVisible(false);
                menuSelectFood.setVisible(false);
                menuConfirmOrder.setVisible(true);

                switch (statusFlag) {
                    case OrderFlag.PENDING:
                        // order đã được tạo, chờ bếp xác nhận
                        // có thể hủy
                        menuConfirmOrder.setTitle(R.string.title_remove_order);
                        return true;

                    case OrderFlag.PAYING:

                    case OrderFlag.COMPLETE:
                        menuConfirmOrder.setVisible(false);
                        return true;

                    default:
                        // order đã được bếp xác nhận
                        // có thể thanh toán
                        menuConfirmOrder.setTitle(R.string.title_confirm_paying);
                        return true;
                }
            }
        }
        return true;
    }

    /*
    * End ISetupOrder
    * */
}
