/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.model.HistoryItemModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends ArrayAdapter<HistoryItemModel> {
    private List<HistoryItemModel> mHistory;

    public HistoryAdapter(Context context, List<HistoryItemModel> history) {
        super(context, R.layout.item_history, history);
        mHistory = history;
    }

    static class ViewHolder {
        @BindView(R.id.favorite_image)
        ImageView favoriteImage;
        @BindView(R.id.original_text)
        TextView originalText;
        @BindView(R.id.translation_text)
        TextView translationText;
        @BindView(R.id.langs_pair_text)
        TextView langsPairText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history, parent, false);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HistoryItemModel item = mHistory.get(position);
        holder.originalText.setText(item.originalText);
        holder.translationText.setText(item.translationText);
        holder.langsPairText.setText(buildLangsPairText(item));

        return convertView;
    }

    private static String buildLangsPairText(HistoryItemModel item) {
        return item.langFromCode.toUpperCase() + "-" + item.langToCode.toUpperCase();
    }
}
