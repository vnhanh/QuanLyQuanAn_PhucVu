package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food;

import android.content.Context;
import android.databinding.ObservableBoolean;
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
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodOrderSocketData;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestManger;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IRecyclerAdapter;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IOrderVM;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ISpinnerDataListener;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class SelectFoodViewModel
        extends BaseNetworkViewModel<IFoodView>
        implements OnSpinnerStateListener.DataProcessor, IFoodVM  {

    private IOrderVM centerVM;

    private FoodRequestManger requestManager;
    // lắng nghe dữ liệu food và category food thay đổi
    private FoodSocketService socketService;

    private String selectedCatID;

    protected String token;
    private Order order;

    private ISpinnerDataListener<CategoryFood> catDataListener;
    private IRecyclerAdapter<Food> foodsDataListener;

    public final ObservableBoolean isLoadingFoods = new ObservableBoolean();

    /*
    * Property
    * */

    public void setCenterViewModel(IOrderVM centerVM) {
        this.centerVM = centerVM;
        this.order = centerVM.getOrder();
        this.token = centerVM.getToken();
        this.requestManager = centerVM.getFoodRequestManager();
        this.socketService = centerVM.getFoodSocketService();
    }

    // spinner adapter
    public void setCatDataListener(ISpinnerDataListener<CategoryFood> catDataListener) {
        this.catDataListener = catDataListener;
    }

    // recyclerview adapter
    public void setFoodsDataListener(IRecyclerAdapter<Food> foodsDataListener) {
        this.foodsDataListener = foodsDataListener;
    }

    /*
    * End Property
    * */

    @Override
    public void onViewAttach(@NonNull IFoodView viewCallback) {
        super.onViewAttach(viewCallback);

        onSetupServerConnection();
        loadCategories();
    }

    @Override
    public void onViewDetached() {
        requestManager = null;
        socketService = null;
        order = null;
        token = null;
        super.onViewDetached();
    }

    private void onSetupServerConnection() {
        listenCatFoodValue();
        listenFoodValue();
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
                    if (selectedCatID.equals(food.getCategoryID())) {
                        foodsDataListener.onAddItem(food);
                    }
                }
            }
        });
        socketService.onEventUpdateFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedCatID.equals(food.getCategoryID())) {
                        foodsDataListener.onUpdateItem(food);
                    }
                }
            }
        });
        socketService.onEventOrderFood(new GetCallback<FoodOrderSocketData>() {
            @Override
            public void onFinish(FoodOrderSocketData data) {
                if (data.getOrderID() != null && !data.getOrderID().equals(getOrderID())) {
                    onOrderFood(data.getFood());
                }
            }
        });
        socketService.onEventAddImageFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedCatID.equals(food.getCategoryID())) {
                        foodsDataListener.onUpdateItem(food);
                    }
                }
            }
        });
        socketService.onEventDeleteImageFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedCatID.equals(food.getCategoryID())) {
                        foodsDataListener.onUpdateItem(food);
                    }
                }
            }
        });
        socketService.onEventUpdateActivedFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food food) {
                if (food.isActived() && foodsDataListener != null) {
                    if (selectedCatID.equals(food.getCategoryID())) {
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

    private void onOrderFood(Food food) {
        if (food != null && food.isActived() && foodsDataListener != null) {
            if (selectedCatID.equals(food.getCategoryID())) {
                foodsDataListener.onUpdateItem(food);
            }
        }
    }

    private void loadCategories() {
        if (isViewAttached()) {
            getView().onLoadingCategories();
        }
        if (StringUtils.isEmpty(token)) {
            token = SSharedReference.getToken(getView().getContext());
        }
        requestManager.loadCategories(token, new GetCallback<ArrayList<CategoryFood>>() {
            @Override
            public void onFinish(ArrayList<CategoryFood> categories) {
                Log.d("LOG", getClass().getSimpleName()
                        + ":loadCategoryfoods():get category food:size:"+categories.size());
                onGetCategories(categories);
            }
        });
    }

    private void onGetCategories(ArrayList<CategoryFood> categoryFoods) {
        catDataListener.onGetList(categoryFoods);
        if (isViewAttached()) {
            getView().onEndLoadingCategories();
        }
    }

    // Khi spinner được select
    @Override
    public void onSelectSpinnerItemId(String id) {
        selectedCatID = id != null ? id : "";
        if (StringUtils.isEmpty(id)) {
            if (isViewAttached()) {
                catDataListener.onGetList(new ArrayList<CategoryFood>());
            }
            return;
        }
        showLoadingFoods();
        if (StringUtils.isEmpty(token)) {
            token = SSharedReference.getToken(getView().getContext());
        }
        requestManager.loadFoodsByCatID(token, selectedCatID, new GetCallback<ArrayList<Food>>() {
            @Override
            public void onFinish(ArrayList<Food> foods) {
                if (foods == null) {
                    foods = new ArrayList<>();
                }
                if (foodsDataListener != null) {
                    foodsDataListener.onGetList(foods);
                }
                hideLoadingFoods();
            }
        });
    }

    private void showLoadingFoods() {
        isLoadingFoods.set(true);
    }

    private void hideLoadingFoods() {
        isLoadingFoods.set(false);
    }

    /*
    * IFoodVM
    * */

    @Override
    public boolean isCreatedOrder() {
        return centerVM.isCreatedOrder();
    }

    @Override
    public int getDetailOrderIndexByFood(String foodID) {
        return centerVM.getDetailOrderIndexByFood(foodID);
    }

    @Override
    public DetailOrder getDetailOrderByFood(String foodID) {
        return centerVM.getDetailOrderByFood(foodID);
    }

    @Override
    public FoodRequestManger getFoodRequestManager() {
        return requestManager;
    }

    @Override
    public FoodSocketService getFoodSocketService() {
        return socketService;
    }

    @Override
    public String getOrderID() {
        return centerVM != null ? centerVM.getOrderID() : "";
    }

    @Override
    public String getToken() {
        if (StringUtils.isEmpty(token)) {
            token = centerVM.getToken();
        }
        return token;
    }

    @Override
    public Context getContext() {
        return centerVM != null ? centerVM.getContext() : null;
    }

    /*
    * End IFoodVM
    * */

}
