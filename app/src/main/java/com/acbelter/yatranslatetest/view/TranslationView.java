/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view;

import com.acbelter.yatranslatetest.model.LanguageModel;

public interface TranslationView extends PresenterView {
    void setLanguageFrom(LanguageModel language);
    void setLanguageTo(LanguageModel language);
}
