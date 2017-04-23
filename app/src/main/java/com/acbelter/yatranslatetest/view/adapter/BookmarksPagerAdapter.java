/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.model.HistoryItemModel;
import com.acbelter.yatranslatetest.storage.HistoryStorage;

public class BookmarksPagerAdapter extends PagerAdapter implements HistoryAdapter.OnFavoriteClickListener {
    public static final int INDEX_HISTORY = 0;
    public static final int INDEX_FAVORITES = 1;

    private ListView mHistoryList;
    private ListView mFavoritesList;

    private View mHistoryEmptyStub;
    private View mFavoritesEmptyStub;

    private HistoryAdapter mHistoryAdapter;
    private HistoryAdapter mFavoritesAdapter;

    private HistoryStorage mHistoryStorage;

    private OnHistoryItemClickListener mItemClickListener;

    public BookmarksPagerAdapter(Context context) {
        mHistoryStorage = HistoryStorage.getInstance(context);
    }

    public void setItemClickListener(OnHistoryItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        switch (position) {
            case INDEX_HISTORY:
                return instantiateHistoryPage(container);
            case INDEX_FAVORITES:
                return instantiateFavoritesPage(container);
            default:
                return null;
        }
    }

    private View instantiateHistoryPage(ViewGroup container) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.pager_item_bookmarks, container, false);
        ((ImageView) view.findViewById(R.id.empty_stub_image))
                .setImageResource(R.drawable.ic_watch_later_big);
        ((TextView) view.findViewById(R.id.empty_stub_text))
                .setText(R.string.empty_history_text);

        mHistoryList = (ListView) view.findViewById(R.id.items_list);
        mHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mItemClickListener != null) {
                    mItemClickListener.onHistoryItemClicked(mHistoryAdapter.getItem(position));
                }
            }
        });
        mHistoryEmptyStub = view.findViewById(R.id.empty_stub);

        updateHistory(container.getContext());

        container.addView(view);
        return view;
    }

    public void updateHistory(Context context) {
        mHistoryAdapter = new HistoryAdapter(context,
                mHistoryStorage.getHistory(false), this);
        mHistoryList.setAdapter(mHistoryAdapter);

        if (!mHistoryAdapter.isEmpty()) {
            mHistoryEmptyStub.setVisibility(View.GONE);
        } else {
            mHistoryEmptyStub.setVisibility(View.VISIBLE);
        }
    }

    private View instantiateFavoritesPage(ViewGroup container) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.pager_item_bookmarks, container, false);
        ((ImageView) view.findViewById(R.id.empty_stub_image))
                .setImageResource(R.drawable.ic_bookmark_big);
        ((TextView) view.findViewById(R.id.empty_stub_text))
                .setText(R.string.empty_favorites_text);

        mFavoritesList = (ListView) view.findViewById(R.id.items_list);
        mFavoritesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mItemClickListener != null) {
                    mItemClickListener.onFavoriteItemClicked(mFavoritesAdapter.getItem(position));
                }
            }
        });
        mFavoritesEmptyStub = view.findViewById(R.id.empty_stub);

        updateFavorites(container.getContext());

        container.addView(view);
        return view;
    }

    public void updateFavorites(Context context) {
        mFavoritesAdapter = new HistoryAdapter(context,
                mHistoryStorage.getHistory(true), this);
        mFavoritesList.setAdapter(mFavoritesAdapter);

        if (!mFavoritesAdapter.isEmpty()) {
            mFavoritesEmptyStub.setVisibility(View.GONE);
        } else {
            mFavoritesEmptyStub.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onFavoriteClicked(HistoryAdapter adapter, HistoryItemModel item, boolean favorite) {
        if (mItemClickListener == null) {
            return;
        }

        if (adapter == mHistoryAdapter) {
            mItemClickListener.onHistoryItemFavoriteStateChanged(item, favorite);
        } else if (adapter == mFavoritesAdapter) {
            mItemClickListener.onFavoriteItemFavoriteStateChanged(item, favorite);
        }
    }
}
