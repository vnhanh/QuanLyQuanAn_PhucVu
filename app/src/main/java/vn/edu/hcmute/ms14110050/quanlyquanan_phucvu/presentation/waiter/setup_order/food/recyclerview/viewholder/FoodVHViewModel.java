package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.food.recyclerview.viewholder;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseVHViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.SquareCornerTransform;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.Food;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.food.IFoodVM;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class FoodVHViewModel extends BaseVHViewModel<IFoodVH> {
    private Food food;
    private DetailOrder detailOrder;
    private IFoodVM containerVM;

    public final ObservableField<Drawable> foodDrawable = new ObservableField<>();
    private BindableFieldTarget foodTarget;

    // số lượng đã order
    public ObservableInt count = new ObservableInt(0);
    // cờ đánh dấu đang đặt món
    public ObservableBoolean isOrdering = new ObservableBoolean(false);
    public ObservableField<String> foodInfo = new ObservableField<>();

    private int serial;

    /*
    * Property
    * */

    public void setContainerViewModel(IFoodVM containerViewModel) {
        this.containerVM = containerViewModel;
    }

    @Override
    public void attachView(IFoodVH view) {
        super.attachView(view);

        foodTarget = new BindableFieldTarget(foodDrawable, getContext().getResources());
    }

    public void setData(Food food, int position) {
        this.food = food;
        serial = position + 1;

        showImage();

        showFoodInfo();

        showOrdered();
    }

    private void showOrdered() {
        // set thông tin đã order (nếu có)
        detailOrder = containerVM.getDetailOrderByFood(food.getId());

        if (detailOrder != null) {
            count.set(detailOrder.getCount());
            isOrdering.set(false);
        }else{
            count.set(0);
            isOrdering.set(true);
        }
    }

    private void showFoodInfo() {
        StringBuilder builder = new StringBuilder("#" + serial);

        builder.append("\n" + food.getName());

        long _inventory, _unitPrice, _discount;

        // set thông tin food
        _inventory = food.getInventory();
        _unitPrice = food.getUnitPrice();
        _discount = food.getDiscount();

        builder.append("\n" + getContext().getString(R.string.content_food_inventory_count, _inventory));
        builder.append("\n" + getContext().getString(R.string.content_food_unit_price, _unitPrice));


        if (_discount > 0) {
            builder.append("\n" + getContext().getString(R.string.content_food_discount, _discount));
        }
        foodInfo.set(builder.toString());
    }

    private void showImage() {
        String url = StrUtil.getAbsoluteImgUrl(food.getImageUrls().get(0));

        // ảnh món
        Picasso.get().load(url)
                .placeholder(R.drawable.bg_light_gray_border_gray)
                .error(R.drawable.bg_light_gray_border_gray)
                .transform(new SquareCornerTransform(getContext(), R.dimen.size_image_food, R.dimen.normal_space))
                .into(foodTarget);
    }

    private void getDetailOrder() {
        if (detailOrder == null) {
            detailOrder = containerVM.getDetailOrderByFood(food.getId());
            if (detailOrder != null) {
                Log.d("LOG", getClass().getSimpleName() + ":getDetailOrder():id" + detailOrder.getFoodId()+ ":name:"
                        + detailOrder.getFoodName() + ":count:" + detailOrder.getCount());
            }
        }

    }

    /*
    * End Property
    * */

    /*
    * DataBinding
    * */

    // Khi bấm vào item
    public void onClickItem() {
        if (containerVM == null) {
            Log.d("LOG", getClass().getSimpleName() + ":onClickItem():not found containerVM");
            return;
        }
        if (isViewAttached()) {
            boolean isCreateOrder = containerVM.isCreatedOrder();
            if (isCreateOrder || detailOrder.getStatusFlag() == OrderFlag.PENDING) {
                getView().onStartViewFoodActivity(containerVM.getContext(), containerVM.getOrderID(), detailOrder, food);
            }else{
                getView().onStartViewFoodActivity(containerVM.getContext(), containerVM.getOrderID(), null, food);
            }
        }
    }
    /*
    * End DataBinding
    * */
}
