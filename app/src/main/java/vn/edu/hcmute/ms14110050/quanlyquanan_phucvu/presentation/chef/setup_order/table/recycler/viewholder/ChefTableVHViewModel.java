package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.chef.setup_order.table.recycler.viewholder;


import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.BaseVHViewModel;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview.IViewHolder;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class ChefTableVHViewModel extends BaseVHViewModel<IViewHolder> {
    private Table table;


    /*
    * Property
    * */

    public void setTable(Table table) {
        this.table = table;
    }

    /*
    * End Property
    * */
}
