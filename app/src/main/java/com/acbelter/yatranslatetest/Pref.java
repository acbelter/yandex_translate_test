/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Pref {
    public static final String KEY_LANGUAGES_LOADED = "languages_loaded";
    public static final String KEY_RECENT_LANG_CODE_FROM = "recent_lang_code_from";
    public static final String KEY_RECENT_LANG_CODE_TO = "recent_lang_code_to";

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

    public static String getRecentLangCodeFrom() {
        // TODO Determine language code by current locale
        return sPrefs.getString(KEY_RECENT_LANG_CODE_FROM, "en");
    }

    public void setRecentLangCodeFrom(String code) {
        sPrefs.edit().putString(KEY_RECENT_LANG_CODE_FROM, code).apply();
    }

    public static String getRecentLangCodeTo() {
        // TODO Determine language code by current locale
        return sPrefs.getString(KEY_RECENT_LANG_CODE_TO, "ru");
    }

    public void setRecentLangCodeTo(String code) {
        sPrefs.edit().putString(KEY_RECENT_LANG_CODE_TO, code).apply();
    }
}
