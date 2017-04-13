/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acbelter.yatranslatetest.R;

public class BookmarksPagerAdapter extends PagerAdapter {
    public static final int INDEX_HISTORY = 0;
    public static final int INDEX_FAVORITE = 1;

    private LayoutInflater mInflater;

    public BookmarksPagerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
            case INDEX_FAVORITE:
                return instantiateFavoritePage(container);
            default:
                return null;
        }
    }

    private View instantiateHistoryPage(ViewGroup container) {
        View view = mInflater.inflate(R.layout.pager_item_history, container, false);
        container.addView(view);
        return view;
    }

    private View instantiateFavoritePage(ViewGroup container) {
        View view = mInflater.inflate(R.layout.pager_item_favorite, container, false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
