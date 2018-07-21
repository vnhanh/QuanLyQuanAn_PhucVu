package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable;

import android.util.Log;

import java.util.Observer;

import io.reactivex.observers.DisposableObserver;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;

public class RxObserver extends DisposableObserver<ResponseValue>{
    private Observer observer;
    private int tag;

    public RxObserver(Observer observer, int tag) {
        this.observer = observer;
        this.tag = tag;
    }

    @Override
    public void onNext(ResponseValue response) {
        SendObject<ResponseValue> object = new SendObject<>(tag, response);
        observer.update(null, object);
        observer = null;
    }

    @Override
    public void onError(Throwable e) {
        Log.d("LOG", getClass().getSimpleName() + ":retrofit:onError():" + e.getMessage());
        ResponseValue value = new ResponseValue(false, "Lỗi thao tác");
        value.setError(e.getMessage());
        SendObject<ResponseValue> object = new SendObject<>(tag, value);
        observer.update(null, object);
        observer = null;
    }

    @Override
    public void onComplete() {

    }
}
