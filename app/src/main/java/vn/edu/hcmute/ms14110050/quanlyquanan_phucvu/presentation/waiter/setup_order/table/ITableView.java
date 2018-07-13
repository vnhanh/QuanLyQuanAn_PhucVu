package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.waiter.setup_order.table;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.contract.LifeCycle;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.Region;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.table.Table;

/**
 * Created by Vo Ngoc Hanh on 6/22/2018.
 */

public interface ITableView extends LifeCycle.View {

    void onShowLoadRegionsProgress();

    void onHideLoadRegionsProgress();
}
