/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.content.Context;

import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.model.TranslationModel;
import com.acbelter.yatranslatetest.repository.HistoryStorage;
import com.acbelter.yatranslatetest.view.BookmarksView;

import de.greenrobot.event.EventBus;

public class BookmarksPresenter implements Presenter<BookmarksView> {
    private int mPresenterId;

    public BookmarksPresenter() {
    }

    public void showTranslationFromHistory(HistoryItemModel item) {
        TranslationModel translation = new TranslationModel();
        translation.detectedLangCode = item.detectedLangCode;
        translation.langFromCode = item.langFromCode;
        translation.langToCode = item.langToCode;
        translation.originalText = item.originalText;
        translation.translationText = item.translationText;
        // FIXME Small hack: post event to show translation
        EventBus.getDefault().post(new HistoryTranslationEvent(translation));
    }

    public void clearHistory(Context context, BookmarksView view) {
        HistoryStorage.getInstance(context).removeItemsWithFavoriteState(false);
        view.updateHistory();
    }

    public void clearFavorites(Context context, BookmarksView view) {
        HistoryStorage.getInstance(context).setAllItemsFavoriteState(false);
        view.updateHistory();
        view.updateFavorites();
    }

    @Override
    public void present(BookmarksView view) {
        view.updateHistory();
        view.updateFavorites();
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
