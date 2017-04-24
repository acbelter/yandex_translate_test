/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.network;

import com.acbelter.yatranslatetest.util.Logger;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkClient {
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    // Кешировать данные на 1 день
                    .header("Cache-Control", "max-age=" + 60 * 60 * 24)
                    .build();
        }
    };

    public static OkHttpClient provideOkHttpClient(File cacheDir) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
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
            cache = new Cache(new File(cacheDir, "yacache"), 2 * 1024 * 1024);  // 2 Мб
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
