package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.ObserIndex.RX_RESPONSE;

@IntDef({RX_RESPONSE})
@Retention(RetentionPolicy.SOURCE)
public @interface ObserIndex {
    int RX_RESPONSE = 0;
}
