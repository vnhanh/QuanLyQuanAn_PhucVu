package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.COMPLETE;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.COOKING;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.CREATING;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.PAYING;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.PENDING;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag.RUNNING;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

@IntDef({CREATING, PENDING, COOKING, RUNNING, PAYING, COMPLETE})
@Retention(RetentionPolicy.SOURCE)
public @interface OrderFlag {
    int CREATING = 0; // đang tạo
    int PENDING = 1; // tạo xong, đang chờ bếp xác nhận
    int COOKING = 2; // bếp xác nhận, đang nấu
    int RUNNING = 3; // nấu xong, dọn ra, khách đang ăn
    int PAYING = 4; // khách xác nhận muốn thanh toán
    int COMPLETE = 5; // thanh toán
}
