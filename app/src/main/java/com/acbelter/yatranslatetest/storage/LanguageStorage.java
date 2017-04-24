/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.util.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Хранилище списка поддерживаемых языков
 */
public class LanguageStorage {
    private static LanguageStorage sInstance;

    private StorageDbHelper mStorageDbHelper;
    // Кеш данных в оперативной памяти
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

    /**
     * Загрузка списка поддерживаемых языков из БД в оперативную память
     */
    public synchronized void load() {
        SQLiteDatabase db = mStorageDbHelper.getReadableDatabase();
        Cursor cursor = cupboard()
                .withDatabase(db)
                .query(LanguageModel.class).getCursor();
        try {
            QueryResultIterable<LanguageModel> iterable =
                    cupboard().withCursor(cursor).iterate(LanguageModel.class);
            List<LanguageModel> languages = iterable.list(true);

            // Сортировка списка языков по их описаниям
            Collections.sort(languages, new Comparator<LanguageModel>() {
                @Override
                public int compare(LanguageModel lang1, LanguageModel lang2) {
                    return lang1.label.compareToIgnoreCase(lang2.label);
                }
            });

            mLanguages = languages;

            Logger.d("Load languages from database: " + languages.size());
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
    }

    /**
     * Сохранение списка поддерживаемых языков в БД
     * @param languages Сохраняемый список поддерживаемых языков
     */
    public synchronized void save(List<LanguageModel> languages) {
        SQLiteDatabase db = mStorageDbHelper.getWritableDatabase();
        cupboard().withDatabase(db).delete(LanguageModel.class, null);
        if (languages != null) {
            cupboard().withDatabase(db).put(languages);
        }
        mLanguages = languages;
        db.close();
    }
}
