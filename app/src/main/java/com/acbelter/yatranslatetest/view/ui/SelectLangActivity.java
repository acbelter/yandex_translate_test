/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.RequestConstants;
import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.presenter.Presenter;
import com.acbelter.yatranslatetest.presenter.PresentersHub;
import com.acbelter.yatranslatetest.presenter.SelectLangMode;
import com.acbelter.yatranslatetest.presenter.SelectLangPresenter;
import com.acbelter.yatranslatetest.repository.LanguageStorage;
import com.acbelter.yatranslatetest.util.Utils;
import com.acbelter.yatranslatetest.view.SelectLangView;
import com.acbelter.yatranslatetest.view.adapter.LanguagesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLangActivity extends AppCompatActivity implements SelectLangView {
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @BindView(R.id.determine_lang_switch)
    protected SwitchCompat mDetermineLangSwitch;
    @BindView(R.id.determine_lang_container)
    protected LinearLayout mDetermineLangContainer;
    @BindView(R.id.langs_list)
    protected ListView mLangsList;

    private SelectLangMode mSelectLangMode;

    private PresentersHub mPresentersHub = PresentersHub.getInstance();

    private SelectLangPresenter mPresenter;

    private LanguagesAdapter mLanguagesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);
        ButterKnife.bind(this);

        PresentersHub.attach(getSupportFragmentManager());

        mToolbar.setTitle(R.string.text_language);
        Utils.tintToolbarIcons(mToolbar, R.color.colorMainDark);

        Utils.attachToolbar(this, mToolbar, true);

        if (savedInstanceState == null) {
            mSelectLangMode = SelectLangMode.valueOf(getIntent().getStringExtra(RequestConstants.KEY_SELECT_LANG_MODE));
            mPresenter = new SelectLangPresenter(LanguageStorage.getInstance(this), mSelectLangMode);
            mPresentersHub.addPresenter(mPresenter);
        } else {
            int presenterId = savedInstanceState.getInt(Presenter.KEY_PRESENTER_ID);
            mSelectLangMode = SelectLangMode.valueOf(savedInstanceState.getString("select_lang_mode"));
            mPresenter = (SelectLangPresenter) mPresentersHub.getPresenterById(presenterId);
            if (mPresenter == null) {
                mPresenter = new SelectLangPresenter(LanguageStorage.getInstance(this), mSelectLangMode);
                mPresentersHub.addPresenter(mPresenter);
            }
        }

        mDetermineLangSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.setDetermineLanguageState(isChecked);
            }
        });

        mLanguagesAdapter = new LanguagesAdapter(this,
                LanguageStorage.getInstance(this).getLanguages());
        mLangsList.setAdapter(mLanguagesAdapter);
        int selectedPosition = mLanguagesAdapter.selectLanguage(mPresenter.getSelectedLanguage());
        mLangsList.setSelection(selectedPosition);

        mLangsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LanguageModel selectedLanguage = mLanguagesAdapter.selectLangForPosition(position);
                mPresenter.setSelectedLanguage(selectedLanguage);
                mPresenter.finishLanguageSelection(SelectLangActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.present(this);
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
        outState.putString("select_lang_mode", mSelectLangMode.name());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setDetermineLangUiVisible(boolean visible) {
        mDetermineLangContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setDetermineLanguageState(boolean state) {
        mDetermineLangSwitch.setChecked(state);
    }

    @Override
    public void setSelectedLanguage(LanguageModel language) {
        mLanguagesAdapter.selectLangForCode(language != null ? language.code : null);
    }
}
