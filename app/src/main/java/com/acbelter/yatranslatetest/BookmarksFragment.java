/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarksFragment extends Fragment {
    @BindView(R.id.tabs)
    protected TabLayout mTabs;
    @BindView(R.id.btn_clear)
    protected ImageButton mClearButton;
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @BindView(R.id.content_view_pager)
    protected ViewPager mContentViewPager;

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ButterKnife.bind(this, view);

        mContentViewPager.setAdapter(new BookmarksPagerAdapter(getContext()));
        mTabs.setupWithViewPager(mContentViewPager);

        mTabs.getTabAt(BookmarksPagerAdapter.INDEX_HISTORY).setText(R.string.history);
        mTabs.getTabAt(BookmarksPagerAdapter.INDEX_FAVORITE).setText(R.string.favorite);

        return view;
    }

    public static String tag() {
        return BookmarksFragment.class.getSimpleName();
    }
}
