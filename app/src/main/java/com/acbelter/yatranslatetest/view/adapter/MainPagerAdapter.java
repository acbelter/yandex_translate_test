/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.acbelter.yatranslatetest.view.ui.BookmarksFragment;
import com.acbelter.yatranslatetest.view.ui.TranslationFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public static final int INDEX_TRANSLATION = 0;
    public static final int INDEX_BOOKMARKS = 1;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case INDEX_TRANSLATION:
                return TranslationFragment.newInstance();
            case INDEX_BOOKMARKS:
                return BookmarksFragment.newInstance();
            default:
                return null;
        }
    }
}
