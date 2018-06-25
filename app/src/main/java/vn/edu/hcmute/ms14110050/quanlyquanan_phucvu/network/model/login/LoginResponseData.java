package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;


/**
 * Created by Vo Ngoc Hanh on 5/14/2018.
 */

public class LoginResponseData extends ResponseValue {
    @SerializedName("token")
    @Expose
    private String token;

    // key : username
    @SerializedName("user")
    @Expose
    private Map<String,String> user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return user.get("username");
    }

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }
}
