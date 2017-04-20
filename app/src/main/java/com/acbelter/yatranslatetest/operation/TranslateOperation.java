/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.operation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.acbelter.yatranslatetest.Cache;
import com.acbelter.yatranslatetest.MainApplication;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.model.TranslationModel;
import com.acbelter.yatranslatetest.network.NetworkClient;
import com.acbelter.yatranslatetest.network.Parser;
import com.acbelter.yatranslatetest.network.YandexTranslateApi;
import com.acbelter.yatranslatetest.util.Logger;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateOperation extends ChronosOperation<TranslationModel> {
    private String mText;
    private String mLangFromCode;
    private String mLangToCode;

    public TranslateOperation(String text,
                              LanguageModel langFrom,
                              LanguageModel langTo) {
        mText = text;
        mLangFromCode = langFrom != null ? langFrom.code : null;
        mLangToCode = langTo != null ? langTo.code : null;
    }

    @Nullable
    @Override
    public TranslationModel run() {
        if (TextUtils.isEmpty(mText)) {
            return null;
        }

        if (mText.length() > YandexTranslateApi.MAX_TEXT_LENGTH) {
            return null;
        }

        // For testing: simulate slow network connection
        if (MainApplication.SIMULATE_SLOW_NETWORK) {
            try {
                Thread.sleep(MainApplication.SLOW_NETWORK_DELAY);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        OkHttpClient client = NetworkClient.provideOkHttpClient(Cache.provideCacheDir());

        Response response = null;
        try {
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "text=" + mText);
            Request request = new Request.Builder()
                    .url(YandexTranslateApi.buildTranslateUrl(
                            mLangFromCode, mLangToCode, YandexTranslateApi.FORMAT_PLAIN))
                    .post(requestBody)
                    .build();

            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code while text translation. Body: "
                        + response.body().string());
            }

            Logger.d("Translation response from cache: " + (response.cacheResponse() != null));

            String data = response.body().string();
            Logger.d("Translation: " + data);
            TranslationModel translation = Parser.parseTranslation(data);
            if (translation.code == 200) {
                translation.originalText = mText;
                return translation;
            }

            return null;
        } catch (Exception e) {
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
    public Class<? extends ChronosOperationResult<TranslationModel>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<TranslationModel> {

    }
}
