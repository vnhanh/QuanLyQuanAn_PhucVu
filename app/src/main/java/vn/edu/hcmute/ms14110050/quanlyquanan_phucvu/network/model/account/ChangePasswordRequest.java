package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class ChangePasswordRequest {
    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("password")
    @Expose
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
