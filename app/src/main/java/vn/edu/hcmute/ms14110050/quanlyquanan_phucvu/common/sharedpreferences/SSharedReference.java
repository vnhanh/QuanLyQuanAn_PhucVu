package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences;

import android.content.Context;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.BaseSharedReference.SR_AUTHENTICATION_TOKEN;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.BaseSharedReference.SR_MAIN;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.BaseSharedReference.SR_USERNAME;


public class SSharedReference {

    public static String getToken(Context context){
        return new BaseSharedReference.Builder()
                .init(context, SR_MAIN)
                .get()
                .getString(SR_AUTHENTICATION_TOKEN, BaseSharedReference.VALUE_DEFAULT_STR);
    }

    public static void setToken(Context context, String token){
        new BaseSharedReference.Builder()
                .init(context, SR_MAIN)
                .putString(SR_AUTHENTICATION_TOKEN, token)
                .save();
    }

    public static void clearToken(Context context) {
        new BaseSharedReference.Builder()
                .init(context, SR_MAIN)
                .delete(SR_AUTHENTICATION_TOKEN)
                .save();
    }

    public static String getUserName(Context context){
        return new BaseSharedReference.Builder()
                .init(context, SR_MAIN)
                .get()
                .getString(SR_USERNAME, BaseSharedReference.VALUE_DEFAULT_STR);
    }

    public static void setUserName(Context context, String userJson) {
        new BaseSharedReference.Builder()
                .init(context, SR_MAIN)
                .putString(SR_USERNAME, userJson)
                .save();
    }

    public static void clearUserName(Context context) {
        new BaseSharedReference.Builder()
                .init(context, SR_MAIN)
                .delete(SR_USERNAME)
                .save();
    }

}
