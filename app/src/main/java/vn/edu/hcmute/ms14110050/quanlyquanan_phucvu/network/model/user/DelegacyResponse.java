package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.IWebData;

public class DelegacyResponse implements IWebData{
    @SerializedName("is_ok")
    @Expose
    private boolean isOk;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("fails")
    @Expose
    private String fails;
    @SerializedName("delegacy_username")
    @Expose
    private String delegacyUserName;
    @SerializedName("delegacy_fullname")
    @Expose
    private String delegacyFullName;
    @SerializedName("handover_username")
    @Expose
    private String handoverUserName;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFails() {
        return fails;
    }

    public void setFails(String fails) {
        this.fails = fails;
    }

    public String getDelegacyUserName() {
        return delegacyUserName;
    }

    public void setDelegacyUserName(String delegacyUserName) {
        this.delegacyUserName = delegacyUserName;
    }

    public String getDelegacyFullName() {
        return delegacyFullName;
    }

    public void setDelegacyFullName(String delegacyFullName) {
        this.delegacyFullName = delegacyFullName;
    }

    public String getHandoverUserName() {
        return handoverUserName;
    }

    public void setHandoverUserName(String handoverUserName) {
        this.handoverUserName = handoverUserName;
    }
}
