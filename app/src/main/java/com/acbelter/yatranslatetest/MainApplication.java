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
    // Включение/выключение режима отладки
    public static final boolean DEBUG = true;
    // Включение/выключение режима симуляции медленного сетевого соединения
    public static final boolean SIMULATE_SLOW_NETWORK = false;
    // Задержка для эмуляции медленного сетевого соединения
    public static final long SLOW_NETWORK_DELAY = 3000L;

    @Override
    public void onCreate() {
        super.onCreate();
        // Включаем в cupboard поддержку аннотации
        CupboardFactory.setCupboard(new CupboardBuilder().useAnnotations().build());
        // Инициализируем библиотеку для отладки
        Stetho.initializeWithDefaults(this);
        // Инициализируем кеш и хранилище настроек
        Pref.init(this);
        Cache.init(this);
    }
}
