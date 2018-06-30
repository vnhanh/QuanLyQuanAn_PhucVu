package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.table.spinner;

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
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.util.StringUtils;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.asynctask.SortTask;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.region.Region;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.ISpinnerDataListener;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class RegionAdapter extends ArrayAdapter implements ISpinnerDataListener<Region> {
    private ArrayList<Region> regions;
    private String currentRegionID;

    @NonNull
    private OnSpinnerStateListener.View viewListener;

    // đơn vị xử lý logic
    @NonNull
    private OnSpinnerStateListener.DataProcessor dataProcessorListener;

    public RegionAdapter(@NonNull Context context, int resource,
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
        return regions == null || regions.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmptyList() ? 1 : regions.size();
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
            content = getContext().getString(R.string.no_region_item);
        }else{
            content = regions.get(position).getName();
        }
        textView.setText(content);
    }

    /*
    * Thao tác với dữ liệu
    * */

    public String getCurrentRegionID() {
        return currentRegionID;
    }

    @Override
    public void onGetList(ArrayList<Region> regions) {
        this.regions = regions;
        onDataChanged();
    }

    @Override
    public void onAddItem(Region region) {
        if (regions == null) {
            regions = new ArrayList<>();
        }
        regions.add(region);
        onDataChanged();
    }

    @Override
    public void onUpdateItem(Region region) {
        String id = region.getId();
        int index = findItem(id);
        if (index > -1) {
            regions.set(index, region);
        }
        onDataChanged();
    }

    @Override
    public void onDeleteItem(@NonNull String regionID) {
        int index = findItem(regionID);
        if (index > -1) {
            regions.remove(index);
            // xóa đúng item đang được chọn
            if (regionID.equals(currentRegionID)) {
                currentRegionID = regions.size() > 0 ? regions.get(0).getId() : "";
                if (regions.size() > 0) {
                    viewListener.onSelectSpinnerItemIndex(0);
                }
                dataProcessorListener.onSelectSpinnerItemId(currentRegionID);
            }
            notifyDataSetChanged();
        }
    }

    private void onDataChanged() {
        viewListener.onStartProcessDataChanged();
        sortRegions();
    }

    private void sortRegions() {
        new SortTask<Region>(
                regions,
                new Comparator<Region>() {
                    @Override
                    public int compare(Region region, Region _region) {
                        String regionNameDeAccent = StringUtils.deAccent(region.getName());
                        String _regionNameDeAccent = StringUtils.deAccent(_region.getName());
                        return regionNameDeAccent.compareToIgnoreCase(_regionNameDeAccent);
                    }
                }, new GetCallback<ArrayList<Region>>() {
                    @Override
                    public void onFinish(ArrayList<Region> _regions) {
                        onSortListFinish(_regions);
                    }
                })
                .execute();
    }

    private void onSortListFinish(ArrayList<Region> _regions) {
        this.regions = _regions;
        notifyDataSetChanged();
        onResetSelectItem();
    }

    private int findItem(String id){
        int size = regions.size();
        for (int i = 0; i < size; i++) {
            Region _region = regions.get(i);
            if (_region.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public void onSelectItemIndex(int position) {
        if (regions == null || position < 0 || position >= regions.size()) {
            return;
        }

        String idItemSelected = position < 0 ? "" : regions.get(position).getId();

        if (!idItemSelected.equals(currentRegionID)) {
            currentRegionID = idItemSelected;
            dataProcessorListener.onSelectSpinnerItemId(currentRegionID);
        }
    }

    // Chọn item cho spinner
    // Khi dữ liệu được làm mới lại hoặc item đang chọn đã bị xóa trên database
    private void onResetSelectItem() {
        int index = 0;
        // cờ đánh dấu có thay đổi item hay không ? (để thực hiện việc load lại dữ liệu)
        boolean isChangeItem = false;

        if (!StringUtils.isEmpty(currentRegionID)) {
            Log.d("LOG", getClass().getSimpleName() + ":onResetSelectItem():current item was existed");

            // tìm vị trí của item đang select
            int _index = 0;
            int size = regions.size();
            for (; _index < size; _index++) {
                if (regions.get(_index).getId().equals(currentRegionID)) {
                    index = _index;
                    break;
                }
            }

            if (_index == size) {
                Log.d("LOG", getClass().getSimpleName() + ":onResetSelectItem():current item just change");
                isChangeItem = true;
                currentRegionID = "";
            }
        }
        // kết thúc việc xử lý khi dữ liệu thay đổi
        viewListener.onEndProcessDataChanged();
        // set selected index cho spinner
        viewListener.onSelectSpinnerItemIndex(index);
        // truyền region id cho viewmodel load list table theo region
        if (isChangeItem) {
            dataProcessorListener.onSelectSpinnerItemId(currentRegionID);
        }
    }
}
