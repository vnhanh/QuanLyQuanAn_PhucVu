package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.view_food;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.WaiterActivityViewFoodBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;

public class WaiterViewFoodActivity extends BaseActivity<WaiterActivityViewFoodBinding, IViewFood, WaiterViewFoodModel> implements IViewFood {
    private static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    private static final String EXTRA_DETAIL_ORDER = "EXTRA_DETAIL_ORDER";
    private static final String EXTRA_FOOD = "EXTRA_FOOD";

    public static void startActivity(Activity activity, String orderID, DetailOrder detailOrder, String foodID) {
        Intent intent = new Intent(activity, WaiterViewFoodActivity.class);

        Gson gson = new Gson();
        String detailOrderJson = gson.toJson(detailOrder);
        intent.putExtra(EXTRA_ORDER_ID, orderID);
        intent.putExtra(EXTRA_FOOD, foodID);
        intent.putExtra(EXTRA_DETAIL_ORDER, detailOrderJson);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        if (viewModel != null) {
            viewModel.onClickBackButton();
        }
    }

    @Override
    public void onBackPrevActivity() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_view_food_waiter, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (item.getItemId() == R.id.menu_confirm_order) {
            onBackPrevActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readIntent();

        if (viewModel.getFood() == null || viewModel.getFood().getId() == null) {
            Toast.makeText(this, getString(R.string.not_start_activity), Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_order_food_count);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (viewModel != null) {
            viewModel.setImageFoodWidth(binding.imageFood.getWidth());
        }
    }

    @Override
    protected WaiterActivityViewFoodBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.waiter_activity_view_food);
    }

    @Override
    protected WaiterViewFoodModel initViewModel() {
        return new WaiterViewFoodModel();
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

    private void readIntent() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_FOOD) && getIntent().hasExtra(EXTRA_DETAIL_ORDER)) {
                String orderID = getIntent().getStringExtra(EXTRA_ORDER_ID);
                String foodID = getIntent().getStringExtra(EXTRA_FOOD);
                String detailOrderJson = getIntent().getStringExtra(EXTRA_DETAIL_ORDER);
                Gson gson = new Gson();
//                Food food = gson.fromJson(foodJson, new TypeToken<Food>(){}.getType());
                DetailOrder detailOrder = gson.fromJson(detailOrderJson, new TypeToken<DetailOrder>(){}.getType());

                viewModel.setOrderID(orderID);
                Food food = new Food();
                food.setId(foodID);
                viewModel.setFood(food);
                viewModel.setDetailOrder(detailOrder);
            }
        }
    }

    /*
    * IViewFood
    * */

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void openInputOrderCountDialog(int oldCount, InputCallback callback) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            ft.remove(fragment);
        }
        ft.addToBackStack(null);

        InputOneTextDialogFragment _fragment = InputOneTextDialogFragment.newInstance(
                getString(R.string.title_order_food_count), getString(R.string.hint_ordered_count), InputType.TYPE_CLASS_NUMBER, String.valueOf(oldCount));
        _fragment.setListener(callback);

        _fragment.show(ft, "dialog");
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
    public View getViewUI() {
        return binding.getRoot();
    }

    /*
    * End IViewFood
    * */

}
