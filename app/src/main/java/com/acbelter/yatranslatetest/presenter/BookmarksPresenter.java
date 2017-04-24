/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.content.Context;

import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.model.TranslationModel;
import com.acbelter.yatranslatetest.storage.HistoryStorage;
import com.acbelter.yatranslatetest.view.BookmarksView;

import de.greenrobot.event.EventBus;

public class BookmarksPresenter implements Presenter<BookmarksView> {
    private int mPresenterId;

    public BookmarksPresenter() {
    }

    public void showTranslationFromHistory(HistoryItemModel item) {
        TranslationModel translation = new TranslationModel(item);
        // FIXME Small hack: post event to show translation
        EventBus.getDefault().post(new HistoryTranslationEvent(translation));
    }

    public boolean hasHistoryItems(Context context) {
        return HistoryStorage.getInstance(context).hasHistoryItems();
    }

    public boolean hasFavoriteItems(Context context) {
        return HistoryStorage.getInstance(context).hasFavoriteItems();
    }

    public void clearHistory(Context context, BookmarksView view) {
        HistoryStorage.getInstance(context).clearHistory();
        view.updateHistory();
        view.updateFavorites();
    }

    public void clearFavorites(Context context, BookmarksView view) {
        HistoryStorage.getInstance(context).clearFavorites();
        view.updateHistory();
        view.updateFavorites();
    }

    public void setHistoryItemFavoriteState(Context context,
                                            BookmarksView view,
                                            HistoryItemModel item,
                                            boolean favorite) {
        HistoryStorage.getInstance(context).setItemFavorite(item, favorite);
        view.updateHistory();
        view.updateFavorites();
    }

    public void setFavoriteItemFavoriteState(Context context,
                                             BookmarksView view,
                                             HistoryItemModel item,
                                             boolean favorite) {
        HistoryStorage.getInstance(context).setItemFavorite(item, favorite);
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
