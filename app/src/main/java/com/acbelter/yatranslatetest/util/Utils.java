/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

public final class Utils {
    private Utils() {}

    public static Drawable getTintDrawable(Context context, int drawableResId, int colorResId) {
        Drawable wrappedDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableResId));
        DrawableCompat.setTint(wrappedDrawable.mutate(), ContextCompat.getColor(context, colorResId));
        return wrappedDrawable;
    }

    public static Drawable tintDrawable(Context context, Drawable drawable, int colorResId) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable.mutate(), ContextCompat.getColor(context, colorResId));
        return wrappedDrawable;
    }

    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
