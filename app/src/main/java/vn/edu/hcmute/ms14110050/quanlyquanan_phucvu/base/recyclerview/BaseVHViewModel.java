package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class BaseVHViewModel<V extends IViewHolder> {
    V view;

    public void attachView(V view) {
        this.view = view;
    }

    protected V getView() {
        return view;
    }

    protected boolean isViewAttached() {
        return view != null;
    }
}
