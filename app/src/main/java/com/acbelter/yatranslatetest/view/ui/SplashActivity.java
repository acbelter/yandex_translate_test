/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.os.Bundle;

import com.acbelter.yatranslatetest.interactor.ChronosInteractor;
import com.acbelter.yatranslatetest.interactor.Interactor;
import com.acbelter.yatranslatetest.operation.InitDataOperation;
import com.acbelter.yatranslatetest.presenter.Presenter;
import com.acbelter.yatranslatetest.presenter.PresentersHub;
import com.acbelter.yatranslatetest.presenter.SplashPresenter;
import com.acbelter.yatranslatetest.view.SplashView;
import com.redmadrobot.chronos.gui.activity.ChronosAppCompatActivity;

public class SplashActivity extends ChronosAppCompatActivity implements SplashView {
    private PresentersHub mPresentersHub = PresentersHub.getInstance();

    private SplashPresenter mPresenter;
    private Interactor mInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PresentersHub.attach(getSupportFragmentManager());

        if (savedInstanceState == null) {
            mPresenter = new SplashPresenter();
            mPresentersHub.addPresenter(mPresenter);
        } else {
            int presenterId = savedInstanceState.getInt(Presenter.KEY_PRESENTER_ID);
            mPresenter = (SplashPresenter) mPresentersHub.getPresenterById(presenterId);
            if (mPresenter == null) {
                mPresenter = new SplashPresenter();
                mPresentersHub.addPresenter(mPresenter);
            }
        }

        mInteractor = new ChronosInteractor(this);
        mPresenter.setInteractor(mInteractor);

        mPresenter.startDataInitialization(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setInteractor(mInteractor);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.setInteractor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresentersHub.removePresenterById(mPresenter.getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Presenter.KEY_PRESENTER_ID, mPresenter.getId());
    }

    public void onOperationFinished(InitDataOperation.Result result) {
        mPresenter.finishDataInitialization(this, this, result.getOutput());
    }

    @Override
    public void hide() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        // Disable animation
        overridePendingTransition(0, 0);
    }
}
