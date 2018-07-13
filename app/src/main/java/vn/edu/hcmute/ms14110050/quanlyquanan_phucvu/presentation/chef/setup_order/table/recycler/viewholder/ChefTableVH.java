package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.table.recycler.viewholder;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.databinding.ChefItemRecyclerTableBinding;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;


/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class ChefTableVH extends BaseViewHolder<ChefItemRecyclerTableBinding, ChefTableVHViewModel> {

    public ChefTableVH(View view) {
        super(view);
    }

    public ChefTableVH(ChefItemRecyclerTableBinding binding) {
        super(binding);
    }

    @Override
    protected ChefTableVHViewModel initViewModel() {
        return new ChefTableVHViewModel();
    }


    public void onBind(Table table) {
        Animation alphaAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        binding.getRoot().startAnimation(alphaAnim);
        viewmodel.setTable(table);
        binding.setTable(table);
    }
}
