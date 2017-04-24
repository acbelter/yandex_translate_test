/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.adapter;

import com.acbelter.yatranslatetest.model.HistoryItemModel;

public interface OnHistoryItemClickListener {
    void onHistoryItemClicked(HistoryItemModel item);
    void onFavoriteItemClicked(HistoryItemModel item);
    void onHistoryItemFavoriteStateChanged(HistoryItemModel item, boolean favorite);
    void onFavoriteItemFavoriteStateChanged(HistoryItemModel item, boolean favorite);
}
