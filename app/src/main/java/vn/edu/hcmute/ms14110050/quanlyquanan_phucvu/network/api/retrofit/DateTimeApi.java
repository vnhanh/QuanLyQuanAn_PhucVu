package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public class DateTimeApi {
    private final String TIME_SERVER = "http://api.timezonedb.com/";

    private Retrofit datTimeRetrofit;

    public DateTimeApi() {
        datTimeRetrofit = new Retrofit.Builder().baseUrl(TIME_SERVER)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public DateTimeService getService() {
        return datTimeRetrofit.create(DateTimeService.class);
    }
}
