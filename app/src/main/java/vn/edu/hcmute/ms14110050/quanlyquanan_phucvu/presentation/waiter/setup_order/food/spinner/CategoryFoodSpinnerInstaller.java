package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.food.spinner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.OnSpinnerStateListener;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.FragmentSelectFoodsBinding;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class CategoryFoodSpinnerInstaller {
    public static CategoryFoodAdapter setup(Context context, FragmentSelectFoodsBinding binding,
                                      final OnSpinnerStateListener.DataProcessor dataProcessor) {
        Spinner spinner = binding.spinnerCategoryFoods;
        RecyclerView recyclerView = binding.recyclerview;
        final CategoryFoodAdapter adapter = setupAdapter(context, spinner, recyclerView, dataProcessor);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adapter != null) {
                    adapter.onSelectItemIndex(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);

        return adapter;
    }

    private static CategoryFoodAdapter setupAdapter(Context context,
                                                    final Spinner spinner,
                                                    final RecyclerView recyclerView,
                                                    OnSpinnerStateListener.DataProcessor dataProcessor) {

        OnSpinnerStateListener.View viewListener = new OnSpinnerStateListener.View() {
            // Khóa spinner và recyclerview khi đang xử lý dữ liệu vừa được thay đổi của spinner
            @Override
            public void onStartProcessDataChanged() {
                spinner.setEnabled(false);
                recyclerView.setEnabled(false);
            }

            @Override
            public void onEndProcessDataChanged() {
                spinner.setEnabled(true);
                recyclerView.setEnabled(true);
            }

            @Override
            public void onSelectSpinnerItemIndex(int index) {
                spinner.setSelection(index);
            }
        };

        CategoryFoodAdapter adapter = new CategoryFoodAdapter(context, R.layout.item_spinner, viewListener, dataProcessor);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);

        return adapter;
    }
}
