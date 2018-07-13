package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ChefActivitySetupOrderBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts.ISetupOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.food.recyclerview.ChefFoodAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.table.recycler.ChefTableAdapter;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.abstracts.OrderConstant.EXTRA_ORDER_ID;

public class ChefSetupOrderActivity extends BaseActivity<ChefActivitySetupOrderBinding, ISetupOrder.View, ChefSetupOrderViewModel>
        implements ISetupOrder.View{

    private Menu menu;

    public static void startActivity(@NonNull Activity context, @NonNull String orderID) {
        Intent intent = new Intent(context, ChefSetupOrderActivity.class);
        if (!StringUtils.isEmpty(orderID)) {
            intent.putExtra(EXTRA_ORDER_ID, orderID);
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void readIntent() {
        if (getIntent() != null && getIntent().hasExtra(EXTRA_ORDER_ID)) {
            viewModel.setOrderID(getIntent().getStringExtra(EXTRA_ORDER_ID));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_setup_order_chef, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cofirm:
                viewModel.onClickConfirmMenu();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readIntent();
        // sau khi đọc intent
        // nếu vẫn chưa xác định được mode xử lý order hoặc order id
        if (StringUtils.isEmpty(viewModel.getOrderID())) {
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
    }

    private ChefTableAdapter tablesAdapter;
    private ChefFoodAdapter foodAdapter;

    private void initRecyclerViews() {
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.recyclerviewTables.setLayoutManager(manager);
        tablesAdapter = new ChefTableAdapter(this);
        binding.recyclerviewTables.setAdapter(tablesAdapter);
        viewModel.setTableDataListener(tablesAdapter);

        LinearLayoutManager _manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        foodAdapter = new ChefFoodAdapter(this);
        binding.recyclerviewFoods.setLayoutManager(_manager);
        binding.recyclerviewFoods.setAdapter(foodAdapter);
        foodAdapter.setContainerVM(viewModel);
        viewModel.setFoodDataListener(foodAdapter);
    }

    /*
     * BaseActivity
     * */

    @Override
    protected ChefActivitySetupOrderBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.chef_activity_setup_order);
    }

    @Override
    protected ChefSetupOrderViewModel initViewModel() {
        return new ChefSetupOrderViewModel();
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

    /*
     * End DataBinding
     * */

    /*
     * ISetupOrder
     * */

    @Override
    public void onToast(final int messageIdRes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), messageIdRes, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAnimationShowStatus() {
        binding.txtStatus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    @Override
    public void onAnimationFinalCost() {
        binding.txtTotalCost.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    @Override
    public boolean onUpdateMenu(int statusFlag) {
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.menu_cofirm);

            if (statusFlag == OrderFlag.EATING) {
                item.setVisible(false);
            }else if(statusFlag == OrderFlag.PENDING || statusFlag == OrderFlag.COOKING){
                item.setVisible(true);
                item.setTitle(statusFlag == OrderFlag.PENDING ? R.string.title_confirm_start_cooking : R.string.title_confirm_cook_finish);
            }else{
                item.setVisible(false);
            }
            return true;
        }
        return false;
    }

    /*
     * End ISetupOrder
     * */
}