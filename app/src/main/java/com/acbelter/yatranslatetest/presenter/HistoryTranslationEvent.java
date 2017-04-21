/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import com.acbelter.yatranslatetest.model.TranslationModel;

public class HistoryTranslationEvent {
    private TranslationModel mTranslation;

    public HistoryTranslationEvent(TranslationModel translation) {
        mTranslation = translation;
    }

    public TranslationModel getTranslation() {
        return mTranslation;
    }
}
