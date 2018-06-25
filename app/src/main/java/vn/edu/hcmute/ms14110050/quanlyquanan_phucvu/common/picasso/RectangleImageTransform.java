package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.DimenRes;
import android.util.Log;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;


/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class RectangleImageTransform implements Transformation {
    WeakReference<Resources> weakResources;
    int maxSize, corner;

    public RectangleImageTransform(Resources resources, @DimenRes int idRes, @DimenRes int cornerIdRes) {
        weakResources = new WeakReference<Resources>(resources);
        maxSize = resources.getDimensionPixelSize(idRes);
        corner = resources.getDimensionPixelSize(cornerIdRes);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (maxSize < 100) {
            maxSize = 100;
        }
        int size = Math.max(source.getWidth(), source.getHeight());
//        Log.d("LOG", getClass().getSimpleName() + ":size:" + size);
        int actualSize = Math.min(size, maxSize);
//        Log.d("LOG", getClass().getSimpleName() + ":actualSize:" + actualSize);
        float scale = (float) (actualSize * 1.0 / size);
//        Log.d("LOG", getClass().getSimpleName() + ":scale:" + scale);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap _bitmap01 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
        if (_bitmap01 != source) {
            source.recycle();
        }
        // không bo góc
        if (corner == 0) {
            return _bitmap01;
        }
        // có bo góc
        else{
            int _w = (int) (source.getWidth() * scale);
            int _h = (int) (source.getHeight() * scale);

            Bitmap _bitmap02 = Bitmap.createBitmap(_w, _h, _bitmap01.getConfig());
            Canvas canvas = new Canvas(_bitmap02);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(_bitmap01, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas.drawRoundRect(new RectF(0,0,_w,_h), corner, corner, paint);
            if (!_bitmap01.equals(_bitmap02)) {
                _bitmap01.recycle();
            }

            return _bitmap02;
        }
    }

    @Override
    public String key() {
        return "RectangleImageTransform";
    }
}
