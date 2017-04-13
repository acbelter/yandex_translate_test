/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.HttpUrl;

public class YandexTranslateApi {
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
    public static HttpUrl buildTranslateUrl(String text, String lang, String format)
            throws IllegalArgumentException {
        if (!FORMAT_PLAIN.equals(format) || !FORMAT_HTML.equals(format)) {
            throw new IllegalArgumentException("Invalid format");
        }

        String encodedText;
        try {
            // The source text must be URL-encoded
            encodedText = URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to encode text");
        }

        return BASE_URL.newBuilder()
                .addPathSegment("translate")
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("text", encodedText)
                .addQueryParameter("lang", lang)
                .addQueryParameter("format", format)
                .build();
    }
}