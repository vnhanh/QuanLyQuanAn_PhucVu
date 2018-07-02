package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.view_food;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.WeakHashMap;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.InputProcessorCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel.BaseNetworkViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.RectangleImageTransform;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.ScaleType;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.sharedpreferences.SSharedReference;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.api.nodejs.FoodSocketService;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodOrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.FoodResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.Order;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderResponse;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.OrderRequestManager;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.food.FoodRequestManger;

/**
 * Created by Vo Ngoc Hanh on 6/27/2018.
 */

public class ViewFoodModel extends BaseNetworkViewModel<IViewFood> implements InputProcessorCallback{
    public final ObservableField<Drawable> foodDrawable = new ObservableField<Drawable>();
    private BindableFieldTarget foodTarget;

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> inventory = new ObservableField<>();
    public ObservableField<String> unitPrice = new ObservableField<>();
    public ObservableField<String> discount = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<String> orderedCount = new ObservableField<>();

    public ObservableBoolean isProgress = new ObservableBoolean(false);
    public ObservableBoolean hasOrdered = new ObservableBoolean(false);

    private FoodRequestManger foodRM;
    private FoodSocketService foodSS;

    private OrderRequestManager orderRM;

    /*
    * Property
    * */

    private DetailOrder detailOrder;
    private Food food;
    private String token;
    private String orderID;
    // độ rộng ảnh món
    private int imageFoodWidth = -1;

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public DetailOrder getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(DetailOrder detailOrder) {
        this.detailOrder = detailOrder;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }

    public void setImageFoodWidth(int imageFoodWidth) {
        this.imageFoodWidth = imageFoodWidth;
        showFoodImage();
    }

    /*
    * End Property
    * */

    public ViewFoodModel() {
    }

    @Override
    public void onViewAttach(@NonNull IViewFood viewCallback) {
        super.onViewAttach(viewCallback);

        //init
        createOrderRequestManager();
        createFoodRequestManager();
        createFoodSocketService();

        foodTarget = new BindableFieldTarget(foodDrawable, getContext().getResources());

        getFoodCallback = new GetFoodCallback(this);
        inputOrderCountCallback = new InputCallbackImpl(this);
        orderFoodCallback = new GetFoodOrderResponseCallback(this);
        updateOrderCallback = new GetOrderCallback(this);

        listenFoodSocket();

        loadFood();

        showFood();
        showOrderIfExist();
    }

    private void createOrderRequestManager() {
        if (orderRM == null) {
            orderRM = new OrderRequestManager();
        }
    }

    private void createFoodRequestManager() {
        if (foodRM == null) {
            foodRM = new FoodRequestManger();
        }
    }

    private void createFoodSocketService() {
        if (foodSS == null) {
            foodSS = new FoodSocketService();
        }
    }

    @Override
    public void onViewDetached() {
        inputOrderCountCallback = null;
        getFoodCallback = null;
        orderFoodCallback = null;
        updateOrderCallback = null;
        super.onViewDetached();
    }

    private void showOrderIfExist() {
        if (detailOrder != null && detailOrder.getCount() > 0) {
            hasOrdered.set(true);
            orderedCount.set(String.valueOf(detailOrder.getCount()));
        }else{
            hasOrdered.set(false);
        }
    }

    private void showFood() {
        showFoodImage();

        name.set(food.getName());
        inventory.set(String.valueOf(food.getInventory()) + " " + food.getUnit());
        unitPrice.set(String.valueOf(food.getUnitPrice()) + " " + getString(R.string.currency_unit));
        discount.set(String.valueOf(food.getDiscount()) + " " + getString(R.string.currency_unit));
        description.set(food.getDescription());
    }

    private void showFoodImage() {
        if (food != null && imageFoodWidth > 0) {
            int height = getResources().getDimensionPixelOffset(R.dimen.height_image_view_food);
            RectangleImageTransform transform =
                    new RectangleImageTransform(imageFoodWidth, height, 0, ScaleType.CENTER_CROP);

            Picasso.get().load(food.getImageUrls().get(0))
                    .placeholder(R.drawable.bg_light_gray_border_gray)
                    .error(R.drawable.bg_light_gray_border_gray)
                    .transform(transform)
                    .into(foodTarget);
        }
    }

    private void listenFoodSocket() {
        foodSS.onEventUpdateFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food _food) {
                if (_food.getId() != null && _food.getId().equals(food.getId())) {
                    showLoadingFood();
                    food = _food;
                    showFood();
                    hideloadingFood();
                }
            }
        });
        foodSS.onEventUpdateActivedFood(new GetCallback<Food>() {
            @Override
            public void onFinish(Food _food) {
                if (_food != null && _food.getId() != null && !_food.isActived()) {
                    if (_food.getId().equals(food.getId())) {
                        Toast.makeText(getContext(), getString(R.string.this_food_is_not_actived), Toast.LENGTH_SHORT).show();
                        getView().onBackPrevActivity();
                    }
                }
            }
        });
    }

    private String orderFoodError = "";

    public void onUpdateOrderResponse(OrderResponse response) {
        hideProgress();
        if (response.getSuccess()) {
            Order order = response.getOrder();
            for (DetailOrder detail : order.getDetailOrders()) {
                if (detail.getFoodId().equals(food.getId())) {
                    detailOrder = detail;
                    showOrderIfExist();
                    return;
                }
            }
        }
        else{
            Log.d("LOG", ViewFoodModel.class.getSimpleName()
                    + ":orderFood():failed:" + response.getMessage());

            // trường hợp update food thành công nhưng update order thất bại
            // sẽ hiển thị message
            if (orderFoodError.equals("")) {
                String message =
                        currentOrderCount == 0 ? getString(R.string.remove_order_food_failed) : getString(R.string.order_food_failed);

                if (!StringUtils.isEmpty(response.getMessage())) {
                    message += " " + response.getMessage();
                }

                if (isViewAttached()) {
                    Snackbar.make(getView().getViewUI(), message, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadFood() {
        showLoadingFood();
        getToken();
        foodRM.loadFoodByID(token, food.getId(), getFoodCallback);
    }

    public void onGetFoodResponse(FoodResponse response) {
        hideloadingFood();
        if (!response.getSuccess()) {
            Log.d("LOG", ViewFoodModel.class.getSimpleName()
                    + ":orderFood():failed:" + response.getMessage());

            if (isViewAttached()) {
                Toast.makeText(getView().getContext(), R.string.not_get_food_info, Toast.LENGTH_SHORT).show();
                getView().onBackPrevActivity();
            }
        }else{
            food = response.getFood();
            showFood();
        }
    }

    private void showLoadingFood() {
        isProgress.set(true);
    }

    private void hideloadingFood() {
        isProgress.set(false);
    }

    private void showProgress() {
        if (isViewAttached()) {
            getView().showProgress(currentOrderCount != 0 ? R.string.ordering_food : R.string.removing_order_food);
        }
    }

    private boolean isWaiteFood() {
        return isProgress.get();
    }

    /*
    * DataBinding
    * */

    private boolean hasOrder() {
        return detailOrder != null && detailOrder.getFoodId() != null && detailOrder.getFoodId().equals(food.getId());
    }

    private int currentOrderCount;

    public void onClickOrderButton(View view) {
        // đang trong trạng thái rảnh, không thực hiện tác vụ xử lý hay load
        if (!isWaiteFood()) {
            if (isViewAttached()) {
                int oldCount = hasOrder() ? detailOrder.getCount() : 0;
                currentOrderCount = oldCount;
                getView().openInputOrderCountDialog(oldCount, inputOrderCountCallback);
            }
        }else{
            Snackbar.make(view, getString(R.string.updating_food), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChangeInputValue(TextInputLayout til, EditText edt, String input) {
        String error = "";
        String newText = "";
        int count = 0;
        boolean isChanged = false;

        try {
            count = input.equals("") ? 0 : Integer.parseInt(input);

            if (count < 0) {
                error = getString(R.string.ordered_count_not_negative);
                newText = "0";
                isChanged = true;
            }else if(count > 0){
                int _oldCount = detailOrder != null ? detailOrder.getCount() : 0;

                if (count > _oldCount + food.getInventory()) {
                    error = getString(R.string.ordered_count_not_greater_inventory);
                    newText = String.valueOf(_oldCount + food.getInventory());
                    isChanged = true;
                } else if (input.charAt(0) == '0') {
                    newText = String.valueOf(count);
                    isChanged = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = getString(R.string.error_wrong_format_input);
            newText = "0";
            isChanged = true;
        }

        if (error.equals("")) {
            currentOrderCount = count;
        }else{
            if (til != null) {
                til.setError(error);
            }else{
                edt.setError(error);
            }
        }
        if (isChanged) {
            edt.setText(newText);
            edt.setSelection(edt.getText().toString().length());
        }
    }

    @Override
    public void onSubmitInputProcessor() {
        if (currentOrderCount == 0 && detailOrder == null) {
            if (isViewAttached()) {
                Toast.makeText(getContext(), getString(R.string.cannot_create_detail_order_for_zero_count), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (detailOrder != null && currentOrderCount == detailOrder.getCount()) {
            if (isViewAttached()) {
                Toast.makeText(getContext(), getString(R.string.count_ordered_not_change), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (orderRM == null) {
            createOrderRequestManager();
        }
        showProgress();

        getToken();
        int oldCount = hasOrder() ? detailOrder.getCount() : 0;

        WeakHashMap<String, Object> fields = new WeakHashMap<>();
        fields.put("orderID", getOrderID());
        fields.put("foodID", food.getId());
        fields.put("oldCount", oldCount);
        fields.put("newCount", currentOrderCount);
        fields.put("inventory", food.getInventory());

        // reset giá trị lỗi hiển thị khi order thất bại
        orderFoodError = "";
        orderRM.orderFood(token, fields, new GetCallback<FoodResponse>() {
            @Override
            public void onFinish(FoodResponse response) {
                if (!response.getSuccess()) {
                    Log.d("LOG", ViewFoodModel.class.getSimpleName()
                            + ":orderFoodForOrder():failed:" + response.getMessage());
                }else{
                    food = response.getFood();
                    showFood();
                }
            }
        }, new GetCallback<OrderResponse>() {
            @Override
            public void onFinish(OrderResponse orderResponse) {
                if (!orderResponse.getSuccess()) {
                    hideProgress();
                    String message = getString(currentOrderCount == 0 ? R.string.remove_order_food_failed : R.string.order_food_failed);
                    if(!StringUtils.isEmpty(orderResponse.getMessage())){
                        message += ". " + orderResponse.getMessage();
                    }
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }else{
                    Order order = orderResponse.getOrder();
                    ArrayList<DetailOrder> details = order.getDetailOrders();
                    int size = details.size();
                    int i = 0;
                    for (; i < size; i++) {
                        if (details.get(i).getFoodId().equals(food.getId())) {
                            detailOrder = details.get(i);
                            break;
                        }
                    }
                    if (i >= size) {
                        detailOrder = null;
                    }
                    showOrderIfExist();
                    hideProgress();
                }
            }
        });
    }

    public void onOrderFoodResponse(FoodOrderResponse response) {
        if (!response.getSuccess()) {
            Log.d("LOG", ViewFoodModel.class.getSimpleName()
                    + ":orderFood():onOrderFoodResposne():failed:" + response.getMessage());

            if (isViewAttached()) {
                orderFoodError =
                        currentOrderCount == 0 ? getString(R.string.remove_order_food_failed) : getString(R.string.order_food_failed);

                if (!StringUtils.isEmpty(response.getMessage())) {
                    orderFoodError += response.getMessage();
                }

                Snackbar.make(getView().getViewUI(), orderFoodError, Toast.LENGTH_SHORT).show();
            }
        }else{
            food = response.getFood();
            showFood();
        }
    }

    public void getToken() {
        if (token == null) {
            token = SSharedReference.getToken(getContext());
        }
    }

    /*
    * End DataBinding
    * */

    private View getViewUI() {
        return isViewAttached() ? getView().getViewUI() : null;
    }

    private InputCallbackImpl inputOrderCountCallback;

    private GetFoodOrderResponseCallback orderFoodCallback;

    private GetFoodCallback getFoodCallback;

    private GetOrderCallback updateOrderCallback;
}
