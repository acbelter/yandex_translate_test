/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.acbelter.yatranslatetest.R;
import com.acbelter.yatranslatetest.view.SelectLangView;

public class SelectLangActivity extends AppCompatActivity implements SelectLangView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);
    }
}
