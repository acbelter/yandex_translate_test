/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.acbelter.yatranslatetest.Pref;
import com.acbelter.yatranslatetest.RequestConstants;
import com.acbelter.yatranslatetest.interactor.Interactor;
import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.model.TranslationModel;
import com.acbelter.yatranslatetest.repository.HistoryStorage;
import com.acbelter.yatranslatetest.repository.LanguageStorage;
import com.acbelter.yatranslatetest.view.TranslationView;
import com.acbelter.yatranslatetest.view.ui.SelectLangActivity;

import de.greenrobot.event.EventBus;

public class TranslationPresenter implements Presenter<TranslationView> {
    private int mPresenterId;
    private Interactor mInteractor;

    private LanguageStorage mLanguageStorage;
    private HistoryStorage mHistoryStorage;

    private LanguageModel mLanguageFrom;
    private LanguageModel mLanguageTo;

    private TranslationModel mCurrentTranslation;

    private boolean mIsFirstPresentation;

    public TranslationPresenter(LanguageStorage languageStorage, HistoryStorage historyStorage) {
        mLanguageStorage = languageStorage;
        mHistoryStorage = historyStorage;
        mIsFirstPresentation = true;
    }

    public void initLanguages(TranslationView view) {
        String fromCode = Pref.getRecentLangCodeFrom();
        String toCode = Pref.getRecentLangCodeTo();
        // No recent language
        if (fromCode == null) {
            // TODO Detect language code by current locale
            fromCode = "en";
        }

        mLanguageFrom = mLanguageStorage.getLanguageByCode(fromCode);
        Pref.setRecentLangCodeFrom(mLanguageFrom);

        // No recent language
        if (toCode == null) {
            // TODO Detect language code by current locale
            toCode = "ru";
        }

        mLanguageTo = mLanguageStorage.getLanguageByCode(toCode);
        Pref.setRecentLangCodeTo(mLanguageTo);

        view.setLanguageFrom(mLanguageFrom);
        view.setLanguageTo(mLanguageTo);
    }

    public void swapLanguages(TranslationView view) {
        LanguageModel temp = mLanguageFrom;
        mLanguageFrom = mLanguageTo;
        mLanguageTo = temp;
        view.setLanguageFrom(mLanguageFrom);
        view.setLanguageTo(mLanguageTo);
        Pref.setRecentLangCodeFrom(mLanguageFrom);
        Pref.setRecentLangCodeTo(mLanguageTo);
    }

    public void startSelectLanguageFrom(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), SelectLangActivity.class);
        intent.putExtra(RequestConstants.KEY_SELECT_LANG_MODE, SelectLangMode.LANG_FROM.name());
        fragment.startActivityForResult(intent, RequestConstants.REQUEST_CODE_SELECT_LANG_FROM);
    }

    public void startSelectLanguageTo(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), SelectLangActivity.class);
        intent.putExtra(RequestConstants.KEY_SELECT_LANG_MODE, SelectLangMode.LANG_TO.name());
        fragment.startActivityForResult(intent, RequestConstants.REQUEST_CODE_SELECT_LANG_TO);
    }

    public void setLanguageFrom(TranslationView view, LanguageModel language) {
        mLanguageFrom = language;
        view.setLanguageFrom(language);
        Pref.setRecentLangCodeFrom(mLanguageFrom);
    }

    public void setLanguageTo(TranslationView view, LanguageModel language) {
        mLanguageTo = language;
        view.setLanguageTo(language);
        Pref.setRecentLangCodeFrom(mLanguageTo);
    }

    public LanguageModel getLanguageFrom() {
        return mLanguageFrom;
    }

    public LanguageModel getLanguageTo() {
        return mLanguageTo;
    }

    public void startTranslation(TranslationView view, String text) {
        if (mInteractor == null) {
            return;
        }

        clearTranslation(view);

        if (Pref.isDetectLang()) {
            mInteractor.startTranslation(text, null, mLanguageTo);
        } else {
            mInteractor.startTranslation(text, mLanguageFrom, mLanguageTo);
        }

        view.showTranslationProcess();
    }

    public void cancelTranslation() {
        mInteractor.cancelTranslation();
    }

    public void finishTranslation(TranslationView view, TranslationModel translation) {
        mCurrentTranslation = translation;
        if (translation != null) {
            view.showTranslation(translation);

            HistoryItemModel historyItem = new HistoryItemModel(translation);
            historyItem.timestamp = System.currentTimeMillis();
            mHistoryStorage.saveItem(historyItem);

            // FIXME Small hack: post event to update history
            EventBus.getDefault().post(new HistoryUpdatedEvent());
        } else {
            view.showTranslationFail();
        }
    }

    public void clearTranslation(TranslationView view) {
        mCurrentTranslation = null;
        view.showTranslation(null);
    }

    public void setInteractor(Interactor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void present(TranslationView view) {
        if (mIsFirstPresentation) {
            initLanguages(view);
            mIsFirstPresentation = false;
        } else {
            view.setLanguageFrom(mLanguageFrom);
            view.setLanguageTo(mLanguageTo);
            if (mCurrentTranslation != null) {
                view.showTranslation(mCurrentTranslation);
            } else if (!view.getOriginalText().isEmpty()) {
                mInteractor.startTranslation(view.getOriginalText(), mLanguageFrom, mLanguageTo);
            }
        }
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
