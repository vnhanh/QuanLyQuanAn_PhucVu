package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vo Ngoc Hanh on 5/15/2018.
 */

public class User implements Cloneable{
    @SerializedName("_id")
    @Expose
    String _id = "";
    @SerializedName("id")
    @Expose
    String id = "";
    @SerializedName("email")
    @Expose
    String email = "";
    @SerializedName("username")
    @Expose
    String username = "";
    @SerializedName("fullname")
    @Expose
    String fullname = "";
    @SerializedName("gender")
    @Expose
    boolean gender;
    @SerializedName("identity_card")
    @Expose
    String identityCard = "";
    @SerializedName("phone")
    @Expose
    String phone = "";
    @SerializedName("birthdate")
    @Expose
    String birthDate = "";
    @SerializedName("url_profile")
    @Expose
    String urlImgProfile = "";
    @SerializedName("address")
    @Expose
    String address = "";
    @TypeAcc
    @SerializedName("type_account")
    @Expose
    int typeUser;

    @SerializedName("actived")
    @Expose
    boolean actived;

    public String getBirthDateStrValue() {
        DateFormat fromDate = new SimpleDateFormat("yyyy-MM-dd");
        fromDate.setLenient(true);
        DateFormat toDate = new SimpleDateFormat("dd/MM/yyyy");
        toDate.setLenient(true);
        try {
            Date date = fromDate.parse(birthDate);
            return toDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("LOG", getClass().getSimpleName() + ":getBirthDateStrValue():exception:" + e.getMessage());
            return "";
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getUrlImgProfile() {
        return urlImgProfile;
    }

    public void setUrlImgProfile(String urlImgProfile) {
        this.urlImgProfile = urlImgProfile;
    }

    public int getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(int typeUser) {
        this.typeUser = typeUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null && !(obj instanceof User)) {
            return false;
        }
        User _another = (User) obj;
        return (_id==null && _another.get_id() == null) || (_id != null && _id.equals(_another.get_id()))
                && ((id==null && _another.getId() == null) || (id != null && id.equals(_another.getId())))
                && ((email==null && _another.getEmail() == null) || (email != null && email.equals(_another.getEmail())))
                && ((username==null && _another.getUsername() == null) || (username != null && username.equals(_another.getUsername())))
                && ((fullname==null && _another.getFullname() == null) || (fullname != null && fullname.equals(_another.getFullname())))
                && gender == _another.isGender()
                && ((identityCard==null && _another.getIdentityCard() == null) || (identityCard != null && identityCard.equals(_another.getIdentityCard())))
                && ((phone==null && _another.getPhone() == null) || (phone != null && phone.equals(_another.getPhone())))
                && ((birthDate==null && _another.getBirthDate() == null) || (birthDate != null && birthDate.equals(_another.getBirthDate())))
                && ((urlImgProfile==null && _another.getUrlImgProfile() == null) || (urlImgProfile != null && urlImgProfile.equals(_another.getUrlImgProfile())))
                && ((address==null && _another.getAddress() == null) || (address != null && address.equals(_another.getAddress())))
                && typeUser == _another.getTypeUser()
                && actived == _another.isActived();
    }
}
