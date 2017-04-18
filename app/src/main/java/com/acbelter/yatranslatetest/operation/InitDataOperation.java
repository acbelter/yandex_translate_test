/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.operation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.acbelter.yatranslatetest.Cache;
import com.acbelter.yatranslatetest.Pref;
import com.acbelter.yatranslatetest.model.HistoryItemModel;
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
import java.util.Collections;
import java.util.Comparator;
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
        List<HistoryItemModel> history = mHistoryStorage.loadFromDatabase();
        mHistoryStorage.setHistory(history);

        if (Pref.isLanguagesLoaded()) {
            List<LanguageModel> languages = mLanguageStorage.loadFromDatabase();
            Collections.sort(languages, new Comparator<LanguageModel>() {
                @Override
                public int compare(LanguageModel lang1, LanguageModel lang2) {
                    return lang1.label.compareToIgnoreCase(lang2.label);
                }
            });
            mLanguageStorage.setLanguages(languages);
        } else {
            Pref.setRecentLangCodeFrom(null);
            Pref.setRecentLangCodeTo(null);

            List<LanguageModel> languages = loadLanguages();
            Collections.sort(languages, new Comparator<LanguageModel>() {
                @Override
                public int compare(LanguageModel lang1, LanguageModel lang2) {
                    return lang1.label.compareToIgnoreCase(lang2.label);
                }
            });
            mLanguageStorage.setLanguages(languages);
            mLanguageStorage.saveToDatabase();
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

    private List<LanguageModel> loadLanguages() {
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
