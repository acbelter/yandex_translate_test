/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LanguageModel implements Parcelable {
    public Long _id;        // for cupboard
    public String code;     // public for cupboard
    public String label;    // public for cupboard

    public LanguageModel() {}

    public LanguageModel(String code, String label) {
        this.code = code;
        this.label = label;
    }

    protected LanguageModel(Parcel in) {
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
        dest.writeString(code);
        dest.writeString(label);
    }

    @Override
    public String toString() {
        return label + " (" + code + ")";
    }
}
