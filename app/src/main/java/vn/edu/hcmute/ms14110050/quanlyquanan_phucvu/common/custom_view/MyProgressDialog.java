package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;


/**
 * Created by Vo Ngoc Hanh on 6/5/2018.
 */

public class MyProgressDialog {
    public static AlertDialog create(Context context, @StringRes int messageIdRes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_progress_dialog, null, false);
        builder.setView(view);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(context.getString(messageIdRes));

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
