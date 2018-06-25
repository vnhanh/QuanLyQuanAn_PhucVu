package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;


/**
 * Created by Vo Ngoc Hanh on 5/16/2018.
 */

public class ProfileResponse {
    @SerializedName("success")
    @Expose
    Boolean success;
    @SerializedName("user")
    @Expose
    User user;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
