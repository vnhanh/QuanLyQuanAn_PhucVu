package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestDelegacy {
    @SerializedName("delegacy_username")
    @Expose
    private String delegacyUserName;
    @SerializedName("handover_username")
    @Expose
    private String handoverUserName;
    @SerializedName("handover_fullname")
    @Expose
    private String handoverFullName;

    public String getDelegacyUserName() {
        return delegacyUserName;
    }

    public void setDelegacyUserName(String delegacyUserName) {
        this.delegacyUserName = delegacyUserName;
    }

    public String getHandoverUserName() {
        return handoverUserName;
    }

    public void setHandoverUserName(String handoverUserName) {
        this.handoverUserName = handoverUserName;
    }

    public String getHandoverFullName() {
        return handoverFullName;
    }

    public void setHandoverFullName(String handoverFullName) {
        this.handoverFullName = handoverFullName;
    }
}
