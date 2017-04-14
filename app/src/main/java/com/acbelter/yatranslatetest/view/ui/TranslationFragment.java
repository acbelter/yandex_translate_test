/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.interactor.ChronosInteractor;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.presenter.Presenter;
import com.acbelter.yatranslatetest.presenter.PresentersHub;
import com.acbelter.yatranslatetest.presenter.TranslationPresenter;
import com.acbelter.yatranslatetest.repository.LanguageStorage;
import com.acbelter.yatranslatetest.view.TranslationView;
import com.redmadrobot.chronos.gui.fragment.ChronosSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TranslationFragment extends ChronosSupportFragment implements TranslationView {
    @BindView(R.id.lang_from_text)
    protected TextView mLangFromText;
    @BindView(R.id.btn_swap_langs)
    protected ImageButton mBtnSwapLangs;
    @BindView(R.id.lang_to_text)
    protected TextView mLangToText;
    @BindView(R.id.original_edit_text)
    protected EditText mOriginalEditText;
    @BindView(R.id.btn_clear)
    protected ImageButton mBtnClear;
    @BindView(R.id.btn_add_favorite)
    protected ImageButton mBtnAddFavorite;
    @BindView(R.id.main_translation_text)
    protected TextView mMainTranslationText;
    @BindView(R.id.translation_variants_list)
    protected RecyclerView mTranslationVariantsList;
    private Unbinder mUnbinder;

    private PresentersHub mPresentersHub = PresentersHub.getInstance();

    private TranslationPresenter mPresenter;
    // Rotate swap button clockwise or not
    private boolean mSwapRotateClockwise = true;

    public static TranslationFragment newInstance() {
        return new TranslationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageStorage languageStorage = LanguageStorage.getInstance(getContext());
        if (savedInstanceState == null) {
            mPresenter = new TranslationPresenter(languageStorage);
            mPresentersHub.addPresenter(mPresenter);
        } else {
            int presenterId = savedInstanceState.getInt(Presenter.KEY_PRESENTER_ID);
            mPresenter = (TranslationPresenter) mPresentersHub.getPresenterById(presenterId);
            if (mPresenter == null) {
                mPresenter = new TranslationPresenter(languageStorage);
                mPresentersHub.addPresenter(mPresenter);
            }

            mSwapRotateClockwise = savedInstanceState.getBoolean("swap_rotate_clockwise");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setInteractor(new ChronosInteractor(this));
        mPresenter.initLanguages(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.setInteractor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Presenter.KEY_PRESENTER_ID, mPresenter.getId());
        outState.putBoolean("swap_rotate_clockwise", mSwapRotateClockwise);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setLanguageFrom(LanguageModel language) {
        mLangFromText.setText(language != null ? language.description : null);
    }

    @Override
    public void setLanguageTo(LanguageModel language) {
        mLangToText.setText(language != null ? language.description : null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.lang_from_text)
    public void onLangFromClicked(View view) {

    }

    @OnClick(R.id.lang_to_text)
    public void onLangToClicked(View view) {

    }

    @OnClick(R.id.btn_swap_langs)
    public void onSwapLangsClicked(View view) {
        int degrees = mSwapRotateClockwise ? 180 : -180;
        RotateAnimation rotate = new RotateAnimation(0, degrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(180);
        rotate.setInterpolator(new LinearInterpolator());

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSwapRotateClockwise = !mSwapRotateClockwise;
                mPresenter.swapLanguages(TranslationFragment.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnSwapLangs.startAnimation(rotate);
    }

    @OnClick(R.id.btn_clear)
    public void onClearClicked(View view) {

    }

    @OnClick(R.id.btn_add_favorite)
    public void onAddFavoriteClicked(View view) {

    }

    public static String tag() {
        return TranslationFragment.class.getSimpleName();
    }
}
