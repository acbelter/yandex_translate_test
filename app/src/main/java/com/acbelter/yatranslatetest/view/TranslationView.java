/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view;

import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.model.TranslationModel;

public interface TranslationView extends PresenterView {
    String getOriginalText();
    void setLanguageFrom(LanguageModel language);
    void setLanguageTo(LanguageModel language);
    void showTranslationProcess();
    void showTranslation(TranslationModel translation);
    void showTranslationFail(int textResId);
}
