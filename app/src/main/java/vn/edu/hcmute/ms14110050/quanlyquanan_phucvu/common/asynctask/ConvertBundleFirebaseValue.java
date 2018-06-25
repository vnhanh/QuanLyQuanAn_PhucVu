package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.asynctask;

import android.os.AsyncTask;

import com.google.gson.Gson;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable.SendObject;

/**
 * Created by Vo Ngoc Hanh on 6/10/2018.
 */

public class ConvertBundleFirebaseValue<MODEL> extends AsyncTask<SendObject<String>,Void,MODEL> {
    private GetCallback<SendObject<MODEL>> callback;
    private Class<MODEL> typePrameterClass;
    private int index = -1;

    public ConvertBundleFirebaseValue(GetCallback<SendObject<MODEL>> callback, Class<MODEL> typePrameterClass) {
        this.callback = callback;
        this.typePrameterClass = typePrameterClass;
    }

    @Override
    protected MODEL doInBackground(SendObject<String>... sendObjects) {
        SendObject<String> sendObject = sendObjects[0];
        // dữ liệu không hợp lệ
        if (sendObject == null || sendObject.getTag() < 0 || sendObject.getValue() == null) {
            return null;
        }

        Gson gson = new Gson();

        String param = sendObject.getValue();

        index = sendObject.getTag();

        return gson.fromJson(param, typePrameterClass);
    }

    @Override
    protected void onPostExecute(MODEL model) {
        super.onPostExecute(model);
        callback.onFinish(new SendObject<MODEL>(index, model));
    }
}
