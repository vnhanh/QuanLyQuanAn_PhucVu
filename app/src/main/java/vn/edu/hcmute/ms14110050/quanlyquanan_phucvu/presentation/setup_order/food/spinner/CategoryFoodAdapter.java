package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.food.spinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.OnSpinnerStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.asynctask.SortTask;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFood;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ISpinnerDataListener;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class CategoryFoodAdapter extends ArrayAdapter implements ISpinnerDataListener<CategoryFood>{
    private ArrayList<CategoryFood> categories;
    private String selectedCatID;

    @NonNull
    private OnSpinnerStateListener.View viewListener;
    @NonNull
    private OnSpinnerStateListener.DataProcessor dataProcessorListener;

    /*
    * Property
    * */

    public String getSelectedCategoryID() {
        return selectedCatID;
    }

    /*
    * End Property
    * */

    public CategoryFoodAdapter(@NonNull Context context, int resource,
                               @NonNull OnSpinnerStateListener.View viewListener,
                               @NonNull OnSpinnerStateListener.DataProcessor dataProcessorListener) {
        super(context, resource);

        this.viewListener = viewListener;
        this.dataProcessorListener = dataProcessorListener;
    }

    /*
    * Thao tác với View UI
    * */
    private boolean isEmptyList() {
        return categories == null || categories.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmptyList() ? 1 : categories.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.item_spinner, parent, false);

        showContent(view, position);

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_spinner_dropdown, parent, false);
        showContent(view, position);

        return view;
    }

    private void showContent(View view, int position) {
        TextView textView = view.findViewById(R.id.textView);
        String content = "";
        if (isEmptyList()) {
            content = getContext().getString(R.string.no_category_food_item);
        }else{
            content = categories.get(position).getName();
        }
        textView.setText(content);
    }

    /*
    * Thao tác với dữ liệu
    * */

    @Override
    public void onGetList(ArrayList<CategoryFood> cats) {
        this.categories = cats;
        onDataChanged();
    }

    @Override
    public void onAddItem(CategoryFood cat) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        categories.add(cat);
        onDataChanged();
    }

    /**
     * Server update tên (name) loại món
     */
    @Override
    public void onUpdateItem(CategoryFood cat) {
        String id = cat.getId();
        int index = findItem(id);
        if (index > -1) {
            categories.set(index, cat);
        }
        onDataChanged();
    }

    @Override
    public void onDeleteItem(@NonNull String catID) {
        int index = findItem(catID);
        if (index > -1) {
            categories.remove(index);

            // xóa đúng item đang được chọn
            if (catID.equals(selectedCatID)) {
                // quay về loại món đầu tiên trong danh sách
                selectedCatID = categories.size() > 0 ? categories.get(0).getId() : "";
                if (categories.size() > 0) {
                    viewListener.onSelectSpinnerItemIndex(0);
                }
                dataProcessorListener.onSelectSpinnerItemId(selectedCatID);
            }

            onDataChanged();
        }
    }

    private void onDataChanged() {
        viewListener.onStartProcessDataChanged();
        sortList();
    }

    private void sortList() {
        new SortTask<CategoryFood>(
                categories,
                new Comparator<CategoryFood>() {
                    @Override
                    public int compare(CategoryFood region, CategoryFood _region) {
                        String regionNameDeAccent = StringUtils.deAccent(region.getName());
                        String _regionNameDeAccent = StringUtils.deAccent(_region.getName());
                        return regionNameDeAccent.compareToIgnoreCase(_regionNameDeAccent);
                    }
                }, new GetCallback<ArrayList<CategoryFood>>() {
                    @Override
                    public void onFinish(ArrayList<CategoryFood> _cats) {
                        onSortListFinish(_cats);
                    }
                })
                .execute();
    }

    private void onSortListFinish(ArrayList<CategoryFood> _cats) {
        this.categories = _cats;
        notifyDataSetChanged();
        onResetSelectItem();
    }

    private int findItem(String id){
        int size = categories.size();
        for (int i = 0; i < size; i++) {
            CategoryFood _region = categories.get(i);
            if (_region.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Khi spinner được bấm (bởi người dùng hoặc code)
     * @param position
     */
    public void onSelectItemIndex(int position) {
        if (categories == null || position < 0 || position >= categories.size()) {
            return;
        }

        String selectedID = (position < 0 || isEmptyList()) ? "" : categories.get(position).getId();

        if (!selectedID.equals(selectedCatID)) {
            selectedCatID = selectedID;
            dataProcessorListener.onSelectSpinnerItemId(selectedCatID);
        }
    }

    // Tái xác định item được chọn sau khi sortList()
    private void onResetSelectItem() {
        int index = 0;
        // cờ đánh dấu có thay đổi item hay không ? (để thực hiện việc load lại dữ liệu)
        boolean isChangeItem = false;
        if (StringUtils.isEmpty(selectedCatID)) {
            if (categories != null && categories.size() > 0) {
                isChangeItem = true;
                selectedCatID = categories.get(0).getId();
            }
        } else {
            Log.d("LOG", getClass().getSimpleName() + ":onResetSelectItem():current item was existed");

            // tìm vị trí của item đang select
            int _index = 0;
            int size = categories.size();
            for (; _index < size; _index++) {
                if (categories.get(_index).getId().equals(selectedCatID)) {
                    index = _index;
                    break;
                }
            }

            if (_index == size) {
                Log.d("LOG", getClass().getSimpleName() + ":onResetSelectItem():current item just change");
                isChangeItem = true;
                selectedCatID = "";
            }
        }
        // kết thúc việc xử lý khi dữ liệu thay đổi
        viewListener.onEndProcessDataChanged();
        // set selected index cho spinner
        viewListener.onSelectSpinnerItemIndex(index);
        // truyền region id cho viewmodel load list món theo loại món
        if (isChangeItem) {
            dataProcessorListener.onSelectSpinnerItemId(selectedCatID);
        }
    }
}
