/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.os.Bundle;

import com.acbelter.yatranslatetest.interactor.ChronosInteractor;
import com.acbelter.yatranslatetest.operation.InitDataOperation;
import com.acbelter.yatranslatetest.presenter.Presenter;
import com.acbelter.yatranslatetest.presenter.PresenterId;
import com.acbelter.yatranslatetest.presenter.PresentersHub;
import com.acbelter.yatranslatetest.presenter.SplashPresenter;
import com.acbelter.yatranslatetest.view.SplashView;
import com.redmadrobot.chronos.gui.activity.ChronosAppCompatActivity;

public class SplashActivity extends ChronosAppCompatActivity implements SplashView {
    private PresentersHub mPresentersHub = PresentersHub.getInstance();

    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PresentersHub.attach(getSupportFragmentManager());

        if (savedInstanceState == null) {
            mPresenter = new SplashPresenter();
            mPresentersHub.addPresenter(mPresenter);
        } else {
            // Получение презентера по сохраненному id из хаба презентеров
            PresenterId id = savedInstanceState.getParcelable(Presenter.KEY_PRESENTER_ID);
            mPresenter = (SplashPresenter) mPresentersHub.getPresenterById(id);
            // Если в хабе почему-то нет презентера с таким id, то создадим новый презентер
            if (mPresenter == null) {
                mPresenter = new SplashPresenter();
                mPresentersHub.addPresenter(mPresenter);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setInteractor(new ChronosInteractor(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.startDataInitialization(this, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.setInteractor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Удаляем презентер из хаба, т.к. он больше не требуется
        mPresentersHub.removePresenterById(mPresenter.getId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Presenter.KEY_PRESENTER_ID,
                mPresentersHub.getIdForPresenter(mPresenter));
    }

    /**
     * Callback-метод библиотеки Chronos, вызываемый при завершении соответствующей операции
     * @param result Результат операции
     */
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
        // Переопределение анимации activity
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
