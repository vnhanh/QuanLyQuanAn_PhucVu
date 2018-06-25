package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit;

import java.util.HashMap;

import io.reactivex.Completable;
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
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.RegionsResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TablesResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user.User;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public interface RegionTableService {

    @GET("region/allRegions")
    Observable<RegionsResponse> loadRegions(@Header("Authorization") String token);

    @GET("table/allTables/{regionID}")
    Observable<TablesResponse> loadTables(@Header("Authorization") String token, @Path("regionID") String regionID);

    @POST("table/addTableToOrder")
    @FormUrlEncoded
    Observable<TableResponse> addTableToOrder(@Header("Authorization") String token,
                                              @Field("id") String tableID, @Field("order_id") String orderID);

    @POST("table/removeTableFromOrder")
    @FormUrlEncoded
    Observable<TableResponse> removeTableFromOrder(@Header("Authorization") String token,
                                                 @Field("id") String tableID, @Field("order_id") String orderID);

    @GET("table/getTables/{orderID}")
    Observable<TablesResponse> loadTablesByOrderID(@Header("Authorization") String token, @Path("orderID") String orderID);
}
