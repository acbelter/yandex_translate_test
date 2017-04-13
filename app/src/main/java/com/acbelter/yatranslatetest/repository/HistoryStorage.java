/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acbelter.yatranslatetest.model.HistoryItemModel;

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
        return mHistory;
    }

    public void setHistory(List<HistoryItemModel> history) {
        mHistory = history;
    }

    public synchronized List<HistoryItemModel> loadFromDatabase() {
        Cursor cursor = cupboard()
                .withDatabase(mStorageDbHelper.getReadableDatabase())
                .query(HistoryItemModel.class).getCursor();
        try {
            QueryResultIterable<HistoryItemModel> iterable =
                    cupboard().withCursor(cursor).iterate(HistoryItemModel.class);
            return iterable.list(true);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public synchronized void saveToDatabase() {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        cupboard().withDatabase(db).delete(HistoryItemModel.class, null);
        if (mHistory != null) {
            cupboard().withDatabase(db).put(mHistory);
        }
    }
}
