/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.operation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.acbelter.yatranslatetest.Pref;
import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.network.NetworkClient;
import com.acbelter.yatranslatetest.network.YandexTranslateApi;
import com.acbelter.yatranslatetest.repository.HistoryStorage;
import com.acbelter.yatranslatetest.repository.LanguageStorage;
import com.acbelter.yatranslatetest.util.Logger;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InitDataOperation extends ChronosOperation<Boolean> {
    private LanguageStorage mLanguageStorage;
    private HistoryStorage mHistoryStorage;
    private File mCacheDir;

    public InitDataOperation(Context context) {
        mLanguageStorage = LanguageStorage.getInstance(context);
        mHistoryStorage = HistoryStorage.getInstance(context);
        mCacheDir = context.getCacheDir();
    }

    @Nullable
    @Override
    public Boolean run() {
        List<HistoryItemModel> history = mHistoryStorage.loadFromDatabase();
        mHistoryStorage.setHistory(history);

        if (Pref.isLanguagesLoaded()) {
            List<LanguageModel> languages = mLanguageStorage.loadFromDatabase();
            mLanguageStorage.setLanguages(languages);
        } else {
            List<LanguageModel> languages = loadLanguages();
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
        OkHttpClient client = NetworkClient.provideOkHttpClient(mCacheDir);

        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noCache().build())
                // TODO Determine language code by current locale
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

            List<LanguageModel> languages = new ArrayList<>();
            JSONObject langsObj = new JSONObject(data).getJSONObject("langs");
            Iterator<String> langsIterator = langsObj.keys();
            while (langsIterator.hasNext()) {
                String key = langsIterator.next();
                String value = langsObj.getString(key);
                languages.add(new LanguageModel(key, value));
            }

            return languages;
        } catch (IOException e) {
            Logger.printStackTrace(e);
            return null;
        } catch (JSONException e) {
            Logger.printStackTrace(e);
            return null;
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
