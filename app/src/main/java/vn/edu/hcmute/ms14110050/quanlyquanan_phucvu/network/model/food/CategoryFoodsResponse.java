package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class CategoryFoodsResponse extends ResponseValue {

    @SerializedName("categoryfoods")
    @Expose
    protected ArrayList<CategoryFood> categoryfoods;

    public ArrayList<CategoryFood> getCategoryfoods() {
        return categoryfoods;
    }

    public void setCategoryfoods(ArrayList<CategoryFood> categoryfoods) {
        this.categoryfoods = categoryfoods;
    }
}
