/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.model.LanguageModel;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Вспомогательный класс для работы с БД
 */
public class StorageDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "yateststorage.db";
    private static final int DATABASE_VERSION = 1;

    public StorageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        cupboard().register(LanguageModel.class);
        cupboard().register(HistoryItemModel.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Существующие столбцы не будут преобразованы при обновлении структуры БД
        cupboard().withDatabase(db).upgradeTables();
    }
}