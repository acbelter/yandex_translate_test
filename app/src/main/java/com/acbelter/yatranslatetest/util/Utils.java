/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.acbelter.yatranslatetest.R;

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

    public static void tintToolbarIcons(Toolbar toolbar, int colorResId) {
        final int color = ContextCompat.getColor(toolbar.getContext(), colorResId);
        Menu menu = toolbar.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            tintMenuItemIcon(color, menu.getItem(i));
        }
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon != null) {
            toolbar.setNavigationIcon(Utils.tintDrawable(toolbar.getContext(), navigationIcon, colorResId));
        }
    }

    private static void tintMenuItemIcon(int color, MenuItem item) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable.mutate(), color);
            item.setIcon(wrappedDrawable);
        }
    }

    public static void tintIndeterminateProgressBar(ProgressBar progressBar) {
        progressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(progressBar.getContext(), R.color.colorAccent),
                PorterDuff.Mode.SRC_IN);
    }


    public static void attachToolbar(Activity activity, Toolbar toolbar, boolean showBackButton) {
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showBackButton);
        }
    }
}
