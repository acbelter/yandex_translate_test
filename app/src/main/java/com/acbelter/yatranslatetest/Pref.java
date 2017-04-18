/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.acbelter.yatranslatetest.model.LanguageModel;

public class Pref {
    public static final String KEY_LANGUAGES_LOADED = "languages_loaded";
    public static final String KEY_RECENT_LANG_CODE_FROM = "recent_lang_code_from";
    public static final String KEY_RECENT_LANG_CODE_TO = "recent_lang_code_to";
    public static final String KEY_DETECT_LANG = "detect_lang";

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
        return sPrefs.getString(KEY_RECENT_LANG_CODE_FROM, null);
    }

    public static void setRecentLangCodeFrom(LanguageModel language) {
        String code = language != null ? language.code : null;
        sPrefs.edit().putString(KEY_RECENT_LANG_CODE_FROM, code).apply();
    }

    public static String getRecentLangCodeTo() {
        return sPrefs.getString(KEY_RECENT_LANG_CODE_TO, null);
    }

    public static void setRecentLangCodeTo(LanguageModel language) {
        String code = language != null ? language.code : null;
        sPrefs.edit().putString(KEY_RECENT_LANG_CODE_TO, code).apply();
    }

    public static boolean isDetectLang() {
        return sPrefs.getBoolean(KEY_DETECT_LANG, false);
    }

    public static void setDetectLang(boolean detect) {
        sPrefs.edit().putBoolean(KEY_DETECT_LANG, detect).apply();
    }
}
