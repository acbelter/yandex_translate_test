/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.network;

import android.text.TextUtils;

import java.net.MalformedURLException;

import okhttp3.HttpUrl;

public class YandexTranslateApi {
    public static final int MAX_TEXT_LENGTH = 10000;

    private static final HttpUrl BASE_URL =
            HttpUrl.parse("https://translate.yandex.net/api/v1.5/tr.json");
    private static final String API_KEY =
            "trnsl.1.1.20170329T120411Z.7fa61d7216d8b52e.308c66b8407100eae88b93dbc8b99604324c9ac4";
    public static final String FORMAT_PLAIN = "plain";
    public static final String FORMAT_HTML = "html";

    // See https://tech.yandex.ru/translate/doc/dg/reference/getLangs-docpage/
    public static HttpUrl buildGetLanguagesUrl(String ui) {
        return BASE_URL.newBuilder()
                .addPathSegment("getLangs")
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("ui", ui)
                .build();
    }

    // See https://tech.yandex.ru/translate/doc/dg/reference/translate-docpage/
    public static HttpUrl buildTranslateUrl(String langFromCode,
                                            String langToCode,
                                            String format) throws MalformedURLException {
        if (!FORMAT_PLAIN.equals(format) && !FORMAT_HTML.equals(format)) {
            throw new MalformedURLException("Invalid format");
        }

        if (TextUtils.isEmpty(langToCode)) {
            throw new MalformedURLException("Invalid second language code");
        }

        String lang;
        if (TextUtils.isEmpty(langFromCode)) {
            lang = langToCode;
        } else {
            lang = langFromCode + "-" + langToCode;
        }

        return BASE_URL.newBuilder()
                .addPathSegment("translate")
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("lang", lang)
                .addQueryParameter("format", format)
                .addQueryParameter("options", "1")
                .build();
    }
}