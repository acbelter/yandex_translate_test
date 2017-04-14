/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import com.acbelter.yatranslatetest.Pref;
import com.acbelter.yatranslatetest.interactor.Interactor;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.repository.LanguageStorage;
import com.acbelter.yatranslatetest.view.TranslationView;

public class TranslationPresenter implements Presenter {
    private int mPresenterId;
    private Interactor mInteractor;

    private LanguageStorage mLanguageStorage;

    private LanguageModel mLanguageFrom;
    private LanguageModel mLanguageTo;

    public TranslationPresenter(LanguageStorage languageStorage) {
        mLanguageStorage = languageStorage;
    }

    public void initLanguages(TranslationView view) {
        String fromCode = Pref.getRecentLangCodeFrom();
        String toCode = Pref.getRecentLangCodeTo();

        mLanguageFrom = mLanguageStorage.getLanguageByCode(fromCode);
        mLanguageTo = mLanguageStorage.getLanguageByCode(toCode);

        view.setLanguageFrom(mLanguageFrom);
        view.setLanguageTo(mLanguageTo);
    }

    public void swapLanguages(TranslationView view) {
        LanguageModel temp = mLanguageFrom;
        mLanguageFrom = mLanguageTo;
        mLanguageTo = temp;
        view.setLanguageFrom(mLanguageFrom);
        view.setLanguageTo(mLanguageTo);
    }

    public LanguageModel getLanguageFrom() {
        return mLanguageFrom;
    }

    public LanguageModel getLanguageTo() {
        return mLanguageTo;
    }

    public void setInteractor(Interactor interactor) {
        mInteractor = interactor;
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
