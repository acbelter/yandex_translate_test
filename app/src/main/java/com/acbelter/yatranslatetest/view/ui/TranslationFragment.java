/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.view.TranslationView;

public class TranslationFragment extends Fragment implements TranslationView {
    public static TranslationFragment newInstance() {
        return new TranslationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, container, false);
        return view;
    }

    public static String tag() {
        return TranslationFragment.class.getSimpleName();
    }
}
