package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.recycler.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseProgressVH;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ItemRecyclerTableBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.ITableVM;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class TableVH extends BaseProgressVH<ItemRecyclerTableBinding, TableVHViewModel>{

    public TableVH(View view) {
        super(view);
    }

    public TableVH(ItemRecyclerTableBinding binding,
                   @NonNull ITableVM containerViewModel) {
        super(binding);
        viewmodel.setContainerViewModel(containerViewModel);
    }

    @Override
    protected TableVHViewModel initViewModel() {
        return new TableVHViewModel();
    }


    public void onBind(Table table) {
        Animation alphaAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.getRoot().startAnimation(alphaAnim);
        viewmodel.setTable(table);
        binding.setTable(table);
    }
}
