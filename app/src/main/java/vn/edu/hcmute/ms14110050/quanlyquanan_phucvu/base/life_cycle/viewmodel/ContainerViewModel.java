package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.life_cycle.viewmodel;

/**
 * Created by Vo Ngoc Hanh on 6/20/2018.
 */

public interface ContainerViewModel<DATA, TYPE_PROCESS> {
    void onProcessTableData(DATA item, TYPE_PROCESS typeProcess);
}
