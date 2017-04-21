/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TranslationModel implements Parcelable {
    public int code;
    public String detectedLangCode;
    public String langFromCode;
    public String langToCode;
    public String originalText;
    public String translationText;

    public TranslationModel() {
    }

    protected TranslationModel(Parcel in) {
        code = in.readInt();
        detectedLangCode = in.readString();
        langFromCode = in.readString();
        langToCode = in.readString();
        originalText = in.readString();
        translationText = in.readString();
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
        dest.writeString(originalText);
        dest.writeString(translationText);
    }
}
