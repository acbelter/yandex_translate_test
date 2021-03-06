/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.RequestConstants;
import com.acbelter.yatranslatetest.presenter.HistoryTranslationEvent;
import com.acbelter.yatranslatetest.presenter.PresentersHub;
import com.acbelter.yatranslatetest.storage.HistoryStorage;
import com.acbelter.yatranslatetest.storage.LanguageStorage;
import com.acbelter.yatranslatetest.util.Logger;
import com.acbelter.yatranslatetest.util.Utils;
import com.acbelter.yatranslatetest.view.adapter.MainPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.content_view_pager)
    protected CustomViewPager mContentViewPager;
    @BindView(R.id.tabs)
    protected TabLayout mTabs;

    private Drawable mTranslationDrawable;
    private Drawable mTranslationSelectedDrawable;
    private Drawable mBookmarksDrawable;
    private Drawable mBookmarksSelectedDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PresentersHub.attach(getSupportFragmentManager());

        // Скрываем клавиатуру при первом запуске и в дальнейшем отображаем ее поверх UI
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        mTranslationDrawable =
                Utils.getTintDrawable(this, R.drawable.ic_translate, R.color.colorLightGray);
        mTranslationSelectedDrawable =
                Utils.getTintDrawable(this, R.drawable.ic_translate, R.color.colorMainDark);

        mBookmarksDrawable =
                Utils.getTintDrawable(this, R.drawable.ic_bookmark, R.color.colorLightGray);
        mBookmarksSelectedDrawable =
                Utils.getTintDrawable(this, R.drawable.ic_bookmark, R.color.colorMainDark);

        mContentViewPager.setSwipePagingEnabled(false);
        mContentViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mTabs.setupWithViewPager(mContentViewPager);

        mTabs.getTabAt(MainPagerAdapter.INDEX_TRANSLATION).setIcon(mTranslationSelectedDrawable);
        mTabs.getTabAt(MainPagerAdapter.INDEX_BOOKMARKS).setIcon(mBookmarksDrawable);

        // Если история либо список поддерживаемых языков не загружен,
        // то показываем сплеш-скрин и загружаем необходимые данные
        if (!LanguageStorage.getInstance(this).isLanguagesLoaded() ||
                !HistoryStorage.getInstance(this).isHistoryLoaded()) {
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, RequestConstants.REQUEST_CODE_SPLASH);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        mTabs.addOnTabSelectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mTabs.removeOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case MainPagerAdapter.INDEX_TRANSLATION:
                tab.setIcon(mTranslationSelectedDrawable);
                break;
            case MainPagerAdapter.INDEX_BOOKMARKS:
                tab.setIcon(mBookmarksSelectedDrawable);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case MainPagerAdapter.INDEX_TRANSLATION:
                tab.setIcon(mTranslationDrawable);
                break;
            case MainPagerAdapter.INDEX_BOOKMARKS:
                tab.setIcon(mBookmarksDrawable);
                break;
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void onEvent(HistoryTranslationEvent event) {
        Logger.d(getClass(), "History translation event");
        // При событии показы элемента из истории переходим к окну с переводом
        mContentViewPager.setCurrentItem(MainPagerAdapter.INDEX_TRANSLATION);
    }
}
