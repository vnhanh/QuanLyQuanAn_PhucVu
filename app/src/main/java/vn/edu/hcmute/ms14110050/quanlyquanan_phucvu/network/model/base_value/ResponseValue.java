package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vo Ngoc Hanh on 6/16/2018.
 */

public class ResponseValue {
    @SerializedName("success")
    @Expose
    protected boolean success;

    @SerializedName("message")
    @Expose
    protected String message;

    @SerializedName("error")
    @Expose
    protected String error;

    // cờ đánh dấu
    protected int tag;

    public ResponseValue() {

    }

    public ResponseValue(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @NonNull
    public String getMessage() {
        if (message == null) {
            message = "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    public String getError() {
        if (error == null) {
            error = "";
        }
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
