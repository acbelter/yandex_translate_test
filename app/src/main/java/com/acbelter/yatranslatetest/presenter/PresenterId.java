/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * Класс, описывающий уникальный идентификатор презентера
 * Идентификатор презентера состоит из двух частей:
 * 1) Идентификатор хаба презентеров. Нужен для того, чтобы определять хаб, который
 * отвечает за этот презентер. Если хаба с таким идентификатором нет, то презентер устарел и его
 * не следует использовать
 * 2) Идентификатор презентера, полученный от хаба презентеров
 */
public class PresenterId implements Parcelable {
    protected UUID mHubId;
    protected int mId;

    public PresenterId(UUID hubId, int id) {
        mHubId = hubId;
        mId = id;
    }

    protected PresenterId(Parcel in) {
        mHubId = UUID.fromString(in.readString());
        mId = in.readInt();
    }

    public UUID getHubId() {
        return mHubId;
    }

    public int getId() {
        return mId;
    }

    public static final Creator<PresenterId> CREATOR = new Creator<PresenterId>() {
        @Override
        public PresenterId createFromParcel(Parcel in) {
            return new PresenterId(in);
        }

        @Override
        public PresenterId[] newArray(int size) {
            return new PresenterId[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mHubId.toString());
        dest.writeInt(mId);
    }
}
