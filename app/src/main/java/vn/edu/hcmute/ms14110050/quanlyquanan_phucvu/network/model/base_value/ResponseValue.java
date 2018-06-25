package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vo Ngoc Hanh on 6/16/2018.
 */

public class ResponseValue {
    @SerializedName("success")
    @Expose
    protected Boolean success;

    @SerializedName("message")
    @Expose
    protected String message;

    @SerializedName("error")
    @Expose
    protected String error;

    public ResponseValue() {

    }

    public ResponseValue(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }
}
