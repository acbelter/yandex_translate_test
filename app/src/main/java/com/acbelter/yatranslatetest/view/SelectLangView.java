/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view;

import com.acbelter.yatranslatetest.model.LanguageModel;

public interface SelectLangView {
    void setDetermineLangUiVisible(boolean visible);
    void setDetermineLanguageState(boolean state);
    void setSelectedLanguage(LanguageModel language);
}
