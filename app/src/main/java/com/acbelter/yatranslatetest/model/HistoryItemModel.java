/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.model;

import android.os.Parcel;
import android.os.Parcelable;

import nl.qbusict.cupboard.annotation.Column;

/**
 * Класс, описывающий элемент истории и избранного
 */
public class HistoryItemModel implements Parcelable {
    // cupboard требует, чтобы поля были public
    public Long _id;    // поле, необходимое для cupboard
    @Column("detected_lang_code")
    public String detectedLangCode;
    @Column("lang_from_code")
    public String langFromCode;
    @Column("lang_to_code")
    public String langToCode;
    @Column("original_text")
    public String originalText;
    @Column("translation_text")
    public String translationText;
    @Column("is_favorite")
    // Флаг, показывающий, находится ли элемент в избранном или нет
    public boolean isFavorite;
    @Column("ic_cleared")
    // Флаг, показывающий, был ли элемент из истории очищен.
    // Данный флаг необходим, т.к. в избранном могут находиться элементы,
    // удаленные из истории
    public boolean isCleared;
    public long timestamp;

    public HistoryItemModel() {}

    public HistoryItemModel(TranslationModel translation) {
        detectedLangCode = translation.detectedLangCode;
        langFromCode = translation.langFromCode;
        langToCode = translation.langToCode;
        originalText = translation.originalText;
        translationText = translation.translationText;
        isFavorite = false;
        isCleared = false;
        timestamp = 0L;
    }

    protected HistoryItemModel(Parcel in) {
        _id = (Long) in.readValue(Long.class.getClassLoader());
        detectedLangCode = in.readString();
        langFromCode = in.readString();
        langToCode = in.readString();
        originalText = in.readString();
        translationText = in.readString();
        isFavorite = in.readByte() != 0;
        isCleared = in.readByte() != 0;
        timestamp = in.readLong();
    }

    public static final Creator<HistoryItemModel> CREATOR = new Creator<HistoryItemModel>() {
        @Override
        public HistoryItemModel createFromParcel(Parcel in) {
            return new HistoryItemModel(in);
        }

        @Override
        public HistoryItemModel[] newArray(int size) {
            return new HistoryItemModel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HistoryItemModel that = (HistoryItemModel) o;
        return _id != null ? _id.equals(that._id) : that._id == null;
    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(_id);
        dest.writeString(detectedLangCode);
        dest.writeString(langFromCode);
        dest.writeString(langToCode);
        dest.writeString(originalText);
        dest.writeString(translationText);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (isCleared ? 1 : 0));
        dest.writeLong(timestamp);
    }
}
