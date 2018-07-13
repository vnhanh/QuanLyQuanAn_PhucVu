package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.ChangeNetworkStateContainer;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.NetworkChangeReceiver;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.BaseActivity;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.activity.MessageRunnable;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.Constant;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.OnChangeSocketStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.SocketManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.ActivityUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.service.get_user.SocketUserService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ActivityHomeBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.account.AccountRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.AccountFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.ListOrdersFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login.LoginActivity;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.NODEJS.USERNAME;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, IHomeView, HomeViewModel>
        implements IHomeView, ChangeNetworkStateContainer {

    private Intent socketUserIntent;

    public static void startActivity(Activity context, String username) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(USERNAME, username);
        context.startActivity(intent);

        context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void readIntent() {
        if (getIntent() != null && getIntent().hasExtra(USERNAME)) {
            String username = getIntent().getStringExtra(USERNAME);
            viewModel.setUserName(username);
        }
    }

    FrameLayout frameLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_order:
                    ListOrdersFragment fragment = new ListOrdersFragment();
                    fragment.setChangeNetworkStateContainer(HomeActivity.this);
                    ActivityUtils.replaceFragment(getSupportFragmentManager(), fragment, R.id.frame_layout);

                    return true;

                case R.id.navigation_account:
                    AccountFragment _fragment = new AccountFragment();
                    _fragment.setChangeNetworkStateContainer(HomeActivity.this);
                    ActivityUtils.replaceFragment(getSupportFragmentManager(), _fragment, R.id.frame_layout);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readIntent();

        SocketManager.getInstance().addSocketStateListener(viewModel);

        frameLayout = binding.frameLayout;
        BottomNavigationView navigation = binding.navigation;
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int MENU_ITEM_DEFAULT = R.id.navigation_order;
        navigation.setSelectedItemId(MENU_ITEM_DEFAULT);
        navigation.performClick();
    }

    @Override
    protected ActivityHomeBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_home);
    }

    @Override
    protected HomeViewModel initViewModel() {
        return new HomeViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String username = viewModel.getUserName();

        if (!StringUtils.isEmpty(username)) {
            socketUserIntent = new Intent(this, SocketUserService.class);
            socketUserIntent.putExtra(USERNAME, username);
            startService(socketUserIntent);
            bindService(socketUserIntent, socketUserServiceConnection, Context.BIND_AUTO_CREATE);
        }
        else{
            onToast(R.string.error_extra_get_username);

            LoginActivity.startActivity(this);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onAttachViewModel() {
        viewModel.onViewAttach(this);
    }

    @Override
    protected void onStop() {
        if (socketUserBinded) {
            unbindService(socketUserServiceConnection);
        }
        stopService(socketUserIntent);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // xóa token và tài khoản user khi thoát app hoặc quay về màn hình login
        SSharedReference.clearToken(this);
        SSharedReference.clearUserName(this);
        SocketManager.getInstance().removeSocketStateListener(viewModel);
        super.onDestroy();
    }

    private ArrayList<GetCallback<User>> changeUserProfileLisenersQueue = new ArrayList<>();

    public void registerChangeUserProfileListener(GetCallback<User> listener){
        if (socketUserService != null) {
            socketUserService.registerChangeUserProfileListener(listener);
        }else{
            changeUserProfileLisenersQueue.add(listener);
        }
    }

    private GetCallback<User> changeUserProfileListener = new GetCallback<User>() {
        @Override
        public void onFinish(User user) {
            if (user == null) {
                HomeActivity.this.onBackPressed();
                Toast.makeText(HomeActivity.this, getString(R.string.user_data_is_null), Toast.LENGTH_SHORT).show();
                return;
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(user.getFullname());
            }

            String username = user.getUsername();
            SSharedReference.setUserName(HomeActivity.this, username);

            while (changeUserProfileLisenersQueue != null && changeUserProfileLisenersQueue.size() > 0) {
                socketUserService.registerChangeUserProfileListener(changeUserProfileLisenersQueue.get(0));
                changeUserProfileLisenersQueue.remove(0);
            }
        }
    };

    private SocketUserService socketUserService;
    private boolean socketUserBinded = false;

    private ServiceConnection socketUserServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SocketUserService.SocketUserBinder binder = (SocketUserService.SocketUserBinder) iBinder;
            socketUserService = binder.getService();
            socketUserBinded = true;
            socketUserService.registerChangeUserProfileListener(changeUserProfileListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            socketUserBinded = false;
        }
    };

    @Override
    public void onLogout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginActivity.startActivity(HomeActivity.this);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
}