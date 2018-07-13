package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

public class FindAccountResponse extends ResponseValue {
    @SerializedName("username")
    @Expose
    protected String username;
    @SerializedName("fullname")
    @Expose
    protected String fullname;
    @SerializedName("birthdate")
    @Expose
    protected String birthdate;
    @SerializedName("gender")
    @Expose
    protected boolean gender;
    @SerializedName("url_profile")
    @Expose
    protected String profileUrlImg;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getProfileUrlImg() {
        return profileUrlImg;
    }

    public void setProfileUrlImg(String profileUrlImg) {
        this.profileUrlImg = profileUrlImg;
    }
}
