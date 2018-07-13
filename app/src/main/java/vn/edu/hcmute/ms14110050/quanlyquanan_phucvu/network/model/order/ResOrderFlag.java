package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.ResOrderFlag.RESPONSE_HANDOVER;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.ResOrderFlag.RESQUEST_HANDOVER;

@IntDef({RESQUEST_HANDOVER, RESPONSE_HANDOVER})
@Retention(RetentionPolicy.SOURCE)
public @interface ResOrderFlag {
    int RESQUEST_HANDOVER = 0;
    int RESPONSE_HANDOVER = 1;
}
