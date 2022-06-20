package com.cjs.android.testtab.utils;

import android.content.Context;
import android.os.Build;

import androidx.annotation.ColorRes;

public class ResourceUtil {
    public static int getColor(Context context, @ColorRes int colorSrc) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorSrc);
        } else {
            return context.getResources().getColor(colorSrc);
        }
    }
}
