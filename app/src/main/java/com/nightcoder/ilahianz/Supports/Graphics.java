package com.nightcoder.ilahianz.Supports;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

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
}
