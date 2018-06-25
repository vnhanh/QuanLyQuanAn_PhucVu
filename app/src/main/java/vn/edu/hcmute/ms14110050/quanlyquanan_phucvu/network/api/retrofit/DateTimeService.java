package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.GetProfileResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.date_time.TimeZone;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public interface DateTimeService {

    @GET("v2/get-time-zone?key=AAX87SSG8BKZ&format=json&by=zone&zone=Asia/Ho_Chi_Minh")
    Observable<TimeZone> getLocalTimeZone();
}
