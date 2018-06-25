package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 *
 * Data mô tả khu vực
 */

public class Region implements Cloneable{
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("actived")
    @Expose
    boolean actived;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
