/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Класс, описывающий язык перевода
 */
public class LanguageModel implements Parcelable {
    // cupboard требует, чтобы поля были public
    public Long _id;    // поле, необходимое для cupboard
    public String code;
    public String label;

    public LanguageModel() {}

    public LanguageModel(String code, String label) {
        this.code = code;
        this.label = label;
    }

    protected LanguageModel(Parcel in) {
        _id = (Long) in.readValue(Long.class.getClassLoader());
        code = in.readString();
        label = in.readString();
    }

    public static final Creator<LanguageModel> CREATOR = new Creator<LanguageModel>() {
        @Override
        public LanguageModel createFromParcel(Parcel in) {
            return new LanguageModel(in);
        }

        @Override
        public LanguageModel[] newArray(int size) {
            return new LanguageModel[size];
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
        LanguageModel that = (LanguageModel) o;
        return code != null ? code.equals(that.code) : that.code == null;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(_id);
        dest.writeString(code);
        dest.writeString(label);
    }

    @Override
    public String toString() {
        return label + " (" + code + ")";
    }
}
