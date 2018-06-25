package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.ChangeNetworkStateContainer;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.broadcast.NetworkChangeReceiver;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.ActivityUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.service.get_user.SocketUserService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.account.AccountFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.home.order.ListOrdersFragment;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.login.LoginActivity;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.NODEJS.USERNAME;

public class HomeActivity extends AppCompatActivity implements ChangeNetworkStateContainer {
    private Intent socketUserIntent;
    private String username;
    protected NetworkChangeReceiver networkChangeReceiver;

    public static void startActivity(Activity context, String username) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(USERNAME, username);
        context.startActivity(intent);
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
        setContentView(R.layout.activity_home);

        // khởi tạo và đăng ký NetworkChangeReceiver
        initNetworkChangeReceiver();

        frameLayout = findViewById(R.id.frame_layout);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int MENU_ITEM_DEFAULT = R.id.navigation_order;
        navigation.setSelectedItemId(MENU_ITEM_DEFAULT);
        navigation.performClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (StringUtils.isEmpty(username)) {
            if (getIntent() != null && getIntent().hasExtra(USERNAME)) {
                username = getIntent().getStringExtra(USERNAME);
            }
        }
        if (!StringUtils.isEmpty(username)) {
            username = getIntent().getStringExtra(USERNAME);
            socketUserIntent = new Intent(this, SocketUserService.class);
            socketUserIntent.putExtra(USERNAME, username);
            startService(socketUserIntent);
            bindService(socketUserIntent, socketUserServiceConnection, Context.BIND_AUTO_CREATE);
        }else{
            Toast.makeText(this, getString(R.string.error_extra_get_username), Toast.LENGTH_SHORT).show();
            LoginActivity.startActivity(this);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
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
        // xóa token khi thoát app hoặc quay về màn hình login
        SSharedReference.clearToken(this);
        super.onDestroy();
    }

    private void initNetworkChangeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    public NetworkChangeReceiver getChangeNetworkStateListener() {
        return networkChangeReceiver;
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
            getSupportActionBar().setTitle(user.getFullname());

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
}