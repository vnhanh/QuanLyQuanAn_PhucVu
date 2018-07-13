package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.recyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.presentation.setup_order.abstracts.IRecyclerAdapter;

public abstract class BaseAdapter<VH extends BaseViewHolder, DATA> extends RecyclerView.Adapter<VH>
        implements IRecyclerAdapter<DATA> {
    private WeakReference<Activity> weakActivity;
    protected ArrayList<DATA> list = new ArrayList<>();

    public BaseAdapter(Activity activity) {
        weakActivity = new WeakReference<>(activity);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return this;
    }

    @Override
    public ArrayList<DATA> getList() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public void onGetList(ArrayList<DATA> list) {
        this.list = list;
        Log.d("LOG", getClass().getSimpleName() + ":onGetList()");
        sortList();
    }

    @Override
    public void clearAll() {
        list.clear();
        runNotifyDataSetChanged();
    }

    // có thể định nghĩa sortList() nếu cần hoặc không
    protected abstract void sortList();

    protected void runNotifyDataSetChanged() {
        Activity activity = weakActivity.get();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    protected final int VIEW_EMPTY = 0;

    protected boolean constainData() {
        return list != null && list.size() > 0;
    }

    @Override
    public int getItemCount() {
        return constainData() ? list.size() : 1;
    }
}
