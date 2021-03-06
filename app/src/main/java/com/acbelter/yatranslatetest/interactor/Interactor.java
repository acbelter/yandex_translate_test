/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.interactor;

import android.content.Context;

import com.acbelter.yatranslatetest.model.LanguageModel;

/**
 * Интерфейс, описывающий запуск асинхронных операций
 */
public interface Interactor {
    void startInitData(Context context);
    void startTranslation(String text,
                          LanguageModel langFrom,
                          LanguageModel langTo);
    void cancelTranslation();
}
