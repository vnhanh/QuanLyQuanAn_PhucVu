package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;


public class BaseSharedReference {
    public static final String SR_MAIN = "QUAN LY QUAN AN";
    public static final int VALUE_DEFAULT_INT = 0;
    public static final String VALUE_DEFAULT_STR = "";

    public static final String SR_AUTHENTICATION_TOKEN = "SR_AUTHENTICATION_TOKEN";
    public static final String SR_USERNAME = "SR_USERNAME";

    public static class Builder {
        Context context;
        String name;
        SharedPreferences pre;
        SharedPreferences.Editor crea;

        public Builder init(Context context, String name){
            this.context = context;
            this.name = name;
            pre = context.getSharedPreferences (
                    name
                    ,MODE_PRIVATE
            );
            crea = pre.edit();
            return this;
        }

        public Builder putString(String key, String value){
            crea.putString(key,value);
            return this;
        }

        public Builder putInt(String key, int value){
            crea.putInt(key,value);
            return this;
        }

        public Builder putBoolean(String key, boolean value){
            crea.putBoolean(key,value);
            return this;
        }

        public Builder putFloat(String key, float value){
            crea.putFloat(key,value);
            return this;
        }

        public Builder putLong(String key, long value){
            crea.putLong(key,value);
            return this;
        }

        public Builder save(){
            crea.commit();
            return this;
        }

        public Builder delete(String key) {
            crea.remove(key);
            return this;
        }

        public SharedPreferences get(){
            return pre;
        }
    }
}
