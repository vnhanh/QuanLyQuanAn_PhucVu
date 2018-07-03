package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IListAdapterListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;

/**
 * Created by Vo Ngoc Hanh on 6/26/2018.
 */

public class FoodSocketListener {
    private IListAdapterListener<Food> foodDataListener;
    private Order order;
    private IOrderVM centerVM;

    private FoodSocketService socketService;

    public FoodSocketListener() {
        createSocketService();
    }

    private void createSocketService() {
        if (socketService == null) {
            socketService = new FoodSocketService();
        }
    }

    public FoodSocketService getSocketService() {
        return socketService;
    }

    public void destroy() {
        order = null;
        centerVM = null;
        socketService.destroy();
    }

    private void updateFoodInfo(Food food) {
        ArrayList<DetailOrder> details = order.getDetailOrders();
        int size = details.size();
        for (int i = 0; i < size; i++) {
            DetailOrder detail = details.get(i);
            if (detail.getFoodId().equals(order.getId())) {

                long oldUnitPrice = detail.getUnitPrice();
                long oldDiscount = detail.getDiscount();
                long newUnitPrice = food.getUnitPrice();
                long newDiscount = food.getDiscount();
                if (oldUnitPrice != newUnitPrice
                        || oldDiscount != newDiscount) {

                    // tính lại final cost
                    long _oldCash = (oldUnitPrice - oldDiscount) * detail.getCount();
                    long _newCash = (newUnitPrice - newDiscount) * detail.getCount();
                    long prevCash = order.getFinalCost();
                    order.setFinalCost(prevCash + _newCash - _oldCash);
                    detail.setUnitPrice(food.getUnitPrice());
                    detail.setDiscount(food.getDiscount());
                }
                detail.setFoodName(food.getName());

                return;
            }
        }
    }

    private void removeFoodFromOrder(Food food) {
        ArrayList<DetailOrder> details = order.getDetailOrders();
        int size = details.size();
        for (int i = 0; i < size; i++) {
            DetailOrder detail = details.get(i);
            if (detail.getFoodId().equals(order.getId())) {
                // tính lại final cost
                long oldFinalCost = order.getFinalCost();
                long newFinalCost = oldFinalCost - (detail.getCount() * (detail.getUnitPrice() - detail.getDiscount()));
                order.setFinalCost(newFinalCost);
                details.remove(i);

                return;
            }
        }
    }

    public void listenSockets(@NonNull IListAdapterListener<Food> dataListener, @NonNull IOrderVM centerVM) {
        this.centerVM = centerVM;
        this.foodDataListener = dataListener;
        this.order = centerVM.getOrder();

        socketService.onEventUpdateFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodDataListener != null) {
                    // tìm thấy item trong listview
                    if (foodDataListener.onUpdateItem(food)) {
                        updateFoodInfo(food);
                    }
                }
            }
        });
        socketService.onEventOrderFood(new GetCallback<FoodOrderSocketData>() {
            @Override
            public void onFinish(FoodOrderSocketData data) {
                // chỉ thay đổi thông tin lượng tồn kho
                if (data.getOrderID() != null && !data.getOrderID().equals(order.getId())) {
                    foodDataListener.onUpdateItem(data.getFood());
                }
            }
        });
        socketService.onEventAddImageFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodDataListener != null) {
                    foodDataListener.onUpdateItem(food);
                }
            }
        });
        socketService.onEventDeleteImageFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodDataListener != null) {
                    foodDataListener.onUpdateItem(food);
                }
            }
        });
        socketService.onEventUpdateActivedFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (foodDataListener != null) {
                    if (!food.isActived()) {
                        if (foodDataListener.onRemoveItem(food.getId())) {
                            removeFoodFromOrder(food);
                        }
                    }
                }
            }
        });
    }
}
