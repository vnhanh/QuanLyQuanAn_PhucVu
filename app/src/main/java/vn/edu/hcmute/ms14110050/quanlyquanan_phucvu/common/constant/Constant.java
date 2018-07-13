package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant;


import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.IntegerUnique;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.TypeAcc;

/**
 * Created by Vo Ngoc Hanh on 5/21/2018.
 */

public class Constant {
    public static final int TAG_VALIDATE = IntegerUnique.generate();
    public static final int NATIVE_TYPE_USER = TypeAcc.WAITER;

    public static final int COLOR_ERROR = R.color.colorLightRed;
    public static final int COLOR_SUCCESS = R.color.colorWhite;
    public static final int COLOR_WARNING = R.color.colorYellow;

    public static final String TAG_DIALOG = "dialog";
}
