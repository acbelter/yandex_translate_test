/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acbelter.yatranslatetest.model.LanguageModel;

import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class LanguageStorage {
    private static LanguageStorage sInstance;

    private StorageDbHelper mStorageDbHelper;
    private List<LanguageModel> mLanguages;

    private LanguageStorage(Context context) {
        mStorageDbHelper = new StorageDbHelper(context);
    }

    public static LanguageStorage getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LanguageStorage(context.getApplicationContext());
        }
        return sInstance;
    }

    public boolean isLanguagesLoaded() {
        return mLanguages != null && !mLanguages.isEmpty();
    }

    public List<LanguageModel> getLanguages() {
        return mLanguages;
    }

    public void setLanguages(List<LanguageModel> languages) {
        mLanguages = languages;
    }

    public LanguageModel getLanguageByCode(String code) {
        if (mLanguages == null || code == null) {
            return null;
        }

        for (LanguageModel language : mLanguages) {
            if (language.code.equals(code)) {
                return language;
            }
        }

        return null;
    }

    public synchronized List<LanguageModel> loadFromDatabase() {
        Cursor cursor = cupboard()
                .withDatabase(mStorageDbHelper.getReadableDatabase())
                .query(LanguageModel.class).getCursor();
        try {
            QueryResultIterable<LanguageModel> iterable =
                    cupboard().withCursor(cursor).iterate(LanguageModel.class);
            return iterable.list(true);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public synchronized void saveToDatabase() {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        cupboard().withDatabase(db).delete(LanguageModel.class, null);
        if (mLanguages != null) {
            cupboard().withDatabase(db).put(mLanguages);
        }
    }
}
