package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs;

import com.google.gson.reflect.TypeToken;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFood;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_ADD_CATEGORY_FOOD;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_ADD_FOOD;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_ADD_IMAGE_FOOD;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_DELETE_CATEGORY_FOOD;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_DELETE_IMAGE_FOOD;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_ACTIVE_FOOD;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_CATEGORY_FOOD;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.SOCKET_EVENT_UPDATE_FOOD;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class FoodSocketService extends BaseSocketService{

    public void onEventAddCatFood(final GetCallback<CategoryFood> callback) {
        listenEvent(SOCKET_EVENT_ADD_CATEGORY_FOOD, callback, "categoryFood", new TypeToken<CategoryFood>(){}.getType());
    }

    public void onEventUpdateCatFood(final GetCallback<CategoryFood> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_CATEGORY_FOOD, callback, "categoryFood", new TypeToken<CategoryFood>(){}.getType());
    }

    public void onEventDeleteCatFood(final GetCallback<String> callback) {
        listenEvent(SOCKET_EVENT_DELETE_CATEGORY_FOOD, callback, "id", null);
    }

    public void onEventAddFood(final GetCallback<Food> callback) {
        listenEvent(SOCKET_EVENT_ADD_FOOD, callback, "food",  new TypeToken<Food>(){}.getType());
    }

    public void onEventUpdateFood(final GetCallback<Food> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_FOOD, callback, "food",  new TypeToken<Food>(){}.getType());
    }

    public void onEventAddImageFood(final GetCallback<Food> callback) {
        listenEvent(SOCKET_EVENT_ADD_IMAGE_FOOD, callback, "food",  new TypeToken<Food>(){}.getType());
    }

    public void onEventDeleteImageFood(final GetCallback<Food> callback) {
        listenEvent(SOCKET_EVENT_DELETE_IMAGE_FOOD, callback, "food",  new TypeToken<Food>(){}.getType());
    }

    public void onEventUpdateActivedFood(final GetCallback<Food> callback) {
        listenEvent(SOCKET_EVENT_UPDATE_ACTIVE_FOOD, callback, "food",  new TypeToken<Food>(){}.getType());
    }

}
