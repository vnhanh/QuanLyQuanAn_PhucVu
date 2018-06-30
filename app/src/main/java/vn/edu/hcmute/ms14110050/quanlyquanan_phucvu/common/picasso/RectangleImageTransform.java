package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;


/**
 * Created by Vo Ngoc Hanh on 6/9/2018.
 */

public class RectangleImageTransform implements Transformation {
    private int width, height, corner;
    @ScaleType
    private int scaleType;
    @ColorInt
    private int bgColor = -1;

    // Có 2 mode hiện tại là CENTER_CROP và CENTER_INSIDE
    public RectangleImageTransform(int widthView, int heightView, int corner, @ScaleType int scaleType) {
        width = widthView;
        height = heightView;
        this.corner = corner;
        this.scaleType = scaleType;
    }

    // Set màu nền (chỉ dùng cho CENTER_INSIDE)
    public void setBackgroundColor(@ColorInt int color) {
        this.bgColor = color;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (scaleType != ScaleType.CENTER_CROP && scaleType != ScaleType.CENTER_INSIDE) {
            return null;
        }

        float widthScale = width * 1.0f / source.getWidth();
        float heightScale = height * 1.0f/ source.getHeight();
        float scale = 0;
        Matrix matrix = new Matrix();

        // 2 chiều của imageview sẽ bằng hoặc nhỏ hơn cả 2 chiều của ảnh bitmap được return
        if (scaleType == ScaleType.CENTER_CROP) {
            // chọn scale lớn nhất
            scale = Math.max(widthScale, heightScale);
        } else if (scaleType == ScaleType.CENTER_INSIDE) {
            scale = Math.min(widthScale, heightScale);
        }

        matrix.postScale(scale, scale);

        //thu nhỏ hoặc phóng to bức ảnh theo tỉ lệ scale
        Bitmap bitmap01 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
        if (bitmap01 != source) {
            source.recycle();
        }

        // chỉnh lại phạm vi của ảnh bitmap (vẽ từ đâu đến đâu)
        Bitmap bitmap02 = Bitmap.createBitmap(width, height, bitmap01.getConfig());
        Canvas canvas02 = new Canvas(bitmap02);
        if (scaleType == ScaleType.CENTER_INSIDE && bgColor > -1) {
            canvas02.drawColor(bgColor);
        }
        canvas02.drawBitmap(bitmap01, (width - bitmap01.getWidth())/2, (height - bitmap01.getHeight())/2, null);

        if (!bitmap01.equals(bitmap02)) {
            bitmap01.recycle();
        }

        // không bo góc
        if (corner == 0) {
            return bitmap02;
        }
        // có bo góc
        else{
            Bitmap bitmap03 = Bitmap.createBitmap(width, height, bitmap02.getConfig());
            Canvas canvas03 = new Canvas(bitmap03);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(bitmap02, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvas03.drawRoundRect(new RectF(0,0,width,height), corner, corner, paint);

            if (!bitmap02.equals(bitmap03)) {
                bitmap02.recycle();
            }

            return bitmap03;
        }
    }

    @Override
    public String key() {
        return "RectangleImageTransform";
    }
}
