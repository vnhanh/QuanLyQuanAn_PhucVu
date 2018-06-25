package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.custom_view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;

/**
 * Created by Vo Ngoc Hanh on 6/20/2018.
 */

public class ConfirmDialog {
    public static AlertDialog create(Context context,
                                     @StringRes int titleIdRes,
                                     @StringRes int messageIdRes,
                                     @NonNull DialogInterface.OnClickListener positiveOnCLick,
                                     @Nullable DialogInterface.OnClickListener negativeOnClick,
                                     @Nullable @StyleRes Integer animationStyle) {

        String title = context.getString(titleIdRes);
        String message = context.getString(messageIdRes);

        return create(context, title, message, positiveOnCLick, negativeOnClick, animationStyle);
    }

    public static AlertDialog create(final Context context,
                                     @NonNull String title,
                                     @NonNull String message,
                                     @NonNull DialogInterface.OnClickListener positiveOnCLick,
                                     @Nullable DialogInterface.OnClickListener negativeOnClick,
                                     @Nullable @StyleRes Integer animationStyle) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_confirm_dialog, null, false);
        builder.setView(view);
        TextView titleView = view.findViewById(R.id.txt_title);
        TextView messageView = view.findViewById(R.id.txt_message);

        titleView.setText(title);
        messageView.setText(message);

        builder.setPositiveButton(R.string.action_yes, positiveOnCLick);
        if (negativeOnClick != null) {
            builder.setNegativeButton(R.string.action_no, negativeOnClick);
        }
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface _dialog) {
                int positiveColor = ContextCompat.getColor(context, R.color.colorTeal);
                int negativeColor = ContextCompat.getColor(context, R.color.colorOrange);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(positiveColor);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(negativeColor);

                int textSize = context.getResources().getDimensionPixelSize(R.dimen.txtSize_xlarge);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        });
        if (animationStyle != null && dialog.getWindow()!=null) {
            dialog.getWindow().getAttributes().windowAnimations = animationStyle;
        }

        return dialog;
    }
}
