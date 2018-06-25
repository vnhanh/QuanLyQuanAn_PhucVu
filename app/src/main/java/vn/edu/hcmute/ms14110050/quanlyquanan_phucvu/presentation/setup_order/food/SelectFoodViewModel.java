package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.OnSpinnerStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFood;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestManger;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ICategoryFoodDataListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IFoodDataListener;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class SelectFoodViewModel
        extends BaseNetworkViewModel<IFoodView>
        implements OnSpinnerStateListener.DataProcessor  {

    private FoodRequestManger requestManager;
    // lắng nghe dữ liệu food và category food thay đổi
    private FoodSocketService socketService;

    private String selectedID;

    protected String token;
    protected String orderID;
    private ICategoryFoodDataListener catDataListener;
    private IFoodDataListener foodsDataListener;

    /*
    * Property
    * */

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    // spinner adapter
    public void setCatDataListener(ICategoryFoodDataListener catDataListener) {
        this.catDataListener = catDataListener;
    }

    // recyclerview adapter
    public void setFoodsDataListener(IFoodDataListener foodsDataListener) {
        this.foodsDataListener = foodsDataListener;
    }
/*
    * End Property
    * */

    @Override
    public void onViewAttach(@NonNull IFoodView viewCallback) {
        super.onViewAttach(viewCallback);
        Log.d("LOG", getClass().getSimpleName() + ":onViewAttach()");

        onSetupServerConnection();
        loadCategories();
    }

    @Override
    public void onViewDetached() {
        socketService.destroy();
        super.onViewDetached();
    }

    private void onSetupServerConnection() {
        createRequestManager();
        createSocketService();
        listenCatFoodValue();
        listenFoodValue();
    }

    public FoodRequestManger getRequestManager() {
        return requestManager;
    }

    private void createSocketService() {
        if (socketService == null) {
            socketService = new FoodSocketService();
        }
    }

    private void createRequestManager() {
        if (requestManager == null) {
            requestManager = new FoodRequestManger();
        }
    }

    private void listenCatFoodValue() {
        socketService.onEventAddCatFood(new GetCallback<CategoryFood>() {
            @Override
            public void onFinish(CategoryFood categoryFood) {
                if (catDataListener != null && categoryFood.isActived()) {
                    catDataListener.onAddItem(categoryFood);
                }
            }
        });
        socketService.onEventUpdateCatFood(new GetCallback<CategoryFood>() {
            @Override
            public void onFinish(CategoryFood categoryFood) {
                if (catDataListener != null) {
                    catDataListener.onUpdateItem(categoryFood);
                }
            }
        });
        socketService.onEventDeleteCatFood(new GetCallback<String>() {
            @Override
            public void onFinish(String catID) {
                if (catDataListener != null) {
                    catDataListener.onDeleteItem(catID);
                }
            }
        });
    }

    private void listenFoodValue() {
        socketService.onEventAddFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedID.equals(food.getCategoryID())) {
                        foodsDataListener.onAddItem(food);
                    }
                }
            }
        });
        socketService.onEventUpdateFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedID.equals(food.getCategoryID())) {
                        // update hoặc add
                        foodsDataListener.onUpdateItem(food);
                    }else{
                        //remove nếu lúc trước có tồn tại
                        foodsDataListener.onRemoveItem(food.getId());
                    }
                }
            }
        });
        socketService.onEventAddImageFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedID.equals(food.getCategoryID())) {
                        foodsDataListener.onAddImageItem(food);
                    }
                }
            }
        });
        socketService.onEventDeleteImageFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedID.equals(food.getCategoryID())) {
                        foodsDataListener.onDeleteImageItem(food);
                    }
                }
            }
        });
        socketService.onEventUpdateActivedFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedID.equals(food.getCategoryID())) {
                        if (food.isActived()) {
                            foodsDataListener.onAddItem(food);
                        }else{
                            foodsDataListener.onRemoveItem(food.getId());
                        }
                    }
                }
            }
        });
    }

    private void loadCategories() {
        if (isViewAttached()) {
            getView().onLoadingCategories();
        }
        if (StringUtils.isEmpty(token)) {
            token = SSharedReference.getToken(getView().getContext());
        }
        Log.d("LOG", getClass().getSimpleName() + ":loadRegions()");
        requestManager.loadCategories(token, new GetCallback<ArrayList<CategoryFood>>() {
            @Override
            public void onFinish(ArrayList<CategoryFood> categories) {
                int i = 0;
                while (categories != null && i < categories.size()) {
                    // nếu loại món không còn actived
                    if (!categories.get(i).isActived()) {
                        categories.remove(i);
                    }else{
                        i++;
                    }
                }
                onGetCategories(categories);
            }
        });
    }

    private void onGetCategories(ArrayList<CategoryFood> categoryFoods) {
        catDataListener.onGetCategories(categoryFoods);
        if (isViewAttached()) {
            getView().onEndLoadingCategories();
        }
    }

    // Khi spinner được select
    @Override
    public void onSelectSpinnerItemId(String id) {
        selectedID = id != null ? id : "";
        if (StringUtils.isEmpty(id)) {
            if (isViewAttached()) {
                catDataListener.onGetCategories(new ArrayList<CategoryFood>());
            }
            return;
        }
        if (isViewAttached()) {
            getView().onLoadingFoods();
        }
        if (StringUtils.isEmpty(token)) {
            token = SSharedReference.getToken(getView().getContext());
        }
        requestManager.loadFoodsByCatID(token, selectedID, new GetCallback<ArrayList<Food>>() {
            @Override
            public void onFinish(ArrayList<Food> foods) {
                if (foods == null) {
                    foods = new ArrayList<>();
                }
                if (foodsDataListener != null) {
                    foodsDataListener.onGetFoods(foods);
                }
                if (isViewAttached()) {
                    getView().onEndLoadingFoods();
                }
            }
        });
    }
}
