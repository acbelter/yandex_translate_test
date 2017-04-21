/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.app.Activity;
import android.content.Intent;

import com.acbelter.yatranslatetest.Pref;
import com.acbelter.yatranslatetest.RequestConstants;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.storage.LanguageStorage;
import com.acbelter.yatranslatetest.view.SelectLangView;

public class SelectLangPresenter implements Presenter<SelectLangView> {
    private int mPresenterId;
    private boolean mDetectLanguageState;
    private boolean mDetectLanguageUiVisible;
    private LanguageModel mSelectedLanguage;

    public SelectLangPresenter(LanguageStorage storage, SelectLangMode mode) {
        mDetectLanguageState = Pref.isDetectLang();
        mDetectLanguageUiVisible = mode == SelectLangMode.LANG_FROM;

        switch (mode) {
            case LANG_FROM:
                mSelectedLanguage = storage.getLanguageByCode(Pref.getRecentLangCodeFrom());
                break;
            case LANG_TO:
                mSelectedLanguage = storage.getLanguageByCode(Pref.getRecentLangCodeTo());
                break;
        }
    }

    public void setDetectLanguageState(Activity activity, boolean state) {
        mDetectLanguageState = state;
        Pref.setDetectLang(state);

        Intent data = new Intent();
        data.putExtra(RequestConstants.KEY_LANG, mSelectedLanguage);
        activity.setResult(Activity.RESULT_OK, data);
    }

    public void setSelectedLanguage(LanguageModel language) {
        mSelectedLanguage = language;
    }

    public void finishLanguageSelection(Activity activity) {
        Intent data = new Intent();
        data.putExtra(RequestConstants.KEY_LANG, mSelectedLanguage);
        activity.setResult(Activity.RESULT_OK, data);
        activity.finish();
    }

    public LanguageModel getSelectedLanguage() {
        return mSelectedLanguage;
    }

    @Override
    public void present(SelectLangView view) {
        view.setDetectLanguageState(mDetectLanguageState);
        view.setDetectLanguageUiVisible(mDetectLanguageUiVisible);
        view.setSelectedLanguage(mSelectedLanguage);
    }

    @Override
    public void setId(int id) {
        mPresenterId = id;
    }

    @Override
    public int getId() {
        return mPresenterId;
    }
}
