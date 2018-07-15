package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.food.recyclerview.viewholder;

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
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.food.IFoodVM;


/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class ChefFoodVHViewModel extends BaseVHViewModel<IFoodVH> {
    private Food food;
    private DetailOrder detailOrder;
    private IFoodVM containerVM;

    public final ObservableField<Drawable> foodDrawable = new ObservableField<>();
    private BindableFieldTarget foodTarget;

    // số lượng đã order
    public ObservableInt count = new ObservableInt(0);
    // cờ đánh dấu đang đặt món
    public ObservableBoolean isOrdering = new ObservableBoolean(false);
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> inventory = new ObservableField<>();
    public ObservableField<String> unitPrice = new ObservableField<>();
    public ObservableField<String> discount = new ObservableField<>();
    public ObservableField<String> orderedCount = new ObservableField<>();
    public ObservableField<String> cash = new ObservableField<>();

    /*
    * Property
    * */

    public void setContainerViewModel(IFoodVM containerViewModel) {
        this.containerVM = containerViewModel;
        foodTarget = new BindableFieldTarget(foodDrawable, getContext().getResources());
    }

    public void setFood(Food food) {
        this.food = food;

        String url = StrUtil.getAbsoluteImgUrl(food.getImageUrls().get(0));

        // ảnh món
        Picasso.get().load(url)
                .placeholder(R.drawable.bg_light_gray_border_gray)
                .error(R.drawable.bg_light_gray_border_gray)
                .transform(new SquareCornerTransform(getContext(), R.dimen.size_image_food, R.dimen.normal_space))
                .into(foodTarget);

        name.set(food.getName());
        long _inventory, _unitPrice, _discount, _orderedCount, _cash;

        // set thông tin food
        _inventory = food.getInventory();
        _unitPrice = food.getUnitPrice();
        _discount = food.getDiscount();

        inventory.set(getContext().getString(R.string.content_food_inventory_count, _inventory));
        unitPrice.set(getContext().getString(R.string.content_food_unit_price, _unitPrice));
        discount.set(getContext().getString(R.string.content_food_discount, _discount));

        // set thông tin đã order (nếu có)
        getDetailOrder();

        if (detailOrder != null) {
            count.set(detailOrder.getCount());

            _orderedCount = detailOrder.getCount();
            _cash = (_unitPrice - _discount) * _orderedCount;

            orderedCount.set(getView().getContext().getString(R.string.content_food_ordered_count, _orderedCount));
            cash.set(getView().getContext().getString(R.string.content_food_cash, _cash));
        }else{
            count.set(0);
        }
    }

    private void getDetailOrder() {
        if (detailOrder == null) {
            detailOrder = containerVM.getDetailOrderByFood(food.getId());
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
            getDetailOrder();
            getView().onStartViewFoodActivity(containerVM.getContext(), containerVM.getOrderID(), detailOrder, food);
        }
    }

    /*
    * End DataBinding
    * */
}
