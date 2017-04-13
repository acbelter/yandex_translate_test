/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Pref {
    public static final String KEY_LANGUAGES_LOADED =
            "com.acbelter.yatranslatetest.KEY_LANGUAGES_LOADED";

    private static SharedPreferences sPrefs;

    public static void init(Context context) {
        sPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static boolean isLanguagesLoaded() {
        return sPrefs.getBoolean(KEY_LANGUAGES_LOADED, false);
    }

    public static void setLanguagesLoaded(boolean loaded) {
        sPrefs.edit().putBoolean(KEY_LANGUAGES_LOADED, loaded).apply();
    }
}
