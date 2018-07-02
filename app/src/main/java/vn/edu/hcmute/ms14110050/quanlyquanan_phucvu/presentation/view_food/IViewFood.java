package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food;

import android.view.View;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;

/**
 * Created by Vo Ngoc Hanh on 6/27/2018.
 */

public interface IViewFood extends LifeCycle.View {
    void onBackPrevActivity();

    // Nhập số lượng món cần đặt
    void openInputOrderCountDialog(int oldCount, InputCallback callback);

    View getViewUI();
}
