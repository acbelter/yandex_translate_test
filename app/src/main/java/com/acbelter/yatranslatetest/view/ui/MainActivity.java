/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.presenter.PresentersHubHolderFragment;
import com.acbelter.yatranslatetest.repository.HistoryStorage;
import com.acbelter.yatranslatetest.repository.LanguageStorage;
import com.acbelter.yatranslatetest.util.Utils;
import com.acbelter.yatranslatetest.view.adapter.MainPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    public static final int REQUEST_CODE_SPLASH = 0;
    @BindView(R.id.content_view_pager)
    CustomViewPager mContentViewPager;
    @BindView(R.id.tabs)
    TabLayout mTabs;

    private Drawable mTranslationDrawable;
    private Drawable mTranslationSelectedDrawable;
    private Drawable mBookmarksDrawable;
    private Drawable mBookmarksSelectedDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        attachPresentersHub();

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

        if (!LanguageStorage.getInstance(this).isLanguagesLoaded() ||
                !HistoryStorage.getInstance(this).isHistoryLoaded()) {
            Intent startSplashIntent = new Intent(MainActivity.this, SplashActivity.class);
            startActivityForResult(startSplashIntent, REQUEST_CODE_SPLASH);
        }
    }

    private void attachPresentersHub() {
        PresentersHubHolderFragment holderFragment =
                (PresentersHubHolderFragment) getSupportFragmentManager()
                        .findFragmentByTag(PresentersHubHolderFragment.tag());
        if (holderFragment == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(new PresentersHubHolderFragment(), PresentersHubHolderFragment.tag());
            ft.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTabs.addOnTabSelectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}
