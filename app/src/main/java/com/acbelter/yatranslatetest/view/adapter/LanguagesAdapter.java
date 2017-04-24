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
import com.acbelter.yatranslatetest.model.LanguageModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguagesAdapter extends ArrayAdapter<LanguageModel> {
    private List<LanguageModel> mLanguages;
    private LanguageModel mSelectedLanguage;

    public LanguagesAdapter(Context context, List<LanguageModel> languages) {
        super(context, R.layout.item_lang, languages);
        mLanguages = languages;
    }

    static class ViewHolder {
        @BindView(R.id.language_label)
        TextView languageLabel;
        @BindView(R.id.selection_mark)
        ImageView selectionMark;

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
                    .inflate(R.layout.item_lang, parent, false);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LanguageModel item = mLanguages.get(position);
        holder.languageLabel.setText(item.label);

        if (item.equals(mSelectedLanguage)) {
            holder.selectionMark.setVisibility(View.VISIBLE);
        } else {
            holder.selectionMark.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public int selectLanguage(LanguageModel language) {
        mSelectedLanguage = language;
        notifyDataSetChanged();
        return mLanguages.indexOf(mSelectedLanguage);
    }

    public LanguageModel selectLangForCode(String code) {
        if (code == null) {
            mSelectedLanguage = null;
            notifyDataSetChanged();
            return null;
        }

        for (int i = 0; i < mLanguages.size(); i++) {
            if (mLanguages.get(i).code.equals(code)) {
                mSelectedLanguage = mLanguages.get(i);
                notifyDataSetChanged();
                return mSelectedLanguage;
            }
        }
        return null;
    }

    public LanguageModel selectLangForPosition(int position) {
        mSelectedLanguage = mLanguages.get(position);
        notifyDataSetChanged();
        return mSelectedLanguage;
    }
}
