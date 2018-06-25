package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class RegionsResponse extends ResponseValue {
    @SerializedName("region")
    @Expose
    private ArrayList<Region> regions;

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }
}
