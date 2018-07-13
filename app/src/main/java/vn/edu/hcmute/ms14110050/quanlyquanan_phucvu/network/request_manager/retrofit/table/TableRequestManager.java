package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.table;

import android.util.Log;

import java.util.ArrayList;
import java.util.WeakHashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.ApiUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.TableService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.Region;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.RegionsResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TablesResponse;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class TableRequestManager {
    private TableService service;

    public TableRequestManager() {
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
                        if (response.isSuccess()) {
                            callback.onFinish(response.getRegions());
                        } else {
                            Log.d("LOG", TableRequestManager.class.getSimpleName()
                                    + ":loadRegions():failed:" + response.getMessage());
                            callback.onFinish(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", TableRequestManager.class.getSimpleName()
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
                        if (response.isSuccess()) {
                            callback.onFinish(response.getTables());
                        }else{
                            callback.onFinish(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", TableRequestManager.class.getSimpleName()
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
                        Log.d("LOG", TableRequestManager.class.getSimpleName()
                                + ":loadTables by OrderID:onFinish():success" + tablesResponse.isSuccess()
                                +":size of result:" + (tablesResponse.getTables() != null ? tablesResponse.getTables().size() : 0));
                        callback.onFinish(tablesResponse.getTables());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", TableRequestManager.class.getSimpleName()
                                + ":loadTables by OrderID:onError():" + e.getMessage());
                        callback.onFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void orderTable(String token, String orderID, final String tableID,
                           final GetCallback<TableResponse> callback) {

        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("order_id", orderID);
        map.put("table_id", tableID);

        service.orderTable(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<TableResponse>() {
                    @Override
                    public void onNext(TableResponse response) {
                        callback.onFinish(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", TableRequestManager.class.getSimpleName()
                                + ":loadTables by OrderID:onError():" + e.getMessage());

                        TableResponse response = new TableResponse(false, "Lỗi xử lý");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void removeTableFromOrder(String token, String orderID, final String tableID,
                           final GetCallback<TableResponse> callback) {

        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("order_id", orderID);
        map.put("table_id", tableID);

        service.removeTableFromOrder(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<TableResponse>() {
                    @Override
                    public void onNext(TableResponse response) {
                        callback.onFinish(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", TableRequestManager.class.getSimpleName()
                                + ":removeTableFromOrder():onError():" + e.getMessage());

                        TableResponse response = new TableResponse(false, "Lỗi xử lý");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
