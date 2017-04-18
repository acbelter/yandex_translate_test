/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TranslationModel implements Parcelable {
    public int code;
    public String detectedLangCode;
    public String langFromCode;
    public String langToCode;
    protected List<String> mTranslations;

    public TranslationModel() {
        mTranslations = new ArrayList<>();
    }

    protected TranslationModel(Parcel in) {
        code = in.readInt();
        detectedLangCode = in.readString();
        langFromCode = in.readString();
        langToCode = in.readString();
        mTranslations = new ArrayList<>();
        in.readStringList(mTranslations);
    }

    public List<String> getTranslations() {
        return mTranslations;
    }

    public String buildTranslationText() {
        if (mTranslations.isEmpty()) {
            return "";
        }

        if (mTranslations.size() == 1) {
            return mTranslations.get(0);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mTranslations.size(); i++) {
            builder.append(i + 1).append(") ").append(mTranslations.get(i));
            if (i != mTranslations.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public void addTranslation(String text) {
        mTranslations.add(text);
    }

    public void clearTranslations() {
        mTranslations.clear();
    }

    public static final Creator<TranslationModel> CREATOR = new Creator<TranslationModel>() {
        @Override
        public TranslationModel createFromParcel(Parcel in) {
            return new TranslationModel(in);
        }

        @Override
        public TranslationModel[] newArray(int size) {
            return new TranslationModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(detectedLangCode);
        dest.writeString(langFromCode);
        dest.writeString(langToCode);
        dest.writeStringList(mTranslations);
    }
}
