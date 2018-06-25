package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode.ADD;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.OrderMode.VIEW;

/**
 * Created by Vo Ngoc Hanh on 6/17/2018.
 */

@IntDef({ADD, VIEW})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderMode {
    int ADD = 0;
    int VIEW = 1;
}
