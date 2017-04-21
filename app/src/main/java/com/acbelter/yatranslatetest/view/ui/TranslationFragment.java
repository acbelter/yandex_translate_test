/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.RequestConstants;
import com.acbelter.yatranslatetest.interactor.ChronosInteractor;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.model.TranslationModel;
import com.acbelter.yatranslatetest.operation.TranslateOperation;
import com.acbelter.yatranslatetest.presenter.HistoryTranslationEvent;
import com.acbelter.yatranslatetest.presenter.Presenter;
import com.acbelter.yatranslatetest.presenter.PresenterId;
import com.acbelter.yatranslatetest.presenter.PresentersHub;
import com.acbelter.yatranslatetest.presenter.TranslationPresenter;
import com.acbelter.yatranslatetest.repository.HistoryStorage;
import com.acbelter.yatranslatetest.repository.LanguageStorage;
import com.acbelter.yatranslatetest.util.Logger;
import com.acbelter.yatranslatetest.util.Utils;
import com.acbelter.yatranslatetest.view.TranslationView;
import com.redmadrobot.chronos.gui.fragment.ChronosSupportFragment;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

public class TranslationFragment extends ChronosSupportFragment implements TranslationView {
    @BindView(R.id.lang_from_label)
    protected TextView mLangFromText;
    @BindView(R.id.btn_swap_langs)
    protected ImageButton mBtnSwapLangs;
    @BindView(R.id.lang_to_label)
    protected TextView mLangToText;
    @BindView(R.id.original_edit_text)
    protected EditText mOriginalEditText;
    @BindView(R.id.btn_clear)
    protected ImageButton mBtnClear;
    @BindView(R.id.translation_text)
    protected TextView mTranslationText;
    @BindView(R.id.detected_language_text)
    protected TextView mDetectedLanguageText;
    @BindView(R.id.translation_progress)
    protected ProgressBar mTranslationProgress;
    private Unbinder mUnbinder;

    private TextWatcher mOriginalTextWatcher;

    private PresentersHub mPresentersHub = PresentersHub.getInstance();

    private TranslationPresenter mPresenter;
    // Rotate swap button clockwise or not
    private boolean mSwapRotateClockwise = true;

    private Handler mUiHandler;

    public static TranslationFragment newInstance() {
        return new TranslationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUiHandler = new Handler(Looper.getMainLooper());

        LanguageStorage languageStorage = LanguageStorage.getInstance(getContext());
        HistoryStorage historyStorage = HistoryStorage.getInstance(getContext());
        if (savedInstanceState == null) {
            mPresenter = new TranslationPresenter(languageStorage, historyStorage);
            mPresentersHub.addPresenter(mPresenter);
        } else {
            PresenterId id = savedInstanceState.getParcelable(Presenter.KEY_PRESENTER_ID);
            mPresenter = (TranslationPresenter) mPresentersHub.getPresenterById(id);
            if (mPresenter == null) {
                mPresenter = new TranslationPresenter(languageStorage, historyStorage);
                mPresentersHub.addPresenter(mPresenter);
            }

            mSwapRotateClockwise = savedInstanceState.getBoolean("swap_rotate_clockwise");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setInteractor(new ChronosInteractor(this));
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.setInteractor(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        mPresenter.present(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Presenter.KEY_PRESENTER_ID,
                mPresentersHub.getIdForPresenter(mPresenter));
        outState.putBoolean("swap_rotate_clockwise", mSwapRotateClockwise);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Utils.tintIndeterminateProgressBar(mTranslationProgress);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Previous versions are not support ripple effect
            mLangFromText.setBackgroundResource(0);
            mLangToText.setBackgroundResource(0);
            mBtnSwapLangs.setBackgroundResource(0);
        }

        mOriginalTextWatcher = new TextWatcher() {
            private final long DELAY = 500L;
            private Timer mTimer = new Timer();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                mTimer.cancel();
                mPresenter.clearTranslation(TranslationFragment.this);
                final String text = s.toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    mBtnClear.setVisibility(View.VISIBLE);
                    mTimer = new Timer();
                    mTimer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    mUiHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPresenter.startTranslation(TranslationFragment.this, text);
                                        }
                                    });
                                }
                            }, DELAY);
                } else {
                    mBtnClear.setVisibility(View.INVISIBLE);
                }
            }
        };

        mOriginalEditText.addTextChangedListener(mOriginalTextWatcher);
        return view;
    }

    @Override
    public String getOriginalText() {
        return mOriginalEditText.getText().toString();
    }

    @Override
    public void setLanguageFrom(LanguageModel language) {
        if (language != null) {
            mLangFromText.setText(language.label);
        } else {
            mLangFromText.setText(R.string.select_language);
        }
    }

    @Override
    public void setLanguageTo(LanguageModel language) {
        if (language != null) {
            mLangToText.setText(language.label);
        } else {
            mLangToText.setText(R.string.select_language);
        }
    }

    @Override
    public void showTranslationProcess() {
        mTranslationText.setText(null);
        mDetectedLanguageText.setText(null);
        mDetectedLanguageText.setVisibility(View.GONE);
        mTranslationProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTranslation(TranslationModel translation) {
        mTranslationProgress.setVisibility(View.INVISIBLE);
        if (translation != null) {
            mOriginalEditText.removeTextChangedListener(mOriginalTextWatcher);
            mOriginalEditText.setText(translation.originalText);
            mOriginalEditText.addTextChangedListener(mOriginalTextWatcher);

            mTranslationText.setText(translation.translationText);
            LanguageModel detectedLang = LanguageStorage.getInstance(getContext())
                    .getLanguageByCode(translation.detectedLangCode);
            if (detectedLang != null) {
                mDetectedLanguageText.setText(
                        getString(R.string.detected_language, detectedLang.label));
                mDetectedLanguageText.setVisibility(View.VISIBLE);
            } else {
                mDetectedLanguageText.setText(null);
                mDetectedLanguageText.setVisibility(View.GONE);
            }
        } else {
            mTranslationText.setText(null);
            mDetectedLanguageText.setText(null);
            mDetectedLanguageText.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTranslationFail() {
        mTranslationText.setText(null);
        mDetectedLanguageText.setText(null);
        mDetectedLanguageText.setVisibility(View.GONE);
        Toast.makeText(getContext().getApplicationContext(),
                R.string.toast_translation_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.lang_from_label)
    public void onLangFromClicked(View view) {
        mPresenter.startSelectLanguageFrom(this);
    }

    @OnClick(R.id.lang_to_label)
    public void onLangToClicked(View view) {
        mPresenter.startSelectLanguageTo(this);
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
                mPresenter.startTranslation(TranslationFragment.this, getOriginalText());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnSwapLangs.startAnimation(rotate);
    }

    @OnClick(R.id.btn_clear)
    public void onClearClicked(View view) {
        mPresenter.cancelTranslation();
        mOriginalEditText.setText(null);
        mBtnClear.setVisibility(View.INVISIBLE);
    }

    public void onOperationFinished(TranslateOperation.Result result) {
        Logger.d("Translation operation is finished");
        TranslationModel translation = !result.getOperation().isCancelled() ? result.getOutput() : null;
        mPresenter.finishTranslation(this, translation);
    }

    public void onEvent(HistoryTranslationEvent event) {
        Logger.d(getClass(), "History translation event");
        showTranslation(event.getTranslation());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.setInteractor(new ChronosInteractor(this));
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestConstants.REQUEST_CODE_SELECT_LANG_FROM) {
                LanguageModel language = data.getParcelableExtra(RequestConstants.KEY_LANG);
                mPresenter.setLanguageFrom(this, language);
                mPresenter.startTranslation(this, getOriginalText());
            } else if (requestCode == RequestConstants.REQUEST_CODE_SELECT_LANG_TO) {
                LanguageModel language = data.getParcelableExtra(RequestConstants.KEY_LANG);
                mPresenter.setLanguageTo(this, language);
                mPresenter.startTranslation(this, getOriginalText());
            }
        }
    }

    public static String tag() {
        return TranslationFragment.class.getSimpleName();
    }
}
