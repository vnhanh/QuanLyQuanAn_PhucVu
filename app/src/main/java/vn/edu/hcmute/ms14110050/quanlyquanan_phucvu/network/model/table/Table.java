package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

public class Table implements Serializable{
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("region_id")
    @Expose
    String regionID;
    @SerializedName("order_id")
    @Expose
    String orderID;
    @SerializedName("actived")
    @Expose
    boolean actived;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public boolean isActived() {
        return actived;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public boolean equalValue(Table another) {
        if (another == null) {
            return false;
        }
        if (isActived() != another.isActived()) {
            return false;
        }
        if ((orderID == null && another.getOrderID() != null) || (orderID != null && another.getOrderID() == null)) {
            return false;
        }
        if (!orderID.equals(another.getOrderID())) {
            return false;
        }
        if ((regionID == null && another.getRegionID() != null) || (regionID != null && another.getRegionID() == null)) {
            return false;
        }
        if (!regionID.equals(another.getRegionID())) {
            return false;
        }
        return true;
    }
}
