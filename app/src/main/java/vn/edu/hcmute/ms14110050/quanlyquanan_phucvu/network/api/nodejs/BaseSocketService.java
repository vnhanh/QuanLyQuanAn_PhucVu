package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import io.socket.emitter.Emitter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.Callback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.asynctask.ConvertSocketResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket.SocketManager;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class BaseSocketService {
    protected ArrayList<String> events = new ArrayList<>();
    protected ArrayList<Emitter.Listener> listeners = new ArrayList<>();

    /**
     *
     * @param event
     * @param callback
     * @param tag : dùng cho các giá trị đơn đuọc gửi tới (ví dụ : {"table":table} -> tag="table").
     *            Nếu có giá trị null, tức là giá trị trả về sẽ được convert sang 1 object có nhiều trường
     * @param typeClass : loại class. Nếu null tức là chỉ cần convert sang kiểu String, không cần convert
     * @param <MODEL>
     */
    protected  <MODEL> void listenEvent(final String event, final GetCallback<MODEL> callback,
                                        final String tag, final Type typeClass) {
        events.add(event);

        Emitter.Listener listener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObj = (JSONObject) args[0];
                try {
                    String value = jsonObj.getString(tag);

//                    Log.d("LOG", BaseSocketService.class.getSimpleName()
//                            + ":onEvent:" + event + ":result:" + jsonObj.toString()
//                            + ":type class:"+ new TypeToken<MODEL>(){}.getClass());

                    // typeClass == null nghĩa là không cần convert
                    if (typeClass == null) {
//                        Log.d("LOG", BaseSocketService.class.getSimpleName() +
//                                ":get data from socket:not convert");
                        callback.onFinish((MODEL) value);
                    }else{
                        new ConvertSocketResponse<MODEL>(callback, typeClass,
                                false)
                                .execute(value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("LOG", BaseSocketService.class.getSimpleName() +
                            ":get data from socket:count exception:"+e.getMessage());
                    callback.onFinish(null);
                }
            }
        };

        listeners.add(listener);

        SocketManager.getInstance().onSocket(event, listener);
    }

    protected  <MODEL> void listenEvent(final String event, final GetCallback<MODEL> callback,
                                        final Type typeClass) {
        events.add(event);

        Emitter.Listener listener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObj = (JSONObject) args[0];
                try {
                    String value = jsonObj.toString();

//                    Log.d("LOG", BaseSocketService.class.getSimpleName()
//                            + ":onEvent:" + event + ":result:" + jsonObj.toString()
//                            + ":type class:"+ new TypeToken<MODEL>(){}.getClass());

                    // typeClass == null nghĩa là không cần convert
                    if (typeClass == null) {
//                        Log.d("LOG", BaseSocketService.class.getSimpleName() +
//                                ":get data from socket:not convert");
                        callback.onFinish((MODEL) value);
                    }else{
                        new ConvertSocketResponse<MODEL>(callback, typeClass,
                                false)
                                .execute(value);
                    }
                } catch (Exception e) {
                    Log.d("LOG", BaseSocketService.class.getSimpleName() +
                            ":get data from socket:count exception:"+e.getMessage());

                    e.printStackTrace();
                    callback.onFinish(null);
                }
            }
        };

        listeners.add(listener);

        SocketManager.getInstance().onSocket(event, listener);
    }

    protected  <MODEL> void listenEvent(final String event, Emitter.Listener listener) {
        events.add(event);

        listeners.add(listener);

        SocketManager.getInstance().onSocket(event, listener);
    }

    public void stopAllEvents() {
        while (events != null && events.size() > 0) {
            SocketManager.getInstance().offSocket(events.get(0), listeners.get(0));
            events.remove(0);
            listeners.remove(0);
        }
    }
}
