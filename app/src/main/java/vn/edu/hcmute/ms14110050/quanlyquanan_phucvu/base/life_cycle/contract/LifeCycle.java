package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract;

import android.content.Context;
import android.support.annotation.NonNull;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IProgressView;

/**
 * Created by Vo Ngoc Hanh on 5/23/2018.
 */

public interface LifeCycle {
    interface View extends IProgressView {
        Context getContext();
    }

    interface ViewModel<V extends View>{
        void onViewAttach(@NonNull V viewCallback);

        void onViewResumed();

        void onViewDetached();

        void onDestroy();
    }
}
