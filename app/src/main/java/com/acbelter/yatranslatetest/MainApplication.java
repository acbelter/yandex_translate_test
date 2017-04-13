/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.app.Application;

public class MainApplication extends Application {
    public static final String TAG = "YATEST";
    public static final boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Pref.init(this);
    }
}
