package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.FragmentViewPagerAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.WaiterActivitySetupOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.contract.OrderMode;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.food.SelectFoodDialogFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.order_info.WaiterOrderInfoFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table.SelectTableDialogFragment;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.OrderConstant.EXTRA_ORDER_ID;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.OrderConstant.EXTRA_PROCESS_MODE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.OrderConstant.EXTRA_USER;

public class WaiterSetupOrderActivity extends BaseActivity<WaiterActivitySetupOrderBinding, ISetupOrder.View, WaiterSetupOrderViewModel>
        implements ISetupOrder.View{

    private Menu menu;

    public static void startActivity(@NonNull Activity context, User waiter, @OrderMode int processMode, String orderID) {
        Intent intent = new Intent(context, WaiterSetupOrderActivity.class);
        // chế độ xử lý order :  tạo hoặc sửa
        intent.putExtra(EXTRA_PROCESS_MODE, processMode);
        Gson gson = new Gson();
        intent.putExtra(EXTRA_USER, gson.toJson(waiter));
        if (!StrUtil.isEmpty(orderID)) {
            intent.putExtra(EXTRA_ORDER_ID, orderID);
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_setup_order_waiter, menu);
        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (viewModel != null) {
            if (viewModel.isCreateOrder.get()) {
                switch (item.getItemId()) {
                    case R.id.menu_process_order:
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
                    case R.id.menu_process_order:
                        viewModel.onCLickReProcessOrder();
                        return true;

                    case R.id.menu_serve_order:
                        viewModel.onClickMenuServeOrder();
                        return true;

                    case R.id.menu_pay_order:
                        viewModel.onClickMenuPayOrder();
                        return true;

                    case R.id.menu_select_food:
                        viewModel.onClickSelectFoods();
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
        Log.d("LOG", getClass().getSimpleName() + ":onCreate()");

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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentViewPagerAdapter pagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());

        WaiterOrderInfoFragment orderInfoFragment = new WaiterOrderInfoFragment();
        orderInfoFragment.setViewmodel(viewModel);
        pagerAdapter.add(null, orderInfoFragment);

        SelectFoodDialogFragment selectFoodFragment = new SelectFoodDialogFragment();
        selectFoodFragment.setCenterViewModel(viewModel);
        pagerAdapter.add(null, selectFoodFragment);

        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    SelectFoodDialogFragment fragment = (SelectFoodDialogFragment) pagerAdapter.getItem(1);
                    fragment.onSelected();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    protected WaiterActivitySetupOrderBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.waiter_activity_setup_order);
    }

    @Override
    protected WaiterSetupOrderViewModel initViewModel() {
        return new WaiterSetupOrderViewModel();
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
        removePrevFragDialog();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        SelectTableDialogFragment fragment = SelectTableDialogFragment.newInstance();
        fragment.setChangeNetworkStateContainer(this);
        fragment.setCenterViewModel(viewModel);
        fragment.show(ft, "dialog");
    }

    /*
    * End DataBinding
    * */

    /*
    * ISetupOrder
    * */

    @Override
    public <Void> void openConfirmDialog(@StringRes int titleResId, @StringRes int messageResId,
                                         final GetCallback<Void> callback) {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(titleResId)
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
    public void onOpenInfoView() {
        binding.viewPager.setCurrentItem(0);
    }

    @Override
    public void onOpenSelectFoodsView() {
        binding.viewPager.setCurrentItem(1);
    }

    @Override
    public void onShowLoadTablesByOrderIDProgress() {
//        binding.recyclerviewTables.setEnabled(false);
    }

    @Override
    public void onHideLoadTablesByOrderIDProgress() {
//        binding.recyclerviewTables.setEnabled(true);
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

        MenuItem menuProcessOrder = menu.findItem(R.id.menu_process_order);
        MenuItem menuSelectTable = menu.findItem(R.id.menu_select_table);
        MenuItem menuSelectFood = menu.findItem(R.id.menu_select_food);
        MenuItem menuServeOrder = menu.findItem(R.id.menu_serve_order);
        MenuItem menuPay = menu.findItem(R.id.menu_pay_order);

        // không được phép shown menu
        if (!availableShow) {
            menuProcessOrder.setVisible(false);
            menuSelectTable.setVisible(false);
            menuSelectFood.setVisible(false);
            menuServeOrder.setVisible(false);
            menuPay.setVisible(false);
        }
        else{
            // đang tạo order
            if (isCreateOrder) {
                menuProcessOrder.setTitle(R.string.action_create_order);
                menuProcessOrder.setVisible(true);
                menuSelectTable.setVisible(true);
                menuSelectFood.setVisible(true);
                menuServeOrder.setVisible(false);
                menuPay.setVisible(false);
            }
            else{
                if (statusFlag < OrderFlag.PAYING) {
                    menuProcessOrder.setTitle(R.string.action_confirm_order);
                    menuProcessOrder.setVisible(true);
                    menuSelectFood.setVisible(true);
                }else{
                    menuProcessOrder.setVisible(false);
                    menuSelectFood.setVisible(false);

                    // ngăn lướt viewpager
                    binding.viewPager.beginFakeDrag();
                }
                menuSelectTable.setVisible(false);
                menuServeOrder.setVisible(true);

                switch (statusFlag) {
                    case OrderFlag.PENDING:
                        // order đã được tạo, chờ bếp xác nhận
                        // có thể hủy
                        menuServeOrder.setTitle(R.string.title_remove_order);
                        menuPay.setVisible(false);
                        return true;

                    case OrderFlag.PREPARE:
                        // bếp nấu xong, chuẩn bị dọn ra
                        menuServeOrder.setTitle(R.string.title_prepare);
                        menuPay.setVisible(true);
                        return true;

                    case OrderFlag.PAYING:


                    case OrderFlag.COMPLETE:
                        menuServeOrder.setVisible(false);
                        menuPay.setVisible(false);
                        return true;

                        // EATING
                    default:
                        // order đã được bếp xác nhận
                        // có thể thanh toán
                        menuServeOrder.setVisible(false);
                        menuPay.setVisible(true);
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
