package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.detail_order;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseVHViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.SquareCornerTransform;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.abstracts.IOrderVM;

public class DetailOrderVM extends BaseVHViewModel<IDetailOrderVH> {
    private IOrderVM centerVM;

    public final ObservableField<Drawable> foodDrawable = new ObservableField<>();
    private BindableFieldTarget foodTarget;

    // số lượng đã order
    public ObservableInt count = new ObservableInt(0);
    public ObservableField<String> info = new ObservableField<>();

    public ObservableBoolean isShowStatus = new ObservableBoolean(false);
    public ObservableField<String> detailOrderStatus = new ObservableField<>();

    public ObservableField<String> processAction = new ObservableField<>();
    public ObservableBoolean isShowProcessButton = new ObservableBoolean(false);

    public ObservableBoolean isConfirmAction = new ObservableBoolean(false);

    private DetailOrder detailOrder;

    private int serial;

    public void setCenterVM(IOrderVM centerVM) {
        this.centerVM = centerVM;
    }

    @Override
    public void attachView(IDetailOrderVH view) {
        super.attachView(view);
        foodTarget = new BindableFieldTarget(foodDrawable, getContext().getResources());
    }

    public void setData(DetailOrder detailOrder, int position) {
        this.detailOrder = detailOrder;
        this.serial = position + 1;

        if (detailOrder != null) {
            showInfo();
        }
    }

    private void showInfo() {
        showImage();

        showDetailInfo();
    }

    private void showDetailInfo() {
        StringBuilder builder = new StringBuilder("#" + serial);

        builder.append("\n" + detailOrder.getFoodName());
        long _unitPrice, _discount;

        // set thông tin food
        _unitPrice = detailOrder.getUnitPrice();
        _discount = detailOrder.getDiscount();

        builder.append("\n" + getContext().getString(R.string.content_food_unit_price, _unitPrice));

        if (_discount > 0) {
            builder.append("\n" + getContext().getString(R.string.content_food_discount, _discount));
        }

        count.set(detailOrder.getCount());

        long _orderedCount, _cash;

        _orderedCount = detailOrder.getCount();
        _cash = _orderedCount * (detailOrder.getUnitPrice() - detailOrder.getDiscount());

        builder.append("\n" + getView().getContext().getString(R.string.content_food_cash, _cash));

        info.set(builder.toString());

        int status = detailOrder.getStatusFlag();

        if (status == OrderFlag.CREATING) {
            isShowStatus.set(false);

            isShowProcessButton.set(true);
            processAction.set(getString(R.string.title_remove_detail_order));
            isConfirmAction.set(false);
        }
        else{
            isShowStatus.set(true);

            if (status == OrderFlag.PREPARE) {
                detailOrderStatus.set(getString(R.string.prepare));

                isShowProcessButton.set(true);
                isConfirmAction.set(true);
                processAction.set(getString(R.string.title_prepare));
            }
            else if(status == OrderFlag.PENDING){
                detailOrderStatus.set(getString(R.string.pending));

                isShowProcessButton.set(true);
                isConfirmAction.set(false);
                processAction.set(getString(R.string.title_remove_detail_order));
            }
            else{
                isShowProcessButton.set(false);
                switch (status) {
                    case OrderFlag.COOKING:
                        detailOrderStatus.set(getString(R.string.cooking));
                        break;
                    case OrderFlag.EATING:
                        detailOrderStatus.set(getString(R.string.eating));
                        break;

                    default:
                        return;
                }
            }
        }
    }

    private void showImage() {
        String url = StrUtil.getAbsoluteImgUrl(detailOrder.getFoodImage());

        // ảnh món
        Picasso.get().load(url)
                .placeholder(R.drawable.bg_light_gray_border_gray)
                .error(R.drawable.bg_light_gray_border_gray)
                .transform(new SquareCornerTransform(getContext(), R.dimen.size_image_food, R.dimen.normal_space))
                .into(foodTarget);
    }

    public void onClickItem() {

    }

    public void onClickPrepareButton() {
        if (centerVM != null) {
            int status = detailOrder.getStatusFlag();
            if (status == OrderFlag.CREATING || status == OrderFlag.PENDING) {
                centerVM.onRequestRemoveDetailOrder(detailOrder.getId());
            } else if (status == OrderFlag.PREPARE) {
                centerVM.onRequestUpdateDetailOrderStatus(detailOrder.getId(), status + 1);
            }
        }
    }
}
