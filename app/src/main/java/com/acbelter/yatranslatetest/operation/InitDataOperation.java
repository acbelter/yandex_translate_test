/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.operation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.acbelter.yatranslatetest.Cache;
import com.acbelter.yatranslatetest.MainApplication;
import com.acbelter.yatranslatetest.Pref;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.network.NetworkClient;
import com.acbelter.yatranslatetest.network.Parser;
import com.acbelter.yatranslatetest.network.YandexTranslateApi;
import com.acbelter.yatranslatetest.repository.HistoryStorage;
import com.acbelter.yatranslatetest.repository.LanguageStorage;
import com.acbelter.yatranslatetest.util.Logger;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InitDataOperation extends ChronosOperation<Boolean> {
    private LanguageStorage mLanguageStorage;
    private HistoryStorage mHistoryStorage;

    public InitDataOperation(Context context) {
        mLanguageStorage = LanguageStorage.getInstance(context);
        mHistoryStorage = HistoryStorage.getInstance(context);
    }

    @Nullable
    @Override
    public Boolean run() {
        // For testing: simulate slow network connection
        if (MainApplication.SIMULATE_SLOW_NETWORK) {
            try {
                Thread.sleep(MainApplication.SLOW_NETWORK_DELAY);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        mHistoryStorage.load();

        if (Pref.isLanguagesLoaded()) {
            mLanguageStorage.load();
        } else {
            Pref.setRecentLangCodeFrom(null);
            Pref.setRecentLangCodeTo(null);

            List<LanguageModel> languages = loadLanguagesFromNetwork();
            mLanguageStorage.save(languages);
        }

        if (!mLanguageStorage.isLanguagesLoaded()) {
            return false;
        } else {
            Pref.setLanguagesLoaded(true);
        }

        try {
            // Prevents instant screen hiding
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignore
        }

        return true;
    }

    private List<LanguageModel> loadLanguagesFromNetwork() {
        OkHttpClient client = NetworkClient.provideOkHttpClient(Cache.provideCacheDir());

        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noCache().build())
                // TODO Detect language code by current locale
                .url(YandexTranslateApi.buildGetLanguagesUrl("en"))
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code while data initialization. Body: "
                        + response.body().string());
            }

            String data = response.body().string();
            return Parser.parseLanguages(data);
        } catch (Exception e) {
            Logger.printStackTrace(e);
            return new ArrayList<>(0);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Boolean>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<Boolean> {

    }
}
