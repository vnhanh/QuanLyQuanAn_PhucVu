package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TYPE_USER.ADMIN;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TYPE_USER.CASHIER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TYPE_USER.CHIEF;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TYPE_USER.CUSTOMER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TYPE_USER.MANAGER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TYPE_USER.WAITER;

/**
 * Created by Vo Ngoc Hanh on 6/14/2018.
 */

@IntDef({CUSTOMER,WAITER,CASHIER,CHIEF,MANAGER,ADMIN})
@Retention(RetentionPolicy.SOURCE)
public @interface TYPE_USER {
    int CUSTOMER = 0;
    int WAITER = 1;
    int CASHIER = 2;
    int CHIEF = 3;
    int MANAGER = 4;
    int ADMIN = 5;
}