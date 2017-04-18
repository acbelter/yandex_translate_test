/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view;

import com.acbelter.yatranslatetest.model.LanguageModel;

public interface SelectLangView {
    void setDetectLanguageState(boolean state);
    void setDetectLanguageUiVisible(boolean visible);
    void setSelectedLanguage(LanguageModel language);
}
