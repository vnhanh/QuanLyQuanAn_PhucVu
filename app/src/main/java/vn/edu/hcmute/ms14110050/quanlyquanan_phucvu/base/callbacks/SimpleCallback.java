package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks;

/**
 * Created by Vo Ngoc Hanh on 5/21/2018.
 */

public interface SimpleCallback<DATA>{
    void onSuccess(DATA data);

    void onError();
}
