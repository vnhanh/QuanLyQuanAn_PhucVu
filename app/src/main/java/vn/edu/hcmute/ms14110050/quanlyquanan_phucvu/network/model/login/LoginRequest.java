package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Vo Ngoc Hanh on 5/14/2018.
 */

public class LoginRequest {
    String username;
    String password;
    @SerializedName("type_account")
    @Expose
    int typeUser;

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

    public int getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(int typeUser) {
        this.typeUser = typeUser;
    }
}
