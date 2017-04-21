/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.adapter;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
    private static final long FAVORITE_TRANSITION_ANIM_DURATION = 250L;
    private List<HistoryItemModel> mHistory;
    private int mNotFavoriteColor;
    private int mFavoriteColor;

    private OnFavoriteClickListener mFavoriteClickListener;

    public interface OnFavoriteClickListener {
        void onFavoriteClicked(HistoryAdapter adapter, HistoryItemModel item, boolean favorite);
    }

    public HistoryAdapter(Context context,
                          List<HistoryItemModel> history,
                          OnFavoriteClickListener listener) {
        super(context, R.layout.item_history, history);
        mHistory = history;
        mNotFavoriteColor = ContextCompat.getColor(context, R.color.colorDarkGray);
        mFavoriteColor = ContextCompat.getColor(context, R.color.colorFavorite);
        mFavoriteClickListener = listener;
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

        final HistoryItemModel item = mHistory.get(position);
        holder.originalText.setText(item.originalText);
        holder.translationText.setText(item.translationText);
        holder.langsPairText.setText(buildLangsPairText(item));
        holder.favoriteImage.setColorFilter(item.isFavorite ? mFavoriteColor : mNotFavoriteColor);
        holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isFavorite) {
                    ValueAnimator anim = new ValueAnimator();
                    anim.setIntValues(mFavoriteColor, mNotFavoriteColor);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            holder.favoriteImage.setColorFilter((Integer) valueAnimator.getAnimatedValue());
                        }
                    });
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (mFavoriteClickListener != null) {
                                mFavoriteClickListener.onFavoriteClicked(
                                        HistoryAdapter.this, item, !item.isFavorite);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    anim.setDuration(FAVORITE_TRANSITION_ANIM_DURATION);
                    anim.start();
                } else {
                    ValueAnimator anim = new ValueAnimator();
                    anim.setIntValues(mNotFavoriteColor, mFavoriteColor);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            holder.favoriteImage.setColorFilter((Integer) valueAnimator.getAnimatedValue());
                        }
                    });

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (mFavoriteClickListener != null) {
                                mFavoriteClickListener.onFavoriteClicked(
                                        HistoryAdapter.this, item, !item.isFavorite);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    anim.setDuration(FAVORITE_TRANSITION_ANIM_DURATION);
                    anim.start();
                }
            }
        });

        return convertView;
    }

    private static String buildLangsPairText(HistoryItemModel item) {
        return item.langFromCode.toUpperCase() + "-" + item.langToCode.toUpperCase();
    }
}
