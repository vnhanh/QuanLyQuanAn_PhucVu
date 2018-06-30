package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Completable;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.ApiUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.FoodService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.OrderService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.RegionTableService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.FullOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TablesResponse;

/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public class OrderRequestManager {
    private OrderService service;
    private FoodService foodService;
    private RegionTableService tableService;

    public OrderRequestManager() {
        createService();
    }

    private void createService() {
        if (service == null) {
            service = ApiUtil.getOrderService();
        }
        if (foodService == null) {
            foodService = ApiUtil.getFoodService();
        }
        if (tableService == null) {
            tableService = ApiUtil.getRegionTableService();
        }
    }

    private MaybeSource<OrderResponse> updateFoodForDetailOrder(String token, Map<String, Object> fields,
                                                                final GetCallback<OrderResponse> callback) {
        return service.updateOrCreateDetailOrder(token, fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                + ":updateFoodForDetailOrder():onError():" + throwable.getMessage());
                        OrderResponse response = new OrderResponse(false,throwable.getMessage());
                        callback.onFinish(response);
                    }
                })
                .doOnNext(new Consumer<OrderResponse>() {
                    @Override
                    public void accept(OrderResponse orderResponse) throws Exception {
                        callback.onFinish(orderResponse);
                    }
                }).singleElement();
    }

    private Completable updateFoodForOrder(String token, final Map<String, Object> fields,
                                           final GetCallback<FoodResponse> callback) {
        return service.updateFoodForOrder(token, fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        FoodResponse response = new FoodResponse();
                        response.setSuccess(false);
                        callback.onFinish(response);
                        fields.put("prevRequest", false);
                    }
                })
                .doOnNext(new Consumer<FoodResponse>() {
                    @Override
                    public void accept(FoodResponse response) throws Exception {
                        callback.onFinish(response);
                    }
                }).doOnNext(new Consumer<FoodResponse>() {
                    @Override
                    public void accept(FoodResponse response) throws Exception {
                        if (response.getSuccess()) {
                            Food food = response.getFood();
                            fields.put("foodName", food.getName());
                            fields.put("priceUnit", food.getUnitPrice());
                            fields.put("discount", food.getDiscount());
                            fields.put("prevRequest", false);
                        }else{
                            fields.put("prevRequest", false);
                        }
                    }
                })
                .ignoreElements();
    }

    public void orderFood(@NonNull final String token,
                          final Map<String, Object> fields,
                          final GetCallback<FoodResponse> foodCallback,
                          final GetCallback<OrderResponse> orderCallback) {

        service.updateFoodForOrder(token, fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        FoodResponse response = new FoodResponse();
                        response.setSuccess(false);
                        foodCallback.onFinish(response);
                    }
                })
                .doOnNext(new Consumer<FoodResponse>() {
                    @Override
                    public void accept(FoodResponse response) throws Exception {
                        foodCallback.onFinish(response);
                    }
                }).doOnNext(new Consumer<FoodResponse>() {
                    @Override
                    public void accept(FoodResponse response) throws Exception {
                        if (response.getSuccess()) {
                            Food food = response.getFood();
                            fields.put("foodName", food.getName());
                            fields.put("priceUnit", food.getUnitPrice());
                            fields.put("discount", food.getDiscount());
                            fields.put("inventory", food.getInventory());
                        }
                    }
                }).flatMap(new Function<FoodResponse, ObservableSource<OrderResponse>>() {
                    @Override
                    public ObservableSource<OrderResponse> apply(FoodResponse response) throws Exception {
                        if (response.getSuccess()) {
                            return service.updateOrCreateDetailOrder(token, fields)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                        }else{
                            Log.d("LOG", OrderRequestManager.class.getSimpleName() + ":updateFoodForOrder() failed");
                            OrderResponse _response = new OrderResponse(false, response.getMessage());
                            return Observable.just(_response);
                        }
                    }
                }).subscribe(new DisposableObserver<OrderResponse>() {
                    @Override
                    public void onNext(OrderResponse orderResponse) {
                        orderCallback.onFinish(orderResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                + ":updateFoodForDetailOrder():onError():" + e.getMessage());
                        OrderResponse response = new OrderResponse(false, "Lỗi xử lý");
                        orderCallback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void createOrder(String token, WeakHashMap<String, Object> fields, final GetCallback<OrderResponse> callback) {
        service.createOrder(token, fields)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OrderResponse responseValue) {
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                + ":createOrder():onError():" + e.getMessage());
                        OrderResponse response = new OrderResponse(false, "Lỗi xử lý");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void orderTable(final String token, final WeakHashMap<String, Object> fields, final GetCallback<TableResponse> callback) {

        service.orderTable(token, fields)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<TableResponse>() {
                    @Override
                    public void onNext(TableResponse tableResponse) {
                        Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                + ":updateTableForOrder():onNext()");
                        callback.onFinish(tableResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                + ":updateTableForOrder():onError():" + e.getMessage());
                        TableResponse responseValue = new TableResponse();
                        responseValue.setSuccess(false);
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void removeTableFromOrder(String token, final WeakHashMap<String, Object> fields, final GetCallback<TableResponse> callback){
        service.removeTableFromOrder(token, fields)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<TableResponse>() {
                    @Override
                    public void onNext(TableResponse tableResponse) {
                        callback.onFinish(tableResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        TableResponse response = new TableResponse();
                        response.setSuccess(false);
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getOrder(final String token, String id, final GetCallback<FullOrderResponse> callback) {
        service.getOrder(token, id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<OrderResponse, ObservableSource<FullOrderResponse>>() {
                    @Override
                    public ObservableSource<FullOrderResponse> apply(OrderResponse response) throws Exception {
                        if (response.getSuccess()) {
                            final Order order = response.getOrder();
                            if (order != null) {
                                List<Observable<FoodResponse>> foodSources = new ArrayList<>();

                                // load danh sách món
                                if (order.getDetailOrders() != null) {
                                    List<DetailOrder> details = order.getDetailOrders();
                                    for (DetailOrder detail : details) {
                                        foodSources.add(foodService.getFoodByID(token, detail.getFoodId())
                                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()));

                                    }
                                    Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                            + ":getOrder():get order:start loading foods in order:count of foods:" + details.size());
                                }

                                // load danh sách bàn
                                Observable<TablesResponse> tablesSource = tableService.loadTablesByOrderID(token, order.getId())
                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

                                List<Observable<? extends ResponseValue>> soucres = new ArrayList<>();
                                soucres.addAll(foodSources);
                                soucres.add(tablesSource);

                                return Observable.zip(soucres, new Function<Object[], FullOrderResponse>() {
                                    @Override
                                    public FullOrderResponse apply(Object[] objects) throws Exception {
                                        Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                                + ":getOrder():get order:get foods of order complete");
                                        FullOrderResponse _response = new FullOrderResponse();
                                        _response.setSuccess(true);
                                        _response.setOrder(order);
                                        for (Object object : objects) {
                                            if (object instanceof FoodResponse) {
                                                FoodResponse _foodRes = (FoodResponse) object;
                                                _response.getFoods().add(_foodRes.getFood());
                                            }else if(object instanceof TablesResponse){
                                                TablesResponse _tablesRes = (TablesResponse) object;
                                                _response.setTables(_tablesRes.getTables());
                                            }
                                        }
                                        return _response;
                                    }
                                });
                            }
                        }
                        Log.d("LOG", OrderRequestManager.class.getSimpleName() + ":get order:return empty FullOrderResponse");
                        FullOrderResponse _response = new FullOrderResponse(false, response.getMessage());
                        return Observable.just(_response);
                    }
                })
                .subscribe(new DisposableObserver<FullOrderResponse>() {
                    @Override
                    public void onNext(FullOrderResponse orderResponse) {
                        if (orderResponse.getFoods() != null) {
                            Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                    + ":get order:success:" + orderResponse.getSuccess() + ":count of details:" + orderResponse.getFoods().size());
                        }else{
                            Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                    + ":get order:success:" + orderResponse.getSuccess() + "no food");
                        }
                        callback.onFinish(orderResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestManager.class.getSimpleName()
                                + ":getOrder():failed:message:" + e.getMessage()
                                + ":type of exception:" + e.getClass().getSimpleName());
                        FullOrderResponse response = new FullOrderResponse(false, "Lỗi xử lý");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // TODO
    public void updateNumberCustomer(String token, WeakHashMap<String,Object> map, GetCallback<ResponseValue> callback) {
        service.updateNumberCustomer(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseValue>() {
                    @Override
                    public void onNext(ResponseValue responseValue) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
