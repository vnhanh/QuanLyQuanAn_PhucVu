package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class GetProfileResponse extends ResponseValue{

    @SerializedName("employee")
    @Expose
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
