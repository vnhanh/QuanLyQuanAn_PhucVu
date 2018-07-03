package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant;


import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.IntegerUnique;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TYPE_USER;

/**
 * Created by Vo Ngoc Hanh on 5/21/2018.
 */

public class Constant {
    public static final int TAG_VALIDATE = IntegerUnique.generate();
    public static final int NATIVE_TYPE_USER = TYPE_USER.WAITER;

    public static final int COLOR_ERROR = R.color.colorRed;
    public static final int COLOR_SUCCESS = R.color.colorWhite;
    public static final int COLOR_WARNING = R.color.colorYellow;
}
