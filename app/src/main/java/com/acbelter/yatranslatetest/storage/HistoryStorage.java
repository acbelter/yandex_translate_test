/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class HistoryStorage {
    private static HistoryStorage sInstance;

    private StorageDbHelper mStorageDbHelper;
    private List<HistoryItemModel> mHistory;

    private HistoryStorage(Context context) {
        mStorageDbHelper = new StorageDbHelper(context);
    }

    public static HistoryStorage getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HistoryStorage(context.getApplicationContext());
        }
        return sInstance;
    }

    public boolean isHistoryLoaded() {
        return mHistory != null;
    }

    public List<HistoryItemModel> getHistory() {
        List<HistoryItemModel> items = new ArrayList<>();
        for (HistoryItemModel item : mHistory) {
            if (!item.isCleared) {
                items.add(item);
            }
        }
        return items;
    }

    public List<HistoryItemModel> getFavorites() {
        List<HistoryItemModel> items = new ArrayList<>();
        for (HistoryItemModel item : mHistory) {
            if (item.isFavorite) {
                items.add(item);
            }
        }
        return items;
    }

    public boolean hasHistoryItems() {
        if (mHistory == null || mHistory.isEmpty()) {
            return false;
        }

        int count = 0;
        for (int i = 0; i < mHistory.size(); i++) {
            if (!mHistory.get(i).isCleared) {
                count++;
            }
        }
        return count != 0;
    }

    public boolean hasFavoriteItems() {
        if (mHistory == null || mHistory.isEmpty()) {
            return false;
        }

        int count = 0;
        for (int i = 0; i < mHistory.size(); i++) {
            if (mHistory.get(i).isFavorite) {
                count++;
            }
        }
        return count != 0;
    }

    public synchronized void load() {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        Cursor cursor = cupboard()
                .withDatabase(db)
                .query(HistoryItemModel.class).getCursor();
        try {
            QueryResultIterable<HistoryItemModel> iterable =
                    cupboard().withCursor(cursor).iterate(HistoryItemModel.class);
            List<HistoryItemModel> history = iterable.list(true);

            Collections.sort(history, new Comparator<HistoryItemModel>() {
                @Override
                public int compare(HistoryItemModel h1, HistoryItemModel h2) {
                    if (h1.timestamp > h2.timestamp) {
                        return -1;
                    }
                    if (h1.timestamp < h2.timestamp) {
                        return 1;
                    }
                    return 0;
                }
            });

            mHistory = history;

            Logger.d("Load history from database: " + history.size());
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
    }

    public synchronized void saveItem(HistoryItemModel item) {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        item._id = cupboard().withDatabase(db).put(item);
        db.close();
        mHistory.add(0, item);
    }

    public synchronized void removeItem(HistoryItemModel item) {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        cupboard().withDatabase(db).delete(item);
        db.close();
        mHistory.remove(item);
    }

    public synchronized void clearHistory() {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        cupboard().withDatabase(db).delete(HistoryItemModel.class, "is_favorite = ?", "0");

        ContentValues values = new ContentValues(1);
        values.put("ic_cleared", "1");
        // Update only items with the opposite ic_cleared value
        cupboard().withDatabase(db).update(HistoryItemModel.class, values, "ic_cleared = ?", "0");
        db.close();

        Iterator<HistoryItemModel> iterator = mHistory.iterator();
        while (iterator.hasNext()) {
            HistoryItemModel item = iterator.next();
            if (!item.isFavorite) {
                iterator.remove();
            } else {
                item.isCleared = true;
            }
        }
    }

    public synchronized void clearFavorites() {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues(1);
        values.put("is_favorite", "0");
        // Update only items with the opposite is_favorite value
        cupboard().withDatabase(db).update(HistoryItemModel.class, values, "is_favorite = ?", "1");
        cupboard().withDatabase(db).delete(HistoryItemModel.class, "is_favorite = ? and ic_cleared = ?", "0", "0");
        db.close();

        Iterator<HistoryItemModel> iterator = mHistory.iterator();
        while (iterator.hasNext()) {
            HistoryItemModel item = iterator.next();
            if (item.isCleared) {
                iterator.remove();
            } else {
                item.isFavorite = false;
            }
        }
    }

    public synchronized void setItemFavorite(HistoryItemModel item, boolean favorite) {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues(1);
        values.put("is_favorite", favorite ? "1" : "0");
        cupboard().withDatabase(db).update(HistoryItemModel.class, values, "_id = ?", Long.toString(item._id));
        db.close();
        item.isFavorite = favorite;
    }

    public synchronized void save(List<HistoryItemModel> history) {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        cupboard().withDatabase(db).delete(HistoryItemModel.class, null);
        if (history != null) {
            cupboard().withDatabase(db).put(mHistory);
        }
        mHistory = history;
        db.close();
    }
}
