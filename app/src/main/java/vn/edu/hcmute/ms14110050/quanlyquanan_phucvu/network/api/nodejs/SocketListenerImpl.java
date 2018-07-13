package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs;

import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Type;

import io.socket.emitter.Emitter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.Callback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.asynctask.ConvertSocketResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.IWebData;

public class SocketListenerImpl<DATA extends IWebData> implements Emitter.Listener, GetCallback<DATA> {
    private Callback<IWebData> callback;
    private int flag;
    private String tag;
    private Type typeClass;

    public SocketListenerImpl(Callback<IWebData> callback, int flag) {
        this.callback = callback;
        this.flag = flag;
    }

    public SocketListenerImpl(int flag, Callback<IWebData> callback, String tag, Type typeClass) {
        this.flag = flag;
        this.callback = callback;
        this.tag = tag;
        this.typeClass = typeClass;
    }

    public SocketListenerImpl(int flag, Callback<IWebData> callback, Type typeClass) {
        this.flag = flag;
        this.callback = callback;
        this.tag = null;
        this.typeClass = typeClass;
    }

    @Override
    public void call(Object... args) {
        if (tag == null && typeClass == null) {
            throw new IllegalStateException("Not have enough argument to convert data get from socket");
        }
        JSONObject jsonObj = (JSONObject) args[0];
        try {
            if (tag == null) {
                String value = jsonObj.toString();

//                    Log.d("LOG", BaseSocketService.class.getSimpleName()
//                            + ":onEvent:" + event + ":result:" + jsonObj.toString()
//                            + ":type class:"+ new TypeToken<MODEL>(){}.getClass());

                new ConvertSocketResponse<DATA>(this, typeClass,
                        false)
                        .execute(value);
            }
            else{
                String value = jsonObj.getString(tag);
                if (typeClass == null) {
//                        Log.d("LOG", BaseSocketService.class.getSimpleName() +
//                                ":get data from socket:not convert");
                    // kiểm tra xem DATA có phải là String hay không
                    // nếu có thì trả về giá trị
                    // không thì throw Exception
                    // TODO

                }else{

                    new ConvertSocketResponse<DATA>(this, typeClass,
                            false)
                            .execute(value);
                }
            }
        } catch (Exception e) {
            Log.d("LOG", BaseSocketService.class.getSimpleName() +
                    ":get data from socket:count exception:"+e.getMessage());

            e.printStackTrace();
            callback.onGet(null, flag);
        }
    }

    @Override
    public void onFinish(DATA data) {
        callback.onGet(data, flag);
    }
}
