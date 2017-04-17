/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import com.acbelter.yatranslatetest.interactor.Interactor;
import com.acbelter.yatranslatetest.view.BookmarksView;

public class BookmarksPresenter implements Presenter<BookmarksView> {
    private int mPresenterId;
    private Interactor mInteractor;

    public BookmarksPresenter() {
    }

    public void setInteractor(Interactor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void present(BookmarksView view) {

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
