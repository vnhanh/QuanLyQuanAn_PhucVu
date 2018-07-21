package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.network;

import android.content.Context;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;

public abstract class BaseRequestManager {
    protected Context context;
    protected String token;

    protected void getToken() {
        if (token == null && context != null) {
            token = SSharedReference.getToken(context);
        }
    }

    public void onStart(Context context) {
        this.context = context;
    }

    public void onStop() {
        this.context = null;
    }

}
