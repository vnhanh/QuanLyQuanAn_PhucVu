package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.ResAccFlag.LOGIN;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.ResAccFlag.LOGOUT;

@IntDef({LOGIN, LOGOUT})
@Retention(RetentionPolicy.SOURCE)
public @interface ResAccFlag {
    int LOGIN = 0;
    int LOGOUT = 1;
}
