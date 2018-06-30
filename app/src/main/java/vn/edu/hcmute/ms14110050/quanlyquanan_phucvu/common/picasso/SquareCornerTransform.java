package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;

/**
 * Created by Vo Ngoc Hanh on 6/26/2018.
 */

public class SquareCornerTransform implements Transformation {
    WeakReference<Context> weakContext;
    int size, corner;

    private Resources getResources() {
        return weakContext.get().getResources();
    }

    private Context getContext() {
        return weakContext.get();
    }

    public SquareCornerTransform(Context context, @DimenRes int idRes, @DimenRes int cornerIdRes) {
        weakContext = new WeakReference<Context>(context);
        size = getResources().getDimensionPixelSize(idRes);
        corner = getResources().getDimensionPixelSize(cornerIdRes);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int _size = Math.max(source.getWidth(), source.getHeight());

        float scale = (float) (size * 1.0 / _size);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap _bitmap01 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
        if (_bitmap01 != source) {
            source.recycle();
        }

        // chỉnh lại phạm vi của ảnh bitmap (vẽ từ đâu đến đâu)
        Bitmap _bitmap02 = Bitmap.createBitmap(size, size, _bitmap01.getConfig());
        Canvas canvas = new Canvas(_bitmap02);
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.colorExtraLightGray));
        canvas.drawBitmap(_bitmap01, (size - _bitmap01.getWidth())/2, (size - _bitmap01.getHeight())/2, null);

        if (_bitmap02 != _bitmap01) {
            _bitmap01.recycle();
        }

        // không bo góc
        if (corner == 0) {
            return _bitmap02;
        }
        // có bo góc
        else{
            // vẽ góc
            Bitmap _bitmap03 = Bitmap.createBitmap(size, size, _bitmap01.getConfig());
            Canvas canvas03 = new Canvas(_bitmap03);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(_bitmap02, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            canvas03.drawRoundRect(new RectF(0,0,size,size), corner, corner, paint);
            if (_bitmap03 != _bitmap02) {
                _bitmap02.recycle();
            }

            return _bitmap03;
        }
    }

    @Override
    public String key() {
        return "SquareCornerTransform";
    }
}
