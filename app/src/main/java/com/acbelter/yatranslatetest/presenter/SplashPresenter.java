/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.content.Context;

import com.acbelter.yatranslatetest.interactor.Interactor;
import com.acbelter.yatranslatetest.storage.HistoryStorage;
import com.acbelter.yatranslatetest.storage.LanguageStorage;
import com.acbelter.yatranslatetest.util.Logger;
import com.acbelter.yatranslatetest.view.SplashView;

public class SplashPresenter implements Presenter<SplashView> {
    private int mPresenterId;
    private Interactor mInteractor;

    public SplashPresenter() {
    }

    public void startDataInitialization(Context context, SplashView splashView) {
        Logger.d("Start data initialization");
        if (!LanguageStorage.getInstance(context).isLanguagesLoaded() ||
                !HistoryStorage.getInstance(context).isHistoryLoaded()) {
            mInteractor.startInitData(context);
        } else {
            splashView.hide();
        }
    }

    public void finishDataInitialization(Context context,
                                         SplashView splashView,
                                         boolean result) {
        Logger.d("Finish data initialization with result: " + result);
        if (result) {
            splashView.hide();
        } else {
            startDataInitialization(context, splashView);
        }
    }

    public void setInteractor(Interactor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void present(SplashView view) {

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
