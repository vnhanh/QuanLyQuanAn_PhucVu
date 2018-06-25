package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.asynctask;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.callbacks.GetCallback;
import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.food.CategoryFood;

/**
 * Created by Vo Ngoc Hanh on 6/18/2018.
 */

public class SortTask<T> extends AsyncTask<Void,Void, Void> {
    @NonNull
    private ArrayList<T> list;
    @NonNull
    private Comparator<T> comparator;
    @NonNull
    private GetCallback<ArrayList<T>> callback;

    public SortTask(@NonNull ArrayList<T> list, @NonNull Comparator<T> comparator,
                    @NonNull GetCallback<ArrayList<T>> callback) {
        this.list = list;
        this.comparator = comparator;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... args) {
        Collections.sort(list, this.comparator);
        return null;
    }

    @Override
    protected void onPostExecute(Void _void) {
        callback.onFinish(this.list);
    }
}
