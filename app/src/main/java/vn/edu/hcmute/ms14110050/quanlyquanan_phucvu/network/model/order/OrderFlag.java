package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.COMPLETE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.CREATING;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.PENDING;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.RUNNING;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

@IntDef({CREATING, PENDING, RUNNING, COMPLETE})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderFlag {
    int CREATING = 0;
    int PENDING = 1;
    int RUNNING = 2;
    int COMPLETE = 3;
}
