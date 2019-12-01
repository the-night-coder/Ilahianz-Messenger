package com.nightcoder.ilahianz.Supports;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Graphics {

    public static int[] getResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int[] dimen = {0, 0};
        dimen[0] = metrics.widthPixels;
        dimen[1] = metrics.heightPixels;
        return dimen;
    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public static float[] getResolutionDP(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float[] dimen = {0, 0};
        dimen[0] = metrics.widthPixels / metrics.density;
        dimen[1] = metrics.heightPixels / metrics.density;
        return dimen;
    }

    public static void setGifImage(Context context, int sourceId, ImageView view) {
        Glide.with(context).load(sourceId)
                .crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }
}
