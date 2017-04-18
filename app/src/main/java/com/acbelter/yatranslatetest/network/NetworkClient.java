/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.network;

import com.acbelter.yatranslatetest.util.Logger;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkClient {
    public static OkHttpClient provideOkHttpClient(File cacheDir) {
        return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .cache(provideCache(cacheDir))
                .build();
    }

    private static Cache provideCache(File cacheDir) {
        if (cacheDir == null) {
            return null;
        }

        Cache cache = null;
        try {
            cache = new Cache(new File(cacheDir, "yacache"), 2 * 1024 * 1024);  // 2 MB
        } catch (Exception e) {
            Logger.d("Could not create cache");
        }
        return cache;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Logger.d(message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return httpLoggingInterceptor;
    }
}
