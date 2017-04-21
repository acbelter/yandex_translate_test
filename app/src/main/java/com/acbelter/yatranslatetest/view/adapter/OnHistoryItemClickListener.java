/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.adapter;

import com.acbelter.yatranslatetest.model.HistoryItemModel;

public interface OnHistoryItemClickListener {
    void onHistoryItemClicked(HistoryItemModel item);
    void onFavoriteItemClicked(HistoryItemModel item);
    void onHistoryItemDeleted(HistoryItemModel item);
    void onFavoriteItemDeleted(HistoryItemModel item);
}
