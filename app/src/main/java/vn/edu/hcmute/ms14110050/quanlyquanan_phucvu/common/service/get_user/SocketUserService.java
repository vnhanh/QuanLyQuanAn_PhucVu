package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.service.get_user;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.SocketManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.account.AccountRequestManager;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.NODEJS.USERNAME;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_SERVER_UPLOAD_EMPLOYEE;

/**
 * Created by Vo Ngoc Hanh on 6/14/2018.
 */

public class SocketUserService extends Service {
    private AccountRequestManager requestManager;
    private String token;
    private String username;
    private User user;
    private ArrayList<GetCallback<User>> changeUserListeners = new ArrayList<>();

    private SocketUserBinder binder = new SocketUserBinder();

    private Emitter.Listener listener;

    public class SocketUserBinder extends Binder{
        public SocketUserService getService() {
            return SocketUserService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            requestManager = AccountRequestManager.getInstance();
            if (intent.hasExtra(USERNAME)) {
                username = intent.getStringExtra(USERNAME);
                Log.d("LOG", getClass().getSimpleName() + ":onStartCommand():intent has username:username:" + username);
                setupRequestUser();
            }
        }else{
            Log.d("LOG", getClass().getSimpleName() + ":onStartCommand():intent is null:stopSelft()");
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void setupRequestUser() {
        if (StringUtils.isEmpty(username)) {
            return;
        }

        if (SocketManager.getInstance().isInit() && !SocketManager.getInstance().connected()) {
            SocketManager.getInstance().connect();
        }

        listener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("LOG", "SocketUserService:socket:on event:server-loadEmployee:" + args[0].toString());
                JSONObject usernameJsonObj = new JSONObject();
                try {
                    usernameJsonObj.put("username", username);
                    GetCallback<User> callback = new GetCallback<User>() {
                        @Override
                        public void onFinish(User user) {
                            onGetUserFromSocket(user);
                        }
                    };
                    new ConvertUserJsonTask(callback).execute((JSONObject) args[0], usernameJsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SocketManager.getInstance().onSocket(SOCKET_SERVER_UPLOAD_EMPLOYEE, listener);

        requestUser();
    }

    private void requestUser() {
        if(token==null) {
            token = SSharedReference.getToken(this);
        }

        requestManager.getProfile(token, username, new GetCallback<User>() {
            @Override
            public void onFinish(User user) {
                Log.d("LOG", SocketUserService.class.getSimpleName()
                        + ":getProfile():user:" + (user != null ? user.getFullname() : user));
                onGetUserFromSocket(user);
            }
        });
    }

    private void onGetUserFromSocket(User user) {
        this.user = user;
        notifyChangeUserListeners(user);
    }

    private void notifyChangeUserListeners(User user) {
        if (changeUserListeners == null) {
            return;
        }
        for (GetCallback<User> listener : changeUserListeners) {
            listener.onFinish(user);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketManager.getInstance().offSocket(SOCKET_SERVER_UPLOAD_EMPLOYEE, listener);
        changeUserListeners = null;
    }

    public void registerChangeUserProfileListener(@NonNull GetCallback<User> listener) {
        if (changeUserListeners == null) {
            changeUserListeners = new ArrayList<>();
        }
        changeUserListeners.add(listener);
        if (user != null) {
            listener.onFinish(user);
        }
    }

    public void unRegisterChangeUserProfileListener(@NonNull GetCallback<User> listener) {
        if (changeUserListeners == null) {
            changeUserListeners = new ArrayList<>();
            return;
        }
        if (changeUserListeners.contains(listener)) {
            changeUserListeners.add(listener);
        }
    }
}
