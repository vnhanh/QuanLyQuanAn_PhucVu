package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.ScaleType.CENTER_CROP;
import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.picasso.ScaleType.CENTER_INSIDE;

/**
 * Created by Vo Ngoc Hanh on 6/28/2018.
 */

@IntDef({CENTER_CROP, CENTER_INSIDE})
@Retention(RetentionPolicy.SOURCE)
public @interface ScaleType {
    int CENTER_CROP = 0;
    int CENTER_INSIDE = 1;
}
