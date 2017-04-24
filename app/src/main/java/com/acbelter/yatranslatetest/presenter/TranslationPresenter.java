/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.acbelter.yatranslatetest.Pref;
import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.RequestConstants;
import com.acbelter.yatranslatetest.interactor.Interactor;
import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.model.TranslationModel;
import com.acbelter.yatranslatetest.network.ApiCode;
import com.acbelter.yatranslatetest.storage.HistoryStorage;
import com.acbelter.yatranslatetest.storage.LanguageStorage;
import com.acbelter.yatranslatetest.util.Logger;
import com.acbelter.yatranslatetest.view.TranslationView;
import com.acbelter.yatranslatetest.view.ui.SelectLangActivity;

import java.lang.ref.WeakReference;

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
        // Если нет сохраненного языка, то используем английский язык
        if (fromCode == null) {
            fromCode = "en";
        }

        mLanguageFrom = mLanguageStorage.getLanguageByCode(fromCode);
        Pref.setRecentLangCodeFrom(mLanguageFrom);

        // Если нет сохраненного языка, то используем дефолное значение
        if (toCode == null) {
            // TODO Определять язык по текущей локале
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
        Pref.setRecentLangCodeTo(mLanguageTo);
    }

    public LanguageModel getLanguageFrom() {
        return mLanguageFrom;
    }

    public LanguageModel getLanguageTo() {
        return mLanguageTo;
    }

    public void startTranslation(TranslationView view, String text) {
        if (mInteractor == null || TextUtils.isEmpty(text)) {
            return;
        }

        clearTranslation(view);
        mInteractor.startTranslation(text, mLanguageFrom, mLanguageTo);
        view.showTranslationProcess();
    }

    public void cancelTranslation() {
        mInteractor.cancelTranslation();
    }

    public void finishTranslation(TranslationView view, final String text, TranslationModel translation) {
        mCurrentTranslation = translation;
        if (translation == null) {
            final WeakReference<Interactor> interactorWeakRef = new WeakReference<>(mInteractor);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Interactor interactor = interactorWeakRef.get();
                    if (interactor != null) {
                        interactor.startTranslation(text, mLanguageFrom, mLanguageTo);
                    }
                }
            }, 3000L);
            return;
        }

        if (translation.code != ApiCode.CODE_OK) {
            clearTranslation(view);
        }

        Logger.d("Finish translation with code: " + translation.code);
        switch (translation.code) {
            case ApiCode.CODE_OK:
                view.showTranslation(translation);

                // Сохраняем полученный перевод в историю
                HistoryItemModel historyItem = new HistoryItemModel(translation);
                historyItem.timestamp = System.currentTimeMillis();
                mHistoryStorage.saveItem(historyItem);

                // FIXME Небольшой хак: рассылаем event для обновления истории
                EventBus.getDefault().post(new HistoryUpdatedEvent());
                break;
            case ApiCode.CODE_API_KEY_INVALID:
                view.showTranslationFail(R.string.toast_translation_fail);
                break;
            case ApiCode.CODE_API_KEY_BLOCKED:
                view.showTranslationFail(R.string.toast_translation_fail);
                break;
            case ApiCode.CODE_LIMIT_EXCEEDED:
                view.showTranslationFail(R.string.toast_translation_fail);
                break;
            case ApiCode.CODE_MAX_TEXT_LIMIT:
                view.showTranslationFail(R.string.toast_too_long_text);
                break;
            case ApiCode.CODE_MAX_URL_LIMIT:
                view.showTranslationFail(R.string.toast_too_long_text);
                break;
            case ApiCode.CODE_UNABLE_TO_TRANSLATE:
                view.showTranslationFail(R.string.toast_translation_fail);
                break;
            case ApiCode.CODE_UNSUPPORTED_TRANSLATE_DIRECTION:
                view.showTranslationFail(R.string.toast_translation_fail);
                break;
            case ApiCode.CODE_SERVER_ERROR:
                view.showTranslationFail(R.string.toast_translation_fail);
                break;
            default:
                view.showTranslationFail(R.string.toast_translation_fail);
                break;
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
