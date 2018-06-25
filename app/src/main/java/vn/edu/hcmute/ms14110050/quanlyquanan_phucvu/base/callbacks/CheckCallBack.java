package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks;

/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public interface CheckCallBack<DATA_ON_TRUE, DATA_ON_FAILED, DATA_ON_ERROR> {
    void onRight(DATA_ON_TRUE data);

    void onWrong(DATA_ON_FAILED data);

    void onError(DATA_ON_ERROR data);
}
