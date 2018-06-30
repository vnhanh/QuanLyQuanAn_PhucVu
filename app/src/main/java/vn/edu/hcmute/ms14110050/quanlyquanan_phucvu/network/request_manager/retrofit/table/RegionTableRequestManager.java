package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.table;

import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.ApiUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.RegionTableService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.Region;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.RegionsResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TablesResponse;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class RegionTableRequestManager {
    private RegionTableService service;

    public RegionTableRequestManager() {
        createService();
    }

    private void createService() {
        if (service == null) {
            service = ApiUtil.getRegionTableService();
        }
    }

    public void loadRegions(String token, final GetCallback<ArrayList<Region>> callback) {
        service.loadRegions(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegionsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RegionsResponse response) {
                        if (response.getSuccess()) {
                            callback.onFinish(response.getRegions());
                        } else {
                            Log.d("LOG", RegionTableRequestManager.class.getSimpleName()
                                    + ":loadRegions():failed:" + response.getMessage());
                            callback.onFinish(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", RegionTableRequestManager.class.getSimpleName()
                                + ":loadRegions():onError():" + e.getMessage());
                        callback.onFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadTablesByRegionID(String token, String regionID, final GetCallback<ArrayList<Table>> callback) {
        service.loadTables(token, regionID)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TablesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TablesResponse response) {
                        if (response.getSuccess()) {
                            callback.onFinish(response.getTables());
                        }else{
                            callback.onFinish(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", RegionTableRequestManager.class.getSimpleName()
                                + ":loadTables():onError():" + e.getMessage());
                        callback.onFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadListSelectedTable(String token, String orderID, final GetCallback<ArrayList<Table>> callback) {
        service.loadTablesByOrderID(token, orderID)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TablesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TablesResponse tablesResponse) {
                        Log.d("LOG", RegionTableRequestManager.class.getSimpleName()
                                + ":loadTables by OrderID:onFinish():success" + tablesResponse.getSuccess()
                                +":size of result:" + (tablesResponse.getTables() != null ? tablesResponse.getTables().size() : 0));
                        callback.onFinish(tablesResponse.getTables());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", RegionTableRequestManager.class.getSimpleName()
                                + ":loadTables by OrderID:onError():" + e.getMessage());
                        callback.onFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
