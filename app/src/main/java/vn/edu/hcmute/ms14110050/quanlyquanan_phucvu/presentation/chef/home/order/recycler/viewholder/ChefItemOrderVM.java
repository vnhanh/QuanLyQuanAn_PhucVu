package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.viewholder;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseVHViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.databinding.BindableFieldTarget;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.SquareCornerTransform;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StrUtil;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.DetailOrder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order.OrderFlag;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.request_manager.retrofit.order.OrderRequestApi;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.home.order.recycler.IDetailOrderAdapter;


public class ChefItemOrderVM extends BaseVHViewModel<IItemOrderView> {
    public final ObservableBoolean isError = new ObservableBoolean();
    public final ObservableField<Drawable> foodDrawable = new ObservableField<>();
    private BindableFieldTarget foodTarget;

    public ObservableField<String> indicator = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> categoryName = new ObservableField<>();
    public ObservableField<String> orderedCount = new ObservableField<>();
    public ObservableField<String> detailOrderStatus = new ObservableField<>();
    public ObservableField<String> processOrder = new ObservableField<>();
    public final ObservableBoolean isShowProcessButton = new ObservableBoolean(true);

    private WeakReference<IDetailOrderProcessor> weakProcessor;

    private int serial;

    public void setDetailOrderProcessor(IDetailOrderProcessor processor) {
        this.weakProcessor = new WeakReference<>(processor);
    }

    public ChefItemOrderVM() {
    }

    @Override
    public void attachView(IItemOrderView view) {
        super.attachView(view);
        foodTarget = new BindableFieldTarget(foodDrawable, getContext().getResources());
    }

    private DetailOrder detailOrder;

    public DetailOrder getDetailOrder() {
        return detailOrder;
    }

    public void setData(DetailOrder detailOrder, int position) {
        this.detailOrder = detailOrder;
        this.serial = position + 1;
        showInfo();
    }

    private void showInfo() {
        if (detailOrder == null) {
            isError.set(true);
            isShowProcessButton.set(false);
            name.set(getString(R.string.undefined));
            return;
        }

        isError.set(false);

        String url = StrUtil.getAbsoluteImgUrl(detailOrder.getFoodImage());

        // ảnh món
        Picasso.get().load(url)
                .placeholder(R.drawable.bg_light_gray_border_gray)
                .error(R.drawable.bg_light_gray_border_gray)
                .transform(new SquareCornerTransform(getContext(), R.dimen.size_image_food, R.dimen.normal_space))
                .into(foodTarget);

        indicator.set(getString(R.string.content_serial, serial));

        name.set(detailOrder.getFoodName());
        String _categoryName = getString(R.string.category_content, detailOrder.getCategoryName());
        categoryName.set(_categoryName);

        long _orderedCount = detailOrder != null ? detailOrder.getCount() : 0;
        orderedCount.set(getView().getContext().getString(R.string.content_food_ordered_count, _orderedCount));

        int status = detailOrder.getStatusFlag();
        String statusContent = null, processLabel = null;
        switch (status) {
            case OrderFlag.PENDING:
                statusContent = getString(R.string.pending);
                processLabel = getString(R.string.title_confirm_start_cooking);
                isShowProcessButton.set(true);
                break;

            case OrderFlag.COOKING:
                statusContent = getString(R.string.cooking);
                processLabel = getString(R.string.title_confirm_cook_finish);
                isShowProcessButton.set(true);
                break;

            case OrderFlag.PREPARE:
                statusContent = getString(R.string.prepare);
                isShowProcessButton.set(false);
                break;

            case OrderFlag.EATING:
                statusContent = getString(R.string.eating);
                isShowProcessButton.set(false);
                break;

            case OrderFlag.PAYING:
                statusContent = getString(R.string.paying);
                isShowProcessButton.set(false);
                break;

            case OrderFlag.COMPLETE:
                statusContent = getString(R.string.complete);
                isShowProcessButton.set(false);
                break;

            default:
                isShowProcessButton.set(false);
                break;
        }
        detailOrderStatus.set(getString(R.string.show_order_status, statusContent));

        if (isShowProcessButton.get()) {
            processOrder.set(processLabel);
        }
    }

//    public void onClickItem() {
//        if (isViewAttached() && detailOrder != null) {
//            getView().onClick(detailOrder.getOrderID());
//        }
//    }

    public void onClickProcessButton() {
        IDetailOrderProcessor processor = weakProcessor.get();
        if (processor != null) {
            processor.requestUpdateStatusDetailOrder(detailOrder, detailOrder.getStatusFlag()+1);
        }
    }
}
