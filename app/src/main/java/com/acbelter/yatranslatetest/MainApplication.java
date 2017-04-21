/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.app.Application;

import com.facebook.stetho.Stetho;

import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;

public class MainApplication extends Application {
    public static final String TAG = "YATEST";
    public static final boolean DEBUG = true;
    public static final boolean SIMULATE_SLOW_NETWORK = false;
    public static final long SLOW_NETWORK_DELAY = 3000L;

    @Override
    public void onCreate() {
        super.onCreate();
        CupboardFactory.setCupboard(new CupboardBuilder().useAnnotations().build());
        Stetho.initializeWithDefaults(this);
        Pref.init(this);
        Cache.init(this);
    }
}
