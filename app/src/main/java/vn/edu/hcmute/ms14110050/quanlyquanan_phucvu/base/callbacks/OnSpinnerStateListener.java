package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public interface OnSpinnerStateListener {

    interface View{
        void onStartProcessDataChanged();

        void onEndProcessDataChanged();

        void onSelectSpinnerItemIndex(int index);
    }

    // đơn vị xử lý logic (thường là viewmodel)
    interface DataProcessor{

        void onSelectSpinnerItemId(String id);

    }

}
