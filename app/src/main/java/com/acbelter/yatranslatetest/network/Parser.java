/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.network;

import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.model.TranslationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser {
    public static List<LanguageModel> parseLanguages(String data) throws JSONException {
        List<LanguageModel> languages = new ArrayList<>();
        JSONObject langs = new JSONObject(data).getJSONObject("langs");
        Iterator<String> langsIterator = langs.keys();
        while (langsIterator.hasNext()) {
            String key = langsIterator.next();
            String value = langs.getString(key);
            languages.add(new LanguageModel(key, value));
        }
        return languages;
    }

    public static TranslationModel parseTranslation(String data) throws JSONException {
        TranslationModel translation = new TranslationModel();
        JSONObject translationObj = new JSONObject(data);
        translation.code = translationObj.getInt("code");

        JSONObject detectedLang = translationObj.optJSONObject("detected");
        if (detectedLang != null) {
            translation.detectedLangCode = detectedLang.getString("lang");
        }

        String[] langs = translationObj.getString("lang").split("-");
        translation.langFromCode = langs[0];
        translation.langToCode = langs[1];

        JSONArray textArray = translationObj.getJSONArray("text");
        for (int i = 0; i < textArray.length(); i++) {
            translation.addTranslation(textArray.getString(i));
        }
        return translation;
    }
}
