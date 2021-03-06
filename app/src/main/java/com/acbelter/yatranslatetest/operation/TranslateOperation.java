/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.operation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.acbelter.yatranslatetest.Cache;
import com.acbelter.yatranslatetest.MainApplication;
import com.acbelter.yatranslatetest.Pref;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.model.TranslationModel;
import com.acbelter.yatranslatetest.network.ApiCode;
import com.acbelter.yatranslatetest.network.NetworkClient;
import com.acbelter.yatranslatetest.network.Parser;
import com.acbelter.yatranslatetest.network.YandexTranslateApi;
import com.acbelter.yatranslatetest.util.Logger;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Класс асинхронной операции для перевода текста
 */
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
        // Для тестирования: симуляция медленного сетевого соединения
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
//            RequestBody requestBody =
//                    RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "text=" + mText);
            String requestLangFromCode = Pref.isDetectLang() ? null : mLangFromCode;
            // Используется GET-запрос, т.к. он легко кешируется с помощью okhttp
            // TODO Использовать POST-запрос и написать собственный кеш для кеширования POST-запросов
            Request request = new Request.Builder()
                    .url(YandexTranslateApi.buildTranslateUrl(
                            mText, requestLangFromCode, mLangToCode, YandexTranslateApi.FORMAT_PLAIN))
//                    .post(requestBody)
                    .build();

            response = client.newCall(request).execute();
            int code = response.code();
            Logger.d("Translation response code: " + response.code());
            if (code != ApiCode.CODE_OK) {
                // Если запрос прошел и выскочила ошибка серевера, то возвращаем перевод с кодом ошибки
                Logger.d("Unexpected response code while text translation. Body: " + response.body().string());
                return new TranslationModel(code);
            }

            Logger.d("Translation response from cache: " + (response.cacheResponse() != null));

            String data = response.body().string();
            Logger.d("Translation data: " + data);
            TranslationModel translation = Parser.parseTranslation(data);
            // Сохраняем язык перевода и оригинальный текст для отображения истории
            translation.langFromCode = mLangFromCode;
            translation.originalText = mText;
            return translation;
        } catch (IOException e) {
            Logger.printStackTrace(e);
            return null;
        } catch (JSONException e) {
            Logger.printStackTrace(e);
            return new TranslationModel(ApiCode.CODE_SERVER_ERROR);
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
