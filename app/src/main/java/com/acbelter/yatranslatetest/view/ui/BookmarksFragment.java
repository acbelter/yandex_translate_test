/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.interactor.ChronosInteractor;
import com.acbelter.yatranslatetest.presenter.BookmarksPresenter;
import com.acbelter.yatranslatetest.presenter.Presenter;
import com.acbelter.yatranslatetest.presenter.PresentersHub;
import com.acbelter.yatranslatetest.view.BookmarksView;
import com.acbelter.yatranslatetest.view.adapter.BookmarksPagerAdapter;
import com.redmadrobot.chronos.gui.fragment.ChronosSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarksFragment extends ChronosSupportFragment implements BookmarksView {
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.btn_clear)
    ImageButton mClearButton;
    @BindView(R.id.content_view_pager)
    ViewPager mContentViewPager;

    private PresentersHub mPresentersHub = PresentersHub.getInstance();

    private BookmarksPresenter mPresenter;

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mPresenter = new BookmarksPresenter();
            mPresentersHub.addPresenter(mPresenter);
        } else {
            int presenterId = savedInstanceState.getInt(Presenter.KEY_PRESENTER_ID);
            mPresenter = (BookmarksPresenter) mPresentersHub.getPresenterById(presenterId);
            if (mPresenter == null) {
                mPresenter = new BookmarksPresenter();
                mPresentersHub.addPresenter(mPresenter);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setInteractor(new ChronosInteractor(this));
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
