package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.ChangePasswordRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.account.GetProfileResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginRequest;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.LoginResponseData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.login.ProfileResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.FindAccountResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;

/**
 * Created by Vo Ngoc Hanh on 5/14/2018.
 */

public interface AuthenticationService {

    @POST("authentication/login")
    @Headers("Content-Type: application/json")
    Observable<LoginResponseData> login(@Body LoginRequest request);

    @GET("authentication/employee/{username}")
    Observable<GetProfileResponse> getProfile(@Header("Authorization") String token, @Path("username") String username);

    @PUT("authentication/updateEmployee")
    Observable<ResponseValue> updateProfile(@Header("Authorization") String token, @Body User user);

    @POST("authentication/verify")
    Observable<ResponseValue> verify(@Header("Authorization") String token, @Body LoginRequest request);

    @PUT("authentication/updatePassword")
    Observable<ResponseValue> updatePassword(@Header("Authorization") String token, @Body ChangePasswordRequest request);

    @POST("authentication/findAccount")
    @FormUrlEncoded
    Observable<FindAccountResponse> findAccount(@Header("Authorization") String token,
                                                @Field("username") String username, @Field("type_account") int typeAccount);

    @POST("authentication/logout")
    @FormUrlEncoded
    Observable<ResponseValue> logout(@Header("Authorization") String token, @Field("username") String username);

}
