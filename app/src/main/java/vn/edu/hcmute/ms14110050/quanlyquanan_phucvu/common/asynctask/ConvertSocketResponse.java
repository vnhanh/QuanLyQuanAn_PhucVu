package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;


/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class ConvertSocketResponse<MODEL> extends AsyncTask<String,Void,MODEL> {
    private GetCallback<MODEL> callback;
    private Type typeParameterClass;
    private boolean enableComplexMapKeySeriablization = false;

    public ConvertSocketResponse(GetCallback<MODEL> callback, Type typeParameterClass, boolean enableComplexMapKeySeriablization) {
        this.callback = callback;
        this.typeParameterClass = typeParameterClass;
        this.enableComplexMapKeySeriablization = enableComplexMapKeySeriablization;
    }

    @Override
    protected MODEL doInBackground(String... params) {
        String param = params[0];
        if (param == null) {
            return null;
        }

        Gson gson = null;
        if (enableComplexMapKeySeriablization) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.enableComplexMapKeySerialization().create();
        }else{
            gson = new Gson();
        }
        Log.d("LOG", getClass().getSimpleName() + ":typeParameterClass:" + typeParameterClass);
        Log.d("LOG", getClass().getSimpleName()
                + ":input:"+param+":result convert:" + gson.fromJson(param, typeParameterClass));

        return gson.fromJson(param, typeParameterClass);
    }

    @Override
    protected void onPostExecute(MODEL model) {
        super.onPostExecute(model);
        callback.onFinish(model);
    }
}
