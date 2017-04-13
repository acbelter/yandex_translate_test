/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import com.acbelter.yatranslatetest.interactor.Interactor;

public class BookmarksPresenter implements Presenter {
    private int mPresenterId;
    private Interactor mInteractor;

    public BookmarksPresenter() {
    }

    public void setInteractor(Interactor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void setId(int id) {
        mPresenterId = id;
    }

    @Override
    public int getId() {
        return mPresenterId;
    }
}
