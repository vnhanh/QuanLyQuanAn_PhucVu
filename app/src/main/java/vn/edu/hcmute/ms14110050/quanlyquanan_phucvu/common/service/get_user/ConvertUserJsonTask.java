package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.service.get_user;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class ConvertUserJsonTask extends AsyncTask<JSONObject, Void, User> {
    private GetCallback<User> _callback;

    public ConvertUserJsonTask(GetCallback<User> callback) {
        this._callback = callback;
    }

    @Override
    protected User doInBackground(JSONObject... jsonObjects) {
        JSONObject json = jsonObjects[0];
        JSONObject usernameJson = jsonObjects[1];
        try {
            String _innerUsername = usernameJson.getString("username");
            Gson gson = new Gson();
            String _userStr = json.getString("user");
            User _user = gson.fromJson(_userStr, new TypeToken<User>(){}.getType());
            if (_innerUsername != null && _user != null && _innerUsername.equals(_user.getUsername())) {
                return _user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("LOG", SocketUserService.class.getSimpleName()
                    + ":socket on event server-loadEmployee:error:" + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        _callback.onFinish(user);
    }
}
