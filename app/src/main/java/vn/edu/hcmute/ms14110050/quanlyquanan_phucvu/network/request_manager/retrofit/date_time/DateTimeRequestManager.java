package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.date_time;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.DateTimeApi;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.DateTimeService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.date_time.TimeZone;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public class DateTimeRequestManager {
    private DateTimeService service;

    public DateTimeRequestManager() {
        service = new DateTimeApi().getService();
    }

    public void getLocalTime(final GetCallback<String> callback) {
        service.getLocalTimeZone()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TimeZone>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TimeZone timeZone) {
                        if (timeZone.getStatus().equals("OK")) {
                            callback.onFinish(timeZone.getFormatted());
                        }else{
                            Log.d("LOG", DateTimeRequestManager.class.getSimpleName()
                                    + ":getLocalTime():onNext():failed:" + timeZone.getMessage());
                            callback.onFinish(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", DateTimeRequestManager.class.getSimpleName()
                                + ":getLocalTime():onError():" + e.getMessage());
                        callback.onFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
