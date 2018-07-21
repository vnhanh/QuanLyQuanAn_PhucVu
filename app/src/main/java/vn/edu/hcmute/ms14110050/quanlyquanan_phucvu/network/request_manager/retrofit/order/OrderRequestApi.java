package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
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
import io.reactivex.subscribers.DisposableSubscriber;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.ApiUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.FoodService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.OrderService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.retrofit.TableService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.base_value.ResponseValue;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.FullOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrderFoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.OrdersResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.PayableOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateDetailOrderStatusResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.response.UpdateStatusOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TableResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.TablesResponse;

/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

public class OrderRequestApi {
    public final int TAG_ORDER_FOOD = 0;

    private OrderService service;
    private FoodService foodService;
    private TableService tableService;

    public OrderRequestApi() {
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

    public Disposable orderFood(@NonNull final String token,
                          final Map<String, Object> fields,
                          DisposableObserver<ResponseValue> observer) {

        return service.updateFoodForOrder(token, fields)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<OrderFoodResponse, ObservableSource<OrderFoodResponse>>() {
                    @Override
                    public ObservableSource<OrderFoodResponse> apply(OrderFoodResponse response) throws Exception {
                        response.setTag(TAG_ORDER_FOOD);
                        return Observable.just(response);
                    }
                })
                .subscribeWith(observer);
    }

    public Disposable makeOrder(String token, String orderID, final GetCallback<UpdateStatusOrderResponse> callback){

        return service.makeOrder(token, orderID)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UpdateStatusOrderResponse>() {
                    @Override
                    public void onNext(UpdateStatusOrderResponse response) {
                        callback.onFinish(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":makeOrder():onError():" + e.getMessage());
                        UpdateStatusOrderResponse response = new UpdateStatusOrderResponse(false, "Lỗi xử lý");
                        callback.onFinish(response);
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
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":createOrder():onError():" + e.getMessage());
                        OrderResponse response = new OrderResponse(false, "Lỗi xử lý");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Disposable orderTable(String token, String orderID, final String tableID,
                           final GetCallback<TableResponse> callback) {

        WeakHashMap<String, Object> map = new WeakHashMap<>();
        map.put("orderID", orderID);
        map.put("tableID", tableID);

        return service.orderTable(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TableResponse>() {
                    @Override
                    public void onNext(TableResponse tableResponse) {
                        callback.onFinish(tableResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":updateTableForOrder():onError():" + e.getMessage());

                        TableResponse responseValue = new TableResponse(false, "Lỗi xử lý");
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Disposable removeTableFromOrder(String token, final WeakHashMap<String, Object> fields, final GetCallback<TableResponse> callback){
        return service.removeTableFromOrder(token, fields)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TableResponse>() {
                    @Override
                    public void onNext(TableResponse tableResponse) {
                        callback.onFinish(tableResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        TableResponse response = new TableResponse(false, "Lỗi xử lý");
                        response.setSuccess(false);
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Flowable<FullOrderResponse> loadOrder(String token, String orderID){
        return service.loadOrder(token, orderID)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    public void getOrder(final String token, String id, final GetCallback<FullOrderResponse> callback) {
        service.getOrder(token, id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<OrderResponse, ObservableSource<FullOrderResponse>>() {
                    @Override
                    public ObservableSource<FullOrderResponse> apply(OrderResponse response) throws Exception {
                        if (response.isSuccess()) {
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
                        FullOrderResponse _response = new FullOrderResponse(false, response.getMessage());
                        return Observable.just(_response);
                    }
                })
                .subscribe(new DisposableObserver<FullOrderResponse>() {
                    @Override
                    public void onNext(FullOrderResponse orderResponse) {
                        if (orderResponse.getFoods() != null) {
                        }else{
                            Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                    + ":get order:is success:" + orderResponse.isSuccess() + "no food");
                        }
                        callback.onFinish(orderResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
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

    public Disposable updateNumberCustomer(String token, WeakHashMap<String,Object> map, final GetCallback<OrderResponse> callback) {

        return service.updateNumberCustomer(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OrderResponse>() {
                    @Override
                    public void onNext(OrderResponse response) {
                        callback.onFinish(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":updateNumberCustomer():error:" + e.getMessage());
                        OrderResponse response = new OrderResponse(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Disposable updateDescription(String token, WeakHashMap<String, Object> map, final GetCallback<ResponseValue> callback) {

        return service.updateDescription(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseValue>() {
                    @Override
                    public void onNext(ResponseValue responseValue) {
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":updateDescription():error:" + e.getMessage());
                        ResponseValue response = new ResponseValue(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Flowable<OrderResponse> updateStatusOrder(String token, WeakHashMap<String, Object> map) {

        return service.updateStatusOrder(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    public void updateStatusOrder(String token, WeakHashMap<String, Object> map, final GetCallback<OrderResponse> callback) {
        service.updateStatusOrder(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<OrderResponse>() {
                    @Override
                    public void onNext(OrderResponse responseValue) {
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":updateStatusOrder():error:" + e.getMessage());
                        OrderResponse response = new OrderResponse(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void setPayableOrder(String token, WeakHashMap<String, Object> map, final GetCallback<PayableOrderResponse> callback) {
        service.setPayableOrder(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<PayableOrderResponse>() {
                    @Override
                    public void onNext(PayableOrderResponse responseValue) {
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":updateStatusOrder():error:" + e.getMessage());
                        PayableOrderResponse response = new PayableOrderResponse(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Disposable restoreOrder(String token, WeakHashMap<String, Object> map, final GetCallback<ResponseValue> callback) {

        return service.removeOrder(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseValue>() {
                    @Override
                    public void onNext(ResponseValue responseValue) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":restoreOrder():finish:success:" + responseValue.isSuccess()
                                + ":message:" + responseValue.getMessage());
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":restoreOrder():error:" + e.getMessage());
                        ResponseValue response = new ResponseValue(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    // Get tất cả order có thể phục vụ
    public void getOrdersForWaiter(String token, final GetCallback<OrdersResponse> callback) {

        service.getOrdersWaiting(token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<OrdersResponse>() {
                    @Override
                    public void onNext(OrdersResponse response) {
                        callback.onFinish(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":restoreOrder():error:" + e.getMessage());

                        OrdersResponse response = new OrdersResponse(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void suggestDelegacy(String token, WeakHashMap<String, Object> map,
                                final GetCallback<ResponseValue> callback) {
        service.suggestDelegacy(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseValue>() {
                    @Override
                    public void onNext(ResponseValue responseValue) {
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":restoreOrder():error:" + e.getMessage());

                        ResponseValue response = new ResponseValue(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void agreeBecomeDelegacy(String token, WeakHashMap<String, Object> map,
                                final GetCallback<ResponseValue> callback) {
        service.agreeBecomeDelegacy(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseValue>() {
                    @Override
                    public void onNext(ResponseValue responseValue) {
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":agreeBecomeDelegacy():error:" + e.getMessage());

                        ResponseValue response = new ResponseValue(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void disagreeBecomeDelegacy(String token, WeakHashMap<String, Object> map,
                                final GetCallback<ResponseValue> callback) {
        service.disagreeBecomeDelegacy(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseValue>() {
                    @Override
                    public void onNext(ResponseValue responseValue) {
                        callback.onFinish(responseValue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", OrderRequestApi.class.getSimpleName()
                                + ":disagreeBecomeDelegacy():error:" + e.getMessage());

                        ResponseValue response = new ResponseValue(false, "");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Flowable<UpdateDetailOrderStatusResponse> updateDetailOrder(String token, WeakHashMap<String, Object> map){
        return service.updateDetailOrderStatus(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    public void removeStatusDetailOrder(String token, WeakHashMap<String,Object> map, GetCallback<OrderResponse> callback) {
        service.removeStatusDetailOrder(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<OrderResponse>() {
                    @Override
                    public void onNext(OrderResponse response) {
                        callback.onFinish(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOG", getClass().getSimpleName() + ":removeStatusDetailOrder():error:" + e.getMessage());
                        OrderResponse response = new OrderResponse(false, "Lỗi");
                        callback.onFinish(response);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Flowable<ResponseValue> reProcessOrder(String token, WeakHashMap<String, Object> map){
        return service.reProcessOrder(token, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);
    }

}
