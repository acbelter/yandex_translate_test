/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.presenter.BookmarksPresenter;
import com.acbelter.yatranslatetest.presenter.HistoryUpdatedEvent;
import com.acbelter.yatranslatetest.presenter.Presenter;
import com.acbelter.yatranslatetest.presenter.PresenterId;
import com.acbelter.yatranslatetest.presenter.PresentersHub;
import com.acbelter.yatranslatetest.view.BookmarksView;
import com.acbelter.yatranslatetest.view.adapter.BookmarksPagerAdapter;
import com.acbelter.yatranslatetest.view.adapter.OnHistoryItemClickListener;
import com.redmadrobot.chronos.gui.fragment.ChronosSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

public class BookmarksFragment extends ChronosSupportFragment implements
        BookmarksView, OnHistoryItemClickListener {
    @BindView(R.id.tabs)
    protected TabLayout mTabs;
    @BindView(R.id.btn_clear)
    protected ImageButton mClearButton;
    @BindView(R.id.content_view_pager)
    protected ViewPager mContentViewPager;
    private Unbinder mUnbinder;

    private BookmarksPagerAdapter mBookmarksPagerAdapter;

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
            PresenterId id = savedInstanceState.getParcelable(Presenter.KEY_PRESENTER_ID);
            mPresenter = (BookmarksPresenter) mPresentersHub.getPresenterById(id);
            if (mPresenter == null) {
                mPresenter = new BookmarksPresenter();
                mPresentersHub.addPresenter(mPresenter);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Presenter.KEY_PRESENTER_ID,
                mPresentersHub.getIdForPresenter(mPresenter));
    }

    @Override
    public void updateHistory() {
        mBookmarksPagerAdapter.updateHistory(getContext());
    }

    @Override
    public void updateFavorites() {
        mBookmarksPagerAdapter.updateFavorites(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mBookmarksPagerAdapter = new BookmarksPagerAdapter(getContext());
        mContentViewPager.setAdapter(mBookmarksPagerAdapter);
        mTabs.setupWithViewPager(mContentViewPager);

        mTabs.getTabAt(BookmarksPagerAdapter.INDEX_HISTORY).setText(R.string.history);
        mTabs.getTabAt(BookmarksPagerAdapter.INDEX_FAVORITES).setText(R.string.favorites);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBookmarksPagerAdapter.setItemClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBookmarksPagerAdapter.setItemClickListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void onEvent(HistoryUpdatedEvent event) {
        updateHistory();
        updateFavorites();
    }

    @OnClick(R.id.btn_clear)
    public void onClearClicked() {
        final int selectedTabIndex = mTabs.getSelectedTabPosition();
        switch (selectedTabIndex) {
            case BookmarksPagerAdapter.INDEX_HISTORY:
                if (!mPresenter.hasNotFavoriteItems(getContext())) {
                    return;
                }
                break;
            case BookmarksPagerAdapter.INDEX_FAVORITES:
                if (!mPresenter.hasFavoriteItems(getContext())) {
                    return;
                }
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.confirmation);
        builder.setCancelable(true);

        switch (selectedTabIndex) {
            case BookmarksPagerAdapter.INDEX_HISTORY:
                builder.setMessage(R.string.clear_history_text);
                break;
            case BookmarksPagerAdapter.INDEX_FAVORITES:
                builder.setMessage(R.string.clear_favorites_text);
                break;
        }

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                switch (selectedTabIndex) {
                    case BookmarksPagerAdapter.INDEX_HISTORY:
                        mPresenter.clearHistory(getContext(), BookmarksFragment.this);
                        break;
                    case BookmarksPagerAdapter.INDEX_FAVORITES:
                        mPresenter.clearFavorites(getContext(), BookmarksFragment.this);
                        break;
                }
                dialog.cancel();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        int buttonsColor = ContextCompat.getColor(getContext(), R.color.colorMainDark);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(buttonsColor);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(buttonsColor);
    }

    @Override
    public void onHistoryItemClicked(HistoryItemModel item) {
        mPresenter.showTranslationFromHistory(item);
    }

    @Override
    public void onFavoriteItemClicked(HistoryItemModel item) {
        mPresenter.showTranslationFromHistory(item);
    }

    @Override
    public void onHistoryItemFavoriteStateChanged(HistoryItemModel item, boolean favorite) {
        mPresenter.setHistoryItemFavoriteState(getContext(), this, item, favorite);
    }

    @Override
    public void onFavoriteItemFavoriteStateChanged(HistoryItemModel item, boolean favorite) {
        mPresenter.setFavoriteItemFavoriteState(getContext(), this, item, favorite);
    }

    public static String tag() {
        return BookmarksFragment.class.getSimpleName();
    }
}
